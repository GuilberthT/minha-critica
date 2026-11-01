package br.ufms.minhacritica;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes das regras de acesso (Visitante, ROLE_USER, ROLE_ADMIN).
 * Usa um banco H2 em memória, populado pelo DataInitializer.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testes;DB_CLOSE_DELAY=-1")
class SegurancaTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void visitantePodeVerInicioECatalogo() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
        mvc.perform(get("/obras")).andExpect(status().isOk());
    }

    @Test
    void visitanteEnviadoAoLoginAoAbrirDetalhe() throws Exception {
        mvc.perform(get("/obras/1")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void visitanteNaoAcessaAdmin() throws Exception {
        mvc.perform(get("/admin/obras")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails("ana@minhacritica.com")
    void usuarioComumVeDetalheMasNaoAcessaAdmin() throws Exception {
        mvc.perform(get("/obras/1")).andExpect(status().isOk());
        mvc.perform(get("/admin/obras")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("bruno@minhacritica.com")
    void usuarioNaoExcluiCriticaDeOutraPessoa() throws Exception {
        // a crítica 1 é da Ana
        mvc.perform(post("/criticas/1/excluir").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin@minhacritica.com")
    void administradorAcessaCrud() throws Exception {
        mvc.perform(get("/admin/obras")).andExpect(status().isOk());
        mvc.perform(get("/admin/obras/nova")).andExpect(status().isOk());
        mvc.perform(post("/admin/obras").with(csrf())
                        .param("titulo", "Bacurau")
                        .param("tipo", "FILME")
                        .param("genero", "Suspense")
                        .param("anoLancamento", "2019")
                        .param("sinopse", "Uma pequena cidade do sertão percebe que sumiu do mapa e precisa reagir."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/obras"));
    }

    @Test
    @WithUserDetails("admin@minhacritica.com")
    void formularioInvalidoVoltaComErros() throws Exception {
        mvc.perform(post("/admin/obras").with(csrf()).param("titulo", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("obra", "titulo", "tipo", "sinopse"));
    }

    @Test
    void cadastroPublicoCriaUsuario() throws Exception {
        mvc.perform(post("/cadastro").with(csrf())
                        .param("nome", "Teste")
                        .param("email", "teste@exemplo.com")
                        .param("senha", "123456")
                        .param("confirmacaoSenha", "123456"))
                .andExpect(redirectedUrl("/login"));
    }
}
