package p466.degree_flowchart.model;

import jakarta.persistence.*;

@Entity
public class DegreeProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;

    @Lob
    private String scheduleJson; // JSON string for flowchart schedule

    @Lob
    private String completedCoursesJson; // JSON string for completed course list

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getScheduleJson() { return scheduleJson; }
    public void setScheduleJson(String scheduleJson) { this.scheduleJson = scheduleJson; }

    public String getCompletedCoursesJson() { return completedCoursesJson; }
    public void setCompletedCoursesJson(String completedCoursesJson) { this.completedCoursesJson = completedCoursesJson; }
}
