package br.ufms.minhacritica.controller;

import br.ufms.minhacritica.model.Obra;
import br.ufms.minhacritica.service.CriticaService;
import br.ufms.minhacritica.service.ObraService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ObraService obraService;
    private final CriticaService criticaService;

    public HomeController(ObraService obraService, CriticaService criticaService) {
        this.obraService = obraService;
        this.criticaService = criticaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        List<Obra> melhores = obraService.maisBemAvaliadas(7);
        // A obra em destaque é a mais bem avaliada; as demais formam a faixa logo abaixo.
        model.addAttribute("destaque", melhores.isEmpty() ? null : melhores.get(0));
        model.addAttribute("melhores", melhores.size() > 1 ? melhores.subList(1, melhores.size()) : List.of());
        model.addAttribute("recentes", obraService.recentes());
        model.addAttribute("totalObras", obraService.contar());
        // Críticas só aparecem para quem está logado (RN01); a view decide pelo perfil.
        model.addAttribute("criticasRecentes", criticaService.recentes());
        return "index";
    }

    /** Aceita qualquer método: o Spring Security encaminha para cá também a partir de POSTs negados. */
    @RequestMapping("/acesso-negado")
    public String acessoNegado(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "erro/acesso-negado";
    }
}
