package p466.degree_flowchart.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import p466.degree_flowchart.data.*;
import p466.degree_flowchart.model.*;

import java.util.*;

@Controller
public class DashboardController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private DegreeProgressRepository progressRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return "redirect:/login";
        }

        String studentId = student.getStudentId();
        DegreeProgress progress = progressRepo.findByStudentId(studentId);
        if (progress == null) {
            progress = new DegreeProgress();
            progress.setStudentId(studentId);
            progressRepo.save(progress);
        }

        model.addAttribute("student", student);
        return "dashboard";
    }

    /** ✅ Returns JSON summary for dashboard frontend */
    @GetMapping("/api/dashboard")
    @ResponseBody
    public Map<String, Object> getDashboardData(HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) {
            return Map.of("error", "Not logged in");
        }

        String studentId = student.getStudentId();
        DegreeProgress progress = progressRepo.findByStudentId(studentId);
        List<Course> allCourses = courseRepo.findAll();

        int totalCredits = allCourses.stream().mapToInt(Course::getCredits).sum();
        int completedCredits = 0;

        if (progress != null && progress.getCompletedCoursesJson() != null) {
            try {
                List<String> completedCourseCodes = mapper.readValue(
                        progress.getCompletedCoursesJson(),
                        new TypeReference<List<String>>() {}
                );

                for (String code : completedCourseCodes) {
                    for (Course c : allCourses) {
                        if (c.getCode().equals(code)) {
                            completedCredits += c.getCredits();
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int percentCompleted = totalCredits > 0
                ? (int) Math.round((completedCredits * 100.0) / totalCredits)
                : 0;

        Map<String, Object> data = new HashMap<>();
        data.put("student", student);
        data.put("totalCredits", totalCredits);
        data.put("completedCredits", completedCredits);
        data.put("percentCompleted", percentCompleted);

        return data;
    }
}
