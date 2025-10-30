package p466.degree_flowchart.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Semester {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Size(min = 3, message = "Semester name must be at least 3 characters long")
  private String semesterName;

  @NotNull
  @Size(min = 1, message = "You must choose at least 1 course")
  @ManyToMany()
  private List<Course> courses = new ArrayList<>();

  public String getSemesterName() {
    return semesterName;
  }

  public void setSemesterName(String semesterName) {
    this.semesterName = semesterName;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getTotalCredits() {
    return courses.stream().mapToInt(Course::getCredits).sum();
  }

  @Override
  public String toString() {
    return "Semester [semesterName=" + semesterName + ", courses=" + courses + "]";
  }
}