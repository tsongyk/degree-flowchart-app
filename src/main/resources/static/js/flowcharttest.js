// ===============================
//  GLOBAL VARIABLES
// ===============================
let currentStudentId = null;
let currentMajor = "computer science";
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
//  MAJOR CONFIGURATION MAP
// ===============================
const MAJOR_CONFIG = {
  "computer science": {
    labels: {
      cscore: "CS Core Credits",
      cse: "CS Elective Credits",
      math: "Math Credits",
      gened: "General Education",
      elective: "Free Electives",
      writing: "Intensive Writing"
    },
    requirements: {
      cscore: 16,
      cse: 45,
      math: 12,
      gened: 30,
      elective: 17,
      writing: 3,
      overall: 120
    }
  },
  "data science": {
    labels: {
      cscore: "DS Core Credits",
      cse: "DS Elective Credits",
      math: "Statistics Credits",
      gened: "General Education",
      elective: "Free Electives",
      writing: "Intensive Writing"
    },
    requirements: {
      cscore: 34,
      cse: 27,
      math: 10,
      gened: 30,
      elective: 17,
      writing: 3,
      overall: 120
    }
  }
};

// ===============================
//  APPLY MAJOR CONFIG
// ===============================
function applyMajorConfig(major) {
  const config = MAJOR_CONFIG[major] || MAJOR_CONFIG["computer science"];

  // Update labels
  document.querySelectorAll('.requirement-block').forEach(block => {
    const h3 = block.querySelector('h3');
    if (h3) {
      if (block.querySelector('.progress-fill.cscore')) h3.textContent = config.labels.cscore;
      if (block.querySelector('.progress-fill.cse')) h3.textContent = config.labels.cse;
      if (block.querySelector('.progress-fill.math')) h3.textContent = config.labels.math;
      if (block.querySelector('.progress-fill.gened')) h3.textContent = config.labels.gened;
      if (block.querySelector('.progress-fill.elective')) h3.textContent = config.labels.elective;
      if (block.querySelector('.progress-fill.writing')) h3.textContent = config.labels.writing;
    }
  });

  // Update required credit labels (text next to totals)
  const updateCreditText = (id, value) => {
    const el = document.getElementById(id);
    if (el && el.nextSibling && el.nextSibling.nodeType === Node.TEXT_NODE) {
      el.nextSibling.textContent = ` / ${value} credits`;
    }
  };

  updateCreditText('csCoreComplete', config.requirements.cscore);
  updateCreditText('csElectiveComplete', config.requirements.cse);
  updateCreditText('mathComplete', config.requirements.math);
  updateCreditText('genEdComplete', config.requirements.gened);
  updateCreditText('electiveComplete', config.requirements.elective);
  updateCreditText('writingComplete', config.requirements.writing);
}

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

      // 🧩 Set global major and apply config
      currentMajor = student.major.toLowerCase();
      applyMajorConfig(currentMajor);

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
//  UPDATE PROGRESS (MODIFIED)
// ===============================
function updateProgress() {
  const config = MAJOR_CONFIG[currentMajor] || MAJOR_CONFIG["computer science"];
  const req = config.requirements;

  let totalCredits = 0;
  const cat = { cscore: 0, cse: 0, math: 0, gened: 0, elective: 0, writing: 0 };

  appState.completedCourses.forEach(code => {
    const c = courseCatalog[code];
    if (!c) return;
    totalCredits += c.credits;
    if (cat[c.category] !== undefined) cat[c.category] += c.credits;
  });

  // Overall Progress
  const pct = (totalCredits / req.overall) * 100;
  document.querySelector('#overallProgress .progress-fill').style.width = pct + '%';
  document.getElementById('overallProgressText').textContent =
    `${totalCredits} / ${req.overall} credits`;

  // Per-category Progress
  updateCategoryProgress('cscore', cat.cscore, req.cscore, 'csCoreComplete');
  updateCategoryProgress('cse', cat.cse, req.cse, 'csElectiveComplete');
  updateCategoryProgress('math', cat.math, req.math, 'mathComplete');
  updateCategoryProgress('gened', cat.gened, req.gened, 'genEdComplete');
  updateCategoryProgress('elective', cat.elective, req.elective, 'electiveComplete');
  updateCategoryProgress('writing', cat.writing, req.writing, 'writingComplete');
}

function updateCategoryProgress(className, current, required, textId) {
  const pct = Math.min((current / required) * 100, 100);
  document.querySelector(`.progress-fill.${className}`).style.width = pct + '%';
  document.getElementById(textId).textContent = current;
}


// ===============================
//  MAIN INIT SEQUENCE
// ===============================
window.addEventListener('load', async () => {
  await loadCurrentStudent();         // sets currentStudentId and major
  await loadProgressFromServer();     // loads saved schedule
  await loadCourses();                // loads catalog
  initFlowchart();                    // builds chart
  updateProgress();                   // updates bars
});
