package p466.degree_flowchart.controllers;

import org.springframework.web.bind.annotation.*;
import p466.degree_flowchart.data.CourseRepository;
import p466.degree_flowchart.model.Course;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    private final CourseRepository courseRepo;

    public CourseController(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseRepo.findAll().stream()
            .map(CourseDTO::new)
            .collect(Collectors.toList());
    }

    static class CourseDTO {
        public String code;
        public String name;
        public String category;
        public int credits;
        public List<String> prereqs;

        public CourseDTO(Course course) {
            this.code = course.getCode();
            this.name = course.getName();
            this.category = course.getCategory().toString();
            this.credits = course.getCredits();
            this.prereqs = course.getPrerequisites() == null
                ? List.of()
                : course.getPrerequisites().stream()
                        .map(Course::getCode)
                        .collect(Collectors.toList());
        }
    }
}
