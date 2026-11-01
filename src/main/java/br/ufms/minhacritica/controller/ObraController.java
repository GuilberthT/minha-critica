package br.ufms.minhacritica.controller;

import br.ufms.minhacritica.dto.CriticaForm;
import br.ufms.minhacritica.model.Obra;
import br.ufms.minhacritica.model.TipoObra;
import br.ufms.minhacritica.service.CriticaService;
import br.ufms.minhacritica.service.ObraService;
import br.ufms.minhacritica.service.UsuarioLogado;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/** Páginas de leitura: catálogo (público) e detalhes da obra (usuário logado). */
@Controller
public class ObraController {

    private final ObraService obraService;
    private final CriticaService criticaService;

    public ObraController(ObraService obraService, CriticaService criticaService) {
        this.obraService = obraService;
        this.criticaService = criticaService;
    }

    @GetMapping("/obras")
    public String catalogo(@RequestParam(required = false) String q,
                           @RequestParam(required = false) TipoObra tipo,
                           Model model) {
        model.addAttribute("obras", obraService.listar(q, tipo));
        model.addAttribute("q", q);
        model.addAttribute("tipoSelecionado", tipo);
        model.addAttribute("tipos", TipoObra.values());
        return "obras/lista";
    }

    @GetMapping("/obras/{id}")
    public String detalhe(@PathVariable Long id, @AuthenticationPrincipal UsuarioLogado usuario, Model model) {
        Obra obra = obraService.buscar(id);
        model.addAttribute("obra", obra);
        model.addAttribute("jaCriticou", criticaService.jaCriticou(usuario.getId(), id));
        if (!model.containsAttribute("criticaForm")) {
            model.addAttribute("criticaForm", new CriticaForm());
        }
        return "obras/detalhe";
    }
}
