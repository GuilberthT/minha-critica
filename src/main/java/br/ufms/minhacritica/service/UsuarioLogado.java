package br.ufms.minhacritica.service;

import br.ufms.minhacritica.model.Perfil;
import br.ufms.minhacritica.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Representa o usuário autenticado na sessão (principal do Spring Security).
 * Guarda id e nome para uso nos controllers e nas views.
 */
public class UsuarioLogado implements UserDetails {

    private final Long id;
    private final String nome;
    private final String email;
    private final String senha;
    private final Perfil perfil;

    public UsuarioLogado(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.perfil = usuario.getPerfil();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Perfil getPerfil() { return perfil; }

    public boolean isAdmin() {
        return perfil == Perfil.ROLE_ADMIN;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(perfil.name()));
    }

    @Override
    public String getPassword() { return senha; }

    @Override
    public String getUsername() { return email; }
}
