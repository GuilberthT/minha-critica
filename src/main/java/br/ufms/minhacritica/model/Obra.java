package br.ufms.minhacritica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Obra (filme, série ou livro) cadastrada pelo administrador. */
@Entity
@Table(name = "obras")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o título.")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotNull(message = "Selecione o tipo da obra.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoObra tipo;

    @NotBlank(message = "Informe o gênero.")
    @Size(max = 60, message = "O gênero deve ter no máximo 60 caracteres.")
    @Column(nullable = false, length = 60)
    private String genero;

    @NotNull(message = "Informe o ano de lançamento.")
    @Min(value = 1800, message = "O ano deve ser a partir de 1800.")
    @Max(value = 2100, message = "O ano deve ser até 2100.")
    @Column(nullable = false)
    private Integer anoLancamento;

    @Size(max = 100, message = "O campo deve ter no máximo 100 caracteres.")
    @Column(length = 100)
    private String autorOuDiretor;

    @NotBlank(message = "Escreva uma sinopse.")
    @Size(min = 20, max = 2000, message = "A sinopse deve ter entre 20 e 2000 caracteres.")
    @Column(nullable = false, length = 2000)
    private String sinopse;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    /** RN05: ao excluir a obra, as críticas dela também são removidas. */
    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dataCriacao DESC")
    private List<Critica> criticas = new ArrayList<>();

    public Obra() {
    }

    public Obra(String titulo, TipoObra tipo, String genero, Integer anoLancamento,
                String autorOuDiretor, String sinopse) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.autorOuDiretor = autorOuDiretor;
        this.sinopse = sinopse;
    }

    /** RN06: média das notas recebidas (0 quando não há críticas). */
    public double getMediaNotas() {
        return criticas.stream().mapToInt(Critica::getNota).average().orElse(0);
    }

    public int getTotalCriticas() {
        return criticas.size();
    }

    /** "1 crítica" / "3 críticas", com o plural correto. */
    public String getTextoCriticas() {
        int total = getTotalCriticas();
        return total == 1 ? "1 crítica" : total + " críticas";
    }

    public String getMediaFormatada() {
        return criticas.isEmpty() ? "–" : String.format("%.1f", getMediaNotas()).replace('.', ',');
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public TipoObra getTipo() { return tipo; }
    public void setTipo(TipoObra tipo) { this.tipo = tipo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Integer getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(Integer anoLancamento) { this.anoLancamento = anoLancamento; }
    public String getAutorOuDiretor() { return autorOuDiretor; }
    public void setAutorOuDiretor(String autorOuDiretor) { this.autorOuDiretor = autorOuDiretor; }
    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public List<Critica> getCriticas() { return criticas; }
    public void setCriticas(List<Critica> criticas) { this.criticas = criticas; }
}
