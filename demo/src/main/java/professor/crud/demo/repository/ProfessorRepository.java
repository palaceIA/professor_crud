package professor.crud.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import professor.crud.demo.model.Professor;
import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor,Long> {
    List<Professor> findByNomeContainingIgnoreCase(String nome);
    List<Professor> findByAreaIgnoreCase(String area);
}
