package br.ufms.minhacritica.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados do formulário de nova crítica. */
public class CriticaForm {

    @NotNull(message = "Escolha uma nota de 1 a 5.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    private Integer nota;

    @NotBlank(message = "Escreva seu comentário.")
    @Size(min = 10, max = 1000, message = "O comentário deve ter entre 10 e 1000 caracteres.")
    private String comentario;

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
