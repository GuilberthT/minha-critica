package br.ufms.minhacritica.controller;

import br.ufms.minhacritica.dto.CadastroForm;
import br.ufms.minhacritica.service.RegraNegocioException;
import br.ufms.minhacritica.service.UsuarioLogado;
import br.ufms.minhacritica.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Login (processado pelo Spring Security) e cadastro público de usuários. */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UsuarioLogado usuario) {
        return usuario != null ? "redirect:/obras" : "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("form", new CadastroForm());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Valid @ModelAttribute("form") CadastroForm form, BindingResult resultado,
                            RedirectAttributes redirect) {
        if (!resultado.hasErrors()) {
            try {
                usuarioService.cadastrar(form);
                redirect.addFlashAttribute("sucesso", "Conta criada com sucesso! Entre com seu e-mail e senha.");
                return "redirect:/login";
            } catch (RegraNegocioException e) {
                resultado.reject("cadastro", e.getMessage());
            }
        }
        return "cadastro";
    }
}
