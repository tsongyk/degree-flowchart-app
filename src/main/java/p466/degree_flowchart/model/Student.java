package p466.degree_flowchart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank; // ensures string fields are not null, empty, or whitespace-only

@Entity
@Table(name = "students") // Tells JPA this class represents a database table
public class Student {

  public Student() {
    
  }

  @Id // Marks studentId as the primary key of the table
  @NotBlank
  private String studentId;

  @NotBlank
  private String password;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String email;

  private String major;

  private String expectedGraduation;

  public Student(String studentId, String password, String firstName, String lastName, 
                  String email, String major, String expectedGraduation) {

    this.studentId = studentId;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.major = major;
    this.expectedGraduation = expectedGraduation;
  }

  public String getStudentId() {
    return studentId;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getMajor() {
    return major;
  }

  public String getExpectedGraduation() {
    return expectedGraduation;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  @Override
  public String toString() {
    return "Student [studentId=" + studentId + ", firstName=" + firstName + 
            ", lastName=" + lastName + ", email=" + email + ", major=" + major + 
            ", expectedGraduation=" + expectedGraduation + "]";
  }
}