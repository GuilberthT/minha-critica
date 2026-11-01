package br.ufms.minhacritica.controller;

import br.ufms.minhacritica.model.Obra;
import br.ufms.minhacritica.model.TipoObra;
import br.ufms.minhacritica.service.ObraService;
import br.ufms.minhacritica.service.RegraNegocioException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CRUD completo de obras. Acesso restrito a ROLE_ADMIN (ver SecurityConfig: /admin/**).
 */
@Controller
@RequestMapping("/admin/obras")
public class AdminObraController {

    private final ObraService obraService;

    public AdminObraController(ObraService obraService) {
        this.obraService = obraService;
    }

    /** Impede que o formulário altere campos internos (proteção contra mass assignment). */
    @InitBinder("obra")
    public void configurarBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "criticas", "criadoEm");
    }

    @ModelAttribute("tipos")
    public TipoObra[] tipos() {
        return TipoObra.values();
    }

    // READ (listagem)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("obras", obraService.listar(null, null));
        return "admin/obras/lista";
    }

    // CREATE
    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("obra", new Obra());
        return "admin/obras/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("obra") Obra obra, BindingResult resultado,
                        RedirectAttributes redirect) {
        if (!resultado.hasErrors()) {
            try {
                Obra salva = obraService.criar(obra);
                redirect.addFlashAttribute("sucesso", "Obra \"" + salva.getTitulo() + "\" cadastrada.");
                return "redirect:/admin/obras";
            } catch (RegraNegocioException e) {
                resultado.reject("obra", e.getMessage());
            }
        }
        return "admin/obras/form";
    }

    // UPDATE
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("obra", obraService.buscar(id));
        return "admin/obras/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("obra") Obra obra,
                            BindingResult resultado, RedirectAttributes redirect) {
        if (!resultado.hasErrors()) {
            try {
                Obra atualizada = obraService.atualizar(id, obra);
                redirect.addFlashAttribute("sucesso", "Obra \"" + atualizada.getTitulo() + "\" atualizada.");
                return "redirect:/admin/obras";
            } catch (RegraNegocioException e) {
                resultado.reject("obra", e.getMessage());
            }
        }
        obra.setId(id);
        return "admin/obras/form";
    }

    // DELETE (com página de confirmação)
    @GetMapping("/{id}/excluir")
    public String confirmarExclusao(@PathVariable Long id, Model model) {
        model.addAttribute("obra", obraService.buscar(id));
        return "admin/obras/excluir";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        String titulo = obraService.buscar(id).getTitulo();
        obraService.excluir(id);
        redirect.addFlashAttribute("sucesso", "Obra \"" + titulo + "\" excluída.");
        return "redirect:/admin/obras";
    }
}
