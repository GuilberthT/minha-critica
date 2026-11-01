package br.ufms.minhacritica.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Crítica escrita por um usuário sobre uma obra.
 * RN03: cada usuário pode publicar apenas UMA crítica por obra (restrição única).
 */
@Entity
@Table(name = "criticas",
        uniqueConstraints = @UniqueConstraint(name = "uk_critica_autor_obra", columnNames = {"autor_id", "obra_id"}))
public class Critica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nota de 1 a 5 estrelas. */
    @Column(nullable = false)
    private Integer nota;

    @Column(nullable = false, length = 1000)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    public Critica() {
    }

    public Critica(Integer nota, String comentario, Usuario autor, Obra obra) {
        this.nota = nota;
        this.comentario = comentario;
        this.autor = autor;
        this.obra = obra;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }
    public Obra getObra() { return obra; }
    public void setObra(Obra obra) { this.obra = obra; }
}
