package p466.degree_flowchart.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import p466.degree_flowchart.data.DegreeProgressRepository;
import p466.degree_flowchart.model.DegreeProgress;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/progress")
public class DegreeProgressController {

    @Autowired
    private DegreeProgressRepository progressRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    /** ✅ Save full schedule + completed courses JSON */
    @PostMapping("/save")
    public Map<String, Object> saveProgress(@RequestBody Map<String, Object> body, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get student from session
            Object studentObj = session.getAttribute("student");
            if (studentObj == null) {
                response.put("error", "Not logged in");
                return response;
            }

            String studentId = (String) mapper.convertValue(((Map<?, ?>) mapper.convertValue(studentObj, Map.class)).get("studentId"), String.class);
            DegreeProgress progress = progressRepo.findByStudentId(studentId);

            if (progress == null) {
                progress = new DegreeProgress();
                progress.setStudentId(studentId);
            }

            // Store schedule and completedCourses as JSON
            if (body.containsKey("schedule")) {
                progress.setScheduleJson(mapper.writeValueAsString(body.get("schedule")));
            }
            if (body.containsKey("completedCourses")) {
                progress.setCompletedCoursesJson(mapper.writeValueAsString(body.get("completedCourses")));
            }

            progressRepo.save(progress);
            response.put("status", "saved");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }

        return response;
    }

    /** ✅ Toggle completed course */
    @PostMapping("/toggleComplete")
    public Map<String, Object> toggleComplete(@RequestBody Map<String, String> body, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            String code = body.get("courseCode");
            if (code == null || code.isEmpty()) {
                response.put("error", "No course code provided");
                return response;
            }

            Object studentObj = session.getAttribute("student");
            if (studentObj == null) {
                response.put("error", "Not logged in");
                return response;
            }

            String studentId = (String) mapper.convertValue(((Map<?, ?>) mapper.convertValue(studentObj, Map.class)).get("studentId"), String.class);
            DegreeProgress progress = progressRepo.findByStudentId(studentId);

            if (progress == null) {
                progress = new DegreeProgress();
                progress.setStudentId(studentId);
            }

            List<String> completed = new ArrayList<>();
            if (progress.getCompletedCoursesJson() != null) {
                completed = mapper.readValue(progress.getCompletedCoursesJson(), new TypeReference<List<String>>() {});
            }

            if (completed.contains(code)) {
                completed.remove(code);
            } else {
                completed.add(code);
            }

            progress.setCompletedCoursesJson(mapper.writeValueAsString(completed));
            progressRepo.save(progress);

            response.put("status", "updated");
            response.put("completedCourses", completed);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }

        return response;
    }

    /** ✅ Load saved progress */
    @GetMapping("/load")
    public Map<String, Object> loadProgress(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Object studentObj = session.getAttribute("student");
            if (studentObj == null) {
                response.put("error", "Not logged in");
                return response;
            }

            String studentId = (String) mapper.convertValue(((Map<?, ?>) mapper.convertValue(studentObj, Map.class)).get("studentId"), String.class);
            DegreeProgress progress = progressRepo.findByStudentId(studentId);

            if (progress == null) {
                response.put("schedule", "{}");
                response.put("completedCourses", "[]");
            } else {
                response.put("schedule", progress.getScheduleJson());
                response.put("completedCourses", progress.getCompletedCoursesJson());
            }

            response.put("status", "loaded");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }

        return response;
    }
}
