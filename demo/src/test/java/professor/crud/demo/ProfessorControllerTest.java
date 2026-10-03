package professor.crud.demo;

import professor.crud.demo.controller.ProfessorController;
import professor.crud.demo.model.Professor;
import professor.crud.demo.service.ProfessorNaoEncontradoException;
import professor.crud.demo.service.ProfessorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfessorController.class)
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfessorService service;

    private Professor professor(Long id, String nome, String email, String area, String telefone) {
        Professor p = new Professor();
        p.setId(id);
        p.setNome(nome);
        p.setEmail(email);
        p.setArea(area);
        p.setTelefone(telefone);
        return p;
    }

    @Test
    void listarTodos_retorna200ComLista() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(
                professor(1L, "Ana Souza", "ana@universidade.br", "Matemática", "1111-1111"),
                professor(2L, "Bruno Lima", "bruno@universidade.br", "Física", "2222-2222")));

        mockMvc.perform(get("/professores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Ana Souza"))
                .andExpect(jsonPath("$[1].area").value("Física"));
    }

    @Test
    void buscarPorNome_retornaProfessoresFiltrados() throws Exception {
        when(service.buscarPorNome("ana")).thenReturn(
                List.of(professor(1L, "Ana Souza", "ana@universidade.br", "Matemática", "1111-1111")));

        mockMvc.perform(get("/professores/nome/{nome}", "ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Ana Souza"));
    }

    @Test
    void buscarPorArea_retornaProfessoresFiltrados() throws Exception {
        when(service.buscarPorArea("Física")).thenReturn(
                List.of(professor(2L, "Bruno Lima", "bruno@universidade.br", "Física", "2222-2222")));

        mockMvc.perform(get("/professores/area/{area}", "Física"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].area").value("Física"));
    }

    @Test
    void cadastrar_retorna201ComProfessorSalvo() throws Exception {
        Professor salvo = professor(3L, "Carla Mendes", "carla@universidade.br", "Química", "3333-3333");
        when(service.cadastrar(any(Professor.class))).thenReturn(salvo);

        String json = """
                {
                  "id": 3,
                  "nome": "Carla Mendes",
                  "email": "carla@universidade.br",
                  "area": "Química",
                  "telefone": "3333-3333"
                }
                """;

        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nome").value("Carla Mendes"))
                .andExpect(jsonPath("$.area").value("Química"));

        verify(service).cadastrar(any(Professor.class));
    }

    @Test
    void atualizar_retorna200ComProfessorAtualizado() throws Exception {
        Professor atualizado = professor(1L, "Ana Souza Silva", "ana.silva@universidade.br", "Matemática", "1111-1111");
        when(service.atualizar(eq(1L), any(Professor.class))).thenReturn(atualizado);

        String json = """
                {
                  "nome": "Ana Souza Silva",
                  "email": "ana.silva@universidade.br",
                  "area": "Matemática",
                  "telefone": "1111-1111"
                }
                """;

        mockMvc.perform(put("/professores/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana Souza Silva"))
                .andExpect(jsonPath("$.email").value("ana.silva@universidade.br"));
    }

    @Test
    void excluir_retorna204() throws Exception {
        mockMvc.perform(delete("/professores/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service).excluir(1L);
    }

    @Test
    void atualizarProfessorInexistente_retorna404() throws Exception {
        when(service.atualizar(eq(99L), any(Professor.class)))
                .thenThrow(new ProfessorNaoEncontradoException(99L));

        String json = """
                {
                  "nome": "Novo Nome",
                  "email": "novo@universidade.br",
                  "area": "Biologia",
                  "telefone": "9999-9999"
                }
                """;

        mockMvc.perform(put("/professores/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Professor não encontrado com ID: 99"));
    }

    @Test
    void excluirProfessorInexistente_retorna404() throws Exception {
        doThrow(new ProfessorNaoEncontradoException(99L)).when(service).excluir(99L);

        mockMvc.perform(delete("/professores/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Professor não encontrado com ID: 99"));
    }
}
