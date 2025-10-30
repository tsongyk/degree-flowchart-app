package p466.degree_flowchart.data;

import org.springframework.data.repository.CrudRepository;
import p466.degree_flowchart.model.DegreeProgress;

public interface DegreeProgressRepository extends CrudRepository<DegreeProgress, Long> {
    DegreeProgress findByStudentId(String studentId);
}
