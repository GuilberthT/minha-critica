package br.ufms.minhacritica.service;

import br.ufms.minhacritica.dto.CadastroForm;
import br.ufms.minhacritica.model.Perfil;
import br.ufms.minhacritica.model.Usuario;
import br.ufms.minhacritica.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Usado pelo Spring Security no login (o "username" é o e-mail). */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(email.trim())
                .map(UsuarioLogado::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    /**
     * RN02: todo cadastro público recebe o perfil ROLE_USER.
     * RN08: e-mail único e senha confirmada, guardada com BCrypt.
     */
    @Transactional
    public Usuario cadastrar(CadastroForm form) {
        String email = form.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new RegraNegocioException("Já existe uma conta com este e-mail.");
        }
        if (!form.getSenha().equals(form.getConfirmacaoSenha())) {
            throw new RegraNegocioException("As senhas não conferem.");
        }
        Usuario usuario = new Usuario(form.getNome().trim(), email,
                passwordEncoder.encode(form.getSenha()), Perfil.ROLE_USER);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
    }
}
