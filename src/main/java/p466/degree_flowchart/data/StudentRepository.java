package p466.degree_flowchart.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p466.degree_flowchart.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByStudentIdAndPassword(String studentId, String password);
    Student findByStudentId(String studentId);
}
