    package professor.crud.demo.controller;

    import professor.crud.demo.model.Professor;
    import professor.crud.demo.service.ProfessorNaoEncontradoException;
    import professor.crud.demo.service.ProfessorService;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/professores")
    public class ProfessorController {

        private final ProfessorService service;

        public ProfessorController(ProfessorService service) {
            this.service = service;
        }

        @GetMapping
        public List<Professor> listarTodos() {
            return service.listarTodos();
        }

        @GetMapping("/nome/{nome}")
        public List<Professor> buscarPorNome(@PathVariable String nome) {
            return service.buscarPorNome(nome);
        }

        @GetMapping("/area/{area}")
        public List<Professor> buscarPorArea(@PathVariable String area) {
            return service.buscarPorArea(area);
        }

        @PostMapping
        public ResponseEntity<Professor> cadastrar(@RequestBody Professor professor) {
            Professor salvo = service.cadastrar(professor);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(salvo);
        }

        @PutMapping("/{id}")
        public Professor atualizar(@PathVariable Long id,@RequestBody Professor professor) {
            return service.atualizar(id, professor);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> excluir(@PathVariable Long id) {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        }

        @ExceptionHandler(ProfessorNaoEncontradoException.class)
        public ResponseEntity<Map<String, String>> tratarNaoEncontrado(
                ProfessorNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", ex.getMessage()));
        }
    }