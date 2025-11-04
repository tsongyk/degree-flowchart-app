package p466.degree_flowchart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
public class Student {

  public Student() {
  }

  @Id
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

  private String specialization;

  private String expectedGraduation;

  // -------------------------------
  // Constructor
  // -------------------------------
  public Student(String studentId, String password, String firstName, String lastName,
                 String email, String major, String specialization, String expectedGraduation) {
    this.studentId = studentId;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.major = major;
    this.specialization = specialization;
    this.expectedGraduation = expectedGraduation;
  }

  // -------------------------------
  // Getters
  // -------------------------------
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

  public String getSpecialization() {
    return specialization;
  }

  public String getExpectedGraduation() {
    return expectedGraduation;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setMajor(String major) {
    this.major = major;
  }

  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }

  public void setExpectedGraduation(String expectedGraduation) {
    this.expectedGraduation = expectedGraduation;
  }

  @Override
  public String toString() {
    return "Student [studentId=" + studentId + ", firstName=" + firstName +
           ", lastName=" + lastName + ", email=" + email + ", major=" + major +
           ", specialization=" + specialization +
           ", expectedGraduation=" + expectedGraduation + "]";
  }
}
