// ===============================
//  GLOBAL VARIABLES
// ===============================
let currentStudentId = null;
let courseCatalog = {};

let appState = {
  schedule: {
    'Year 1': { 'Fall': [], 'Spring': [] },
    'Year 2': { 'Fall': [], 'Spring': [] },
    'Year 3': { 'Fall': [], 'Spring': [] },
    'Year 4': { 'Fall': [], 'Spring': [] }
  },
  completedCourses: new Set(),
  currentSemester: null
};

// ===============================
//  HELPERS
// ===============================
function getStorageKey() {
  return currentStudentId
    ? `degreeTrackerState_${currentStudentId}`
    : 'degreeTrackerState';
}

/*******************************
 *  BACKEND SYNC HELPERS
 *******************************/
async function loadProgressFromServer() {
  if (!currentStudentId) return;
  try {
    const res = await fetch(`/api/progress/${currentStudentId}`);
    if (!res.ok) return;
    const progress = await res.json();

    if (progress && progress.scheduleJson) {
      appState.schedule = JSON.parse(progress.scheduleJson);
      appState.completedCourses = new Set(JSON.parse(progress.completedCoursesJson || '[]'));
      console.log(`✅ Loaded saved progress for student ${currentStudentId}`);
    }
  } catch (err) {
    console.error("Error loading progress:", err);
  }
}

async function syncProgressToServer() {
  if (!currentStudentId) return;
  try {
    await fetch('/api/progress/update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        studentId: currentStudentId,
        scheduleJson: JSON.stringify(appState.schedule),
        completedCoursesJson: JSON.stringify(Array.from(appState.completedCourses))
      })
    });
    console.log("✅ Progress synced to server");
  } catch (err) {
    console.error("Error syncing progress:", err);
  }
}


// ===============================
//  STUDENT INFO
// ===============================
async function loadCurrentStudent() {
  try {
    const response = await fetch('/api/student/current');
    const student = await response.json();

    if (student && student.studentId) {
      currentStudentId = student.studentId;

      document.getElementById('studentName').textContent =
        `${student.firstName} ${student.lastName}`;
      document.getElementById('studentMajor').textContent = student.major;
      document.getElementById('gradDate').textContent = student.expectedGraduation;
    } else {
      console.warn('⚠️ No student session found. Redirecting to login.');
      window.location.href = '/login';
    }
  } catch (err) {
    console.error('Error loading student info:', err);
    window.location.href = '/login';
  }
}

// ===============================
//  COURSE DATA
// ===============================
async function loadCourses() {
  try {
    const response = await fetch('/api/courses');
    const data = await response.json();

    courseCatalog = {};
    data.forEach(c => {
      courseCatalog[c.code] = {
        name: c.name,
        credits: c.credits,
        category: c.category.toLowerCase(),
        prereqs: c.prereqs || []
      };
    });

    console.log('✅ Loaded courses:', courseCatalog);
  } catch (err) {
    console.error('Error fetching courses:', err);
  }
}

// ===============================
//  CONTEXT MENU (RIGHT-CLICK)
// ===============================
function showContextMenu(e, courseCode) {
  // Remove any existing context menu
  const existing = document.querySelector('.context-menu');
  if (existing) existing.remove();

  const menu = document.createElement('div');
  menu.className = 'context-menu';
  menu.style.position = 'absolute';
  menu.style.left = `${e.pageX}px`;
  menu.style.top = `${e.pageY}px`;
  menu.style.background = 'white';
  menu.style.border = '1px solid #ccc';
  menu.style.borderRadius = '6px';
  menu.style.boxShadow = '0 2px 8px rgba(0,0,0,0.15)';
  menu.style.zIndex = '9999';
  menu.style.padding = '4px 0';
  menu.style.fontFamily = 'Segoe UI, sans-serif';
  menu.style.minWidth = '150px';

  const isCompleted = appState.completedCourses.has(courseCode);

  const toggleItem = document.createElement('div');
  toggleItem.className = 'context-menu-item';
  toggleItem.textContent = isCompleted ? 'Mark Incomplete' : 'Mark Complete';
  toggleItem.style.padding = '8px 12px';
  toggleItem.style.cursor = 'pointer';
  toggleItem.addEventListener('mouseenter', () => toggleItem.style.background = '#f2f2f2');
  toggleItem.addEventListener('mouseleave', () => toggleItem.style.background = 'white');
  toggleItem.addEventListener('click', () => {
    toggleCourseCompletion(courseCode);
    menu.remove();
  });

  const removeItem = document.createElement('div');
  removeItem.className = 'context-menu-item';
  removeItem.textContent = 'Remove Course';
  removeItem.style.padding = '8px 12px';
  removeItem.style.color = 'red';
  removeItem.style.cursor = 'pointer';
  removeItem.addEventListener('mouseenter', () => removeItem.style.background = '#f2f2f2');
  removeItem.addEventListener('mouseleave', () => removeItem.style.background = 'white');
  removeItem.addEventListener('click', () => {
    removeCourse(courseCode);
    menu.remove();
  });

  menu.appendChild(toggleItem);
  menu.appendChild(removeItem);
  document.body.appendChild(menu);

  // Close when clicking anywhere else
  setTimeout(() => {
    document.addEventListener('click', function closeMenu() {
      menu.remove();
      document.removeEventListener('click', closeMenu);
    });
  }, 10);
}

/*******************************
 *  COURSE SIDEBAR & SELECTION
 *******************************/

// Open the sidebar for a given semester
function openCourseSidebar(year, semester) {
  appState.currentSemester = { year, semester };
  document.getElementById('selectedSemester').textContent = `${year} - ${semester}`;
  
  const currentCredits = calculateSemesterCredits(year, semester);
  document.getElementById('currentCredits').textContent = currentCredits;

  populateCourseList();
  document.getElementById('courseSidebar').classList.add('open');
  document.getElementById('overlay').classList.add('active');
}

// Close sidebar
function closeSidebar() {
  document.getElementById('courseSidebar').classList.remove('open');
  document.getElementById('overlay').classList.remove('active');
}

// Calculate semester credits
function calculateSemesterCredits(year, semester) {
  const courses = appState.schedule[year][semester];
  return courses.reduce((sum, code) => sum + courseCatalog[code].credits, 0);
}

// Populate the sidebar with available courses
function populateCourseList() {
  const courseList = document.getElementById('courseList');
  courseList.innerHTML = '';

  const searchTerm = document.getElementById('courseSearch').value.toLowerCase();
  const { year, semester } = appState.currentSemester;
  const currentCredits = calculateSemesterCredits(year, semester);
  const scheduledCourses = appState.schedule[year][semester];

  Object.keys(courseCatalog).forEach(code => {
    const course = courseCatalog[code];

    // Filter by search
    if (searchTerm && !code.toLowerCase().includes(searchTerm) && !course.name.toLowerCase().includes(searchTerm)) {
      return;
    }

    // Skip if already in schedule
    if (scheduledCourses.includes(code)) return;

    const item = document.createElement('div');
    item.className = 'course-item';

    const prereqsMet = course.prereqs.every(p => appState.completedCourses.has(p));
    const canAdd = prereqsMet && (currentCredits + course.credits <= 19);

    if (!canAdd) item.classList.add('disabled');

    const prereqText = course.prereqs.length
      ? `Prerequisites: ${course.prereqs.join(', ')}`
      : 'No prerequisites';

    item.innerHTML = `
      <div class="course-item-header">
        <div class="course-item-code">${code}</div>
        <div class="course-item-credits">${course.credits} credits</div>
      </div>
      <div class="course-item-name">${course.name}</div>
      <div class="course-item-prereq">${prereqText}</div>
    `;

    if (canAdd) {
      item.addEventListener('click', () => addCourse(code));
    }

    courseList.appendChild(item);
  });
}

// Hook up sidebar search box
document.getElementById('courseSearch').addEventListener('input', populateCourseList);

// Overlay click closes sidebar
document.getElementById('overlay').addEventListener('click', closeSidebar);


// ===============================
//  STATE MANAGEMENT
// ===============================
function loadState() {
  const key = getStorageKey();
  const saved = localStorage.getItem(key);
  if (saved) {
    const parsed = JSON.parse(saved);
    appState.schedule = parsed.schedule;
    appState.completedCourses = new Set(parsed.completedCourses);
    console.log(`📂 Loaded flowchart for ${key}`, parsed);
  } else {
    console.log(`No saved data for ${key}`);
  }
}

function saveState() {
  const toSave = {
    schedule: appState.schedule,
    completedCourses: Array.from(appState.completedCourses)
  };
  const key = getStorageKey();
  localStorage.setItem(key, JSON.stringify(toSave));
  syncProgressToServer(); // ✅ Save remotely too
}


