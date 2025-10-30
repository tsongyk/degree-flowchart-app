package p466.degree_flowchart.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import p466.degree_flowchart.model.Semester;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    @Override
    @NonNull
    List<Semester> findAll();

    List<Semester> findBySemesterNameIn(List<String> names);
    Semester findBySemesterName(String name);
}
