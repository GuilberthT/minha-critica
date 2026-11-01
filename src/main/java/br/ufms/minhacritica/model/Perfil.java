package br.ufms.minhacritica.model;

/**
 * Perfis de acesso usados pelo Spring Security.
 * O nome do enum já segue o padrão "ROLE_" exigido pelo hasRole().
 */
public enum Perfil {
    ROLE_USER("Usuário"),
    ROLE_ADMIN("Administrador");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