// ===============================
//  FLOWCHART RENDERING
// ===============================
function initFlowchart() {
  const flowchart = document.getElementById('flowchart');
  flowchart.innerHTML = '';

  const years = ['Year 1', 'Year 2', 'Year 3', 'Year 4'];
  const semesters = ['Fall', 'Spring'];

  years.forEach(year => {
    const yearSection = document.createElement('div');
    yearSection.className = 'year-section';

    const yearTitle = document.createElement('h2');
    yearTitle.className = 'year-title';
    yearTitle.textContent = year;
    yearSection.appendChild(yearTitle);

    semesters.forEach(semester => {
      const semesterRow = document.createElement('div');
      semesterRow.className = 'semester-row';

      const label = document.createElement('div');
      label.className = 'semester-label';
      label.textContent = semester;
      semesterRow.appendChild(label);

      const coursesContainer = document.createElement('div');
      coursesContainer.className = 'semester-courses';
      coursesContainer.dataset.year = year;
      coursesContainer.dataset.semester = semester;

      const courses = appState.schedule[year][semester];
      courses.forEach(courseCode => {
        coursesContainer.appendChild(createCourseNode(courseCode));
      });

      coursesContainer.appendChild(createPlaceholderNode(year, semester));
      semesterRow.appendChild(coursesContainer);
      yearSection.appendChild(semesterRow);
    });

    flowchart.appendChild(yearSection);
  });

  setTimeout(drawArrows, 100);
}

// ===============================
//  COURSE NODES
// ===============================
function createCourseNode(courseCode) {
  const course = courseCatalog[courseCode];
  const node = document.createElement('div');
  node.className = 'course-node';
  node.dataset.courseCode = courseCode;

  if (appState.completedCourses.has(courseCode)) node.classList.add('completed');

  node.innerHTML = `
    <input type="checkbox" class="course-checkbox" ${appState.completedCourses.has(courseCode) ? 'checked' : ''}>
    <div class="course-code">${courseCode}</div>
    <div class="course-name">${course.name}</div>
    <div class="course-credits">${course.credits} credits</div>
  `;

  node.querySelector('.course-checkbox').addEventListener('change', () => {
    toggleCourseCompletion(courseCode);
  });

  node.addEventListener('contextmenu', e => {
    e.preventDefault();
    showContextMenu(e, courseCode);
  });

  return node;
}

function createPlaceholderNode(year, semester) {
  const node = document.createElement('div');
  node.className = 'course-node placeholder';
  node.textContent = '+ Add Course';
  node.dataset.year = year;
  node.dataset.semester = semester;
  node.addEventListener('click', () => openCourseSidebar(year, semester));
  return node;
}

// ===============================
//  COURSE ACTIONS
// ===============================
function toggleCourseCompletion(courseCode) {
  if (appState.completedCourses.has(courseCode)) {
    appState.completedCourses.delete(courseCode);
  } else {
    appState.completedCourses.add(courseCode);
  }
  saveState();
  initFlowchart();
  updateProgress();
}

function addCourse(courseCode) {
  const { year, semester } = appState.currentSemester;
  appState.schedule[year][semester].push(courseCode);
  saveState();
  initFlowchart();
  updateProgress();
  openCourseSidebar(year, semester);
  setTimeout(drawArrows, 50);
}

function removeCourse(courseCode) {
  for (let year in appState.schedule) {
    for (let sem in appState.schedule[year]) {
      appState.schedule[year][sem] =
        appState.schedule[year][sem].filter(c => c !== courseCode);
    }
  }
  appState.completedCourses.delete(courseCode);
  saveState();
  initFlowchart();
  updateProgress();
}

// ===============================
//  ARROWS
// ===============================
function drawArrows() {
  const container = document.querySelector('.flowchart-container');
  const flowchart = document.getElementById('flowchart');
  const svg = document.getElementById('arrowCanvas');
  if (!container || !flowchart || !svg) return;

  // Match SVG size and position to the full flowchart content
  svg.style.left = flowchart.offsetLeft + 'px';
  svg.style.top = flowchart.offsetTop + 'px';
  svg.setAttribute('width', flowchart.scrollWidth);
  svg.setAttribute('height', flowchart.scrollHeight);
  svg.innerHTML = '';

  // Arrowhead
  if (!svg.querySelector('#arrowhead')) {
    const defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
    const marker = document.createElementNS('http://www.w3.org/2000/svg', 'marker');
    marker.setAttribute('id', 'arrowhead');
    marker.setAttribute('markerWidth', '10');
    marker.setAttribute('markerHeight', '10');
    marker.setAttribute('refX', '5');
    marker.setAttribute('refY', '5');
    marker.setAttribute('orient', 'auto');
    marker.setAttribute('markerUnits', 'userSpaceOnUse');

    const polygon = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
    polygon.setAttribute('points', '0 0, 10 5, 0 10');
    polygon.setAttribute('fill', '#9e9e9e');
    polygon.setAttribute('opacity', '0.6');

    marker.appendChild(polygon);
    defs.appendChild(marker);
    svg.appendChild(defs);
  }

  // Helper: coordinates relative to flowchart content
  const pos = el => {
    const r = el.getBoundingClientRect();
    const rf = flowchart.getBoundingClientRect();
    return {
      x: r.left - rf.left + container.scrollLeft,
      y: r.top - rf.top + container.scrollTop
    };
  };

  document.querySelectorAll('.course-node:not(.placeholder)').forEach(node => {
    const code = node.dataset.courseCode;
    const course = courseCatalog[code];
    if (!course || !course.prereqs) return;

    // Target course top edge midpoint
    const target = pos(node);
    const x2 = target.x + node.offsetWidth / 2;
    const y2 = target.y; // top edge

    course.prereqs.forEach(pr => {
      const prereq = document.querySelector(`.course-node[data-course-code="${pr}"]`);
      if (!prereq) return;

      const src = pos(prereq);
      const x1 = src.x + prereq.offsetWidth / 2;
      const y1 = src.y + prereq.offsetHeight; // bottom edge

      // Control points for a smooth downward → upward curve
      const verticalGap = (y2 - y1) / 2;
      const c1x = x1;
      const c1y = y1 + Math.max(40, verticalGap / 2);
      const c2x = x2;
      const c2y = y2 - Math.max(40, verticalGap / 2);

      const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      path.setAttribute('d', `M ${x1} ${y1} C ${c1x} ${c1y}, ${c2x} ${c2y}, ${x2} ${y2}`);
      path.setAttribute('stroke', '#9e9e9e');
      path.setAttribute('stroke-width', '2.5');
      path.setAttribute('fill', 'none');
      path.setAttribute('marker-end', 'url(#arrowhead)');
      path.setAttribute('opacity', '0.6');

      svg.appendChild(path);
    });
  });
}



