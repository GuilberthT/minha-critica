package br.ufms.minhacritica.model;

/** Tipos de obra que podem ser criticadas na plataforma. */
public enum TipoObra {
    FILME("Filme"),
    SERIE("Série"),
    LIVRO("Livro");

    private final String descricao;

    TipoObra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
