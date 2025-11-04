package p466.degree_flowchart.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import p466.degree_flowchart.model.Student;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDashboardPageLoads() throws Exception {
        // 🧩 Create a mock student and store it in the session
        Student mockStudent = new Student();
        mockStudent.setStudentId("1111");
        mockStudent.setFirstName("Alex");
        mockStudent.setFirstName("Johnson");
        mockStudent.setMajor("Computer Science");
        mockStudent.setSpecialization("Software Engineering");
        mockStudent.setEmail("mock@iu.edu");
        mockStudent.setExpectedGraduation("2027");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("student", mockStudent);

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(content().string(containsString("Degree Progress Tracker")))
                .andExpect(content().string(containsString("Overall Degree Progress")));
    }

    @Test
    void testRedirectWhenNoStudentInSession() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