// ===============================
//  PROGRESS + SYNC
// ===============================
function updateProgress() {
  let totalCredits = 0;
  const cat = { cscore: 0, cse: 0, math: 0, gened: 0, elective: 0 };

  appState.completedCourses.forEach(code => {
    const c = courseCatalog[code];
    if (!c) return;
    totalCredits += c.credits;
    if (cat[c.category] !== undefined) cat[c.category] += c.credits;
  });

  const pct = (totalCredits / 120) * 100;
  document.querySelector('#overallProgress .progress-fill').style.width = pct + '%';
  document.getElementById('overallProgressText').textContent = `${totalCredits} / 120 credits`;

  updateCategoryProgress('cscore', cat.cscore, 16, 'csCoreComplete');
  updateCategoryProgress('cse', cat.cse, 45, 'csElectiveComplete');
  updateCategoryProgress('math', cat.math, 12, 'mathComplete');
  updateCategoryProgress('gened', cat.gened, 30, 'genEdComplete');
  updateCategoryProgress('elective', cat.elective, 17, 'electiveComplete');
}

function updateCategoryProgress(className, current, required, textId) {
  const pct = Math.min((current / required) * 100, 100);
  document.querySelector(`.progress-fill.${className}`).style.width = pct + '%';
  document.getElementById(textId).textContent = current;
}

async function syncProgressToServer() {
  if (!currentStudentId) return;
  await fetch('/api/progress/update', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      studentId: currentStudentId,
      scheduleJson: JSON.stringify(appState.schedule),
      completedCoursesJson: JSON.stringify(Array.from(appState.completedCourses))
    })
  });
}

// ===============================
//  MAIN INIT SEQUENCE
// ===============================
window.addEventListener('load', async () => {
  await loadCurrentStudent();         // sets currentStudentId
  await loadProgressFromServer();     // 🔄 loads from DB if available
  await loadCourses();                // loads catalog
  initFlowchart();
  updateProgress();
});


// ===============================
//  SCROLL + RESIZE
// ===============================
window.addEventListener('resize', drawArrows);
document.querySelector('.flowchart-container')?.addEventListener('scroll', () => {
  requestAnimationFrame(drawArrows);
});

/*******************************
 *  LOGOUT FUNCTION
 *******************************/
async function logout() {
  try {
    // Optional: only clear this student's local data, not everyone’s
    if (currentStudentId) {
      localStorage.removeItem(`degreeTrackerState_${currentStudentId}`);
    } else {
      localStorage.clear();
    }

    // Call backend logout endpoint (Spring Security handles this)
    await fetch('/logout', { method: 'GET' });

    // Redirect to login page
    window.location.href = '/login';
  } catch (err) {
    console.error('Logout failed:', err);
    window.location.href = '/login'; // fallback
  }
}

document.addEventListener('DOMContentLoaded', () => {
  const container = document.querySelector('.flowchart-container');
  const svg = document.getElementById('arrowCanvas');
  if (!container || !svg) return;

  // Keep arrows visually aligned during scrolling
  container.addEventListener('scroll', () => {
    const { scrollLeft, scrollTop } = container;
    svg.style.transform = `translate(${-scrollLeft}px, ${-scrollTop}px)`;
  });
});


window.addEventListener('resize', drawArrows);
setTimeout(drawArrows, 0); // after initFlowchart()
