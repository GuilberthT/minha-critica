package br.ufms.minhacritica.controller;

import br.ufms.minhacritica.service.ObraService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final ObraService obraService;

    public HomeController(ObraService obraService) {
        this.obraService = obraService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("recentes", obraService.recentes());
        model.addAttribute("totalObras", obraService.contar());
        return "index";
    }

    /** Aceita qualquer método: o Spring Security encaminha para cá também a partir de POSTs negados. */
    @RequestMapping("/acesso-negado")
    public String acessoNegado(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "erro/acesso-negado";
    }
}
