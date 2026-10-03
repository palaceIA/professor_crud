package professor.crud.demo.service;

public class ProfessorNaoEncontradoException
        extends RuntimeException {

    public ProfessorNaoEncontradoException(Long id) {
        super("Professor não encontrado com ID: " + id);
    }
}