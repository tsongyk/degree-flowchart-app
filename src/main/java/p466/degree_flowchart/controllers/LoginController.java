package p466.degree_flowchart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import p466.degree_flowchart.data.StudentRepository;
import p466.degree_flowchart.model.Student;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginController.class);

    private final StudentRepository studentRepo;

    @Autowired
    public LoginController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    @GetMapping
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping
    public String processLogin(
            @RequestParam("studentId") String studentId,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        log.info("Login attempt for student ID: {}", studentId);

        Student student = studentRepo.findByStudentIdAndPassword(studentId, password);

        if (student != null) {
            session.setAttribute("student", student);
            log.info("Login successful for: {}", student.getFullName());
            return "redirect:/dashboard";
        } else {
            log.warn("Login failed for student ID: {}", studentId);
            redirectAttributes.addFlashAttribute("error", "Invalid student ID or password");
            return "redirect:/login";
        }
    }
}
