package professor.crud.demo.service;

import org.springframework.stereotype.Service;
import professor.crud.demo.model.Professor;
import professor.crud.demo.repository.ProfessorRepository;
import java.util.List;

@Service 
public class ProfessorService {
    private final ProfessorRepository repository ; 

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public List<Professor> listarTodos(){
        return repository.findAll() ; 
    }

    public List<Professor> buscarPorNome(String nome){
        return repository.findByNomeContainingIgnoreCase(nome) ; 
    }

    public List<Professor> buscarPorArea(String area){
        return  repository.findByAreaIgnoreCase(area) ; 
    }

    public Professor cadastrar(Professor professor){
        return  repository.save(professor) ; 
    }

     public Professor atualizar(Long id, Professor dados) {
        Professor professor = repository.findById(id)
                .orElseThrow(() ->
                        new ProfessorNaoEncontradoException(id));

        professor.setNome(dados.getNome());
        professor.setEmail(dados.getEmail());
        professor.setArea(dados.getArea());
        professor.setTelefone(dados.getTelefone());

        return repository.save(professor);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ProfessorNaoEncontradoException(id);
        }

        repository.deleteById(id);
    }

}
