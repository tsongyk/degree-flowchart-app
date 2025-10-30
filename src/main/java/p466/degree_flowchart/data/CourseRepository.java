package p466.degree_flowchart.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p466.degree_flowchart.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    
}
