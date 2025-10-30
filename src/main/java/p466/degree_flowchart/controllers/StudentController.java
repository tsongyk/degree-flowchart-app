package p466.degree_flowchart.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import p466.degree_flowchart.data.StudentRepository;
import p466.degree_flowchart.model.Student;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    /** ✅ Return currently logged-in student from session */
    @GetMapping("/current")
    public Optional<Student> getCurrentStudent(HttpSession session) {
        Student student = (Student) session.getAttribute("student");
        if (student == null) return null;
        // Optional: refresh from DB if needed
        return Optional.of(studentRepo.findByStudentId(student.getStudentId()));
    }
}
