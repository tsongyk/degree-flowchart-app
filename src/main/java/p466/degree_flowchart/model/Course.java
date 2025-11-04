package p466.degree_flowchart.model;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Course {
    @Id
    private String code;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int credits;

    @ManyToMany
    private List<Course> prerequisites;

  public Course(String code, String name, Category category, int credits) {
    this.code = code; 
    this.name = name;
    this.category = category; // Categories: Computer Science Core (CSCore), Math, Writing, GenEd, Elective, Career, Specialization Course (SC), Luddy Natural Science (LNS), CS Elective (CSE)
    this.credits = credits;

  }

  public Course() {

  }

  public enum Category {
    CSCORE, DSCORE, ELECTIVE, MATH, LNS, GENED, CAREER, SC, CSE, DSE, WRITING
  }

  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public int getCredits() { return credits; }
  public void setCredits(int credits) { this.credits = credits; }

  public Category getCategory() { return category; }
  public void setCategory(Category category) { this.category = category; }

  public List<Course> getPrerequisites() { return prerequisites; }
  public void setPrerequisites(List<Course> prerequisites) { this.prerequisites = prerequisites; }
}
