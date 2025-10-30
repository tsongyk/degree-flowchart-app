package p466.degree_flowchart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import p466.degree_flowchart.data.DegreeProgressRepository;
import p466.degree_flowchart.model.DegreeProgress;
import java.util.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private DegreeProgressRepository progressRepo;

    // ✅ Save progress from frontend
    @PostMapping("/update")
    public String updateProgress(@RequestBody Map<String, Object> payload) {
        try {
            String studentId = (String) payload.get("studentId");
            String scheduleJson = (String) payload.get("scheduleJson");
            String completedCoursesJson = (String) payload.get("completedCoursesJson");

            if (studentId == null) return "Missing studentId";

            DegreeProgress progress = progressRepo.findByStudentId(studentId);
            if (progress == null) {
                progress = new DegreeProgress();
                progress.setStudentId(studentId);
            }

            progress.setScheduleJson(scheduleJson);
            progress.setCompletedCoursesJson(completedCoursesJson);
            progressRepo.save(progress);

            return "Progress saved successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error saving progress: " + e.getMessage();
        }
    }

    // ✅ Load progress for the current student
    @GetMapping("/{studentId}")
    public DegreeProgress getProgress(@PathVariable String studentId) {
        return progressRepo.findByStudentId(studentId);
    }
}
