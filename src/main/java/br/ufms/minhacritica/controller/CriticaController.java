package br.ufms.minhacritica.controller;

import br.ufms.minhacritica.dto.CriticaForm;
import br.ufms.minhacritica.service.CriticaService;
import br.ufms.minhacritica.service.ObraService;
import br.ufms.minhacritica.service.RegraNegocioException;
import br.ufms.minhacritica.service.UsuarioLogado;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Publicação e exclusão de críticas (usuário logado; admin modera). */
@Controller
public class CriticaController {

    private final CriticaService criticaService;
    private final ObraService obraService;

    public CriticaController(CriticaService criticaService, ObraService obraService) {
        this.criticaService = criticaService;
        this.obraService = obraService;
    }

    @PostMapping("/obras/{obraId}/criticas")
    public String publicar(@PathVariable Long obraId,
                           @Valid @ModelAttribute("criticaForm") CriticaForm form, BindingResult resultado,
                           @AuthenticationPrincipal UsuarioLogado usuario,
                           Model model, RedirectAttributes redirect) {
        if (!resultado.hasErrors()) {
            try {
                criticaService.publicar(obraId, form, usuario.getId());
                redirect.addFlashAttribute("sucesso", "Sua crítica foi publicada.");
                return "redirect:/obras/" + obraId + "#criticas";
            } catch (RegraNegocioException e) {
                model.addAttribute("erro", e.getMessage());
            }
        }
        model.addAttribute("obra", obraService.buscar(obraId));
        model.addAttribute("jaCriticou", criticaService.jaCriticou(usuario.getId(), obraId));
        return "obras/detalhe";
    }

    @PostMapping("/criticas/{id}/excluir")
    public String excluir(@PathVariable Long id, @AuthenticationPrincipal UsuarioLogado usuario,
                          RedirectAttributes redirect) {
        Long obraId = criticaService.excluir(id, usuario);
        redirect.addFlashAttribute("sucesso", "Crítica excluída.");
        return "redirect:/obras/" + obraId + "#criticas";
    }
}
