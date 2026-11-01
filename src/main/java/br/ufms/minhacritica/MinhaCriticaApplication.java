package br.ufms.minhacritica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação "Minha Crítica".
 * Plataforma web para catalogar filmes, séries e livros e publicar críticas.
 */
@SpringBootApplication
public class MinhaCriticaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinhaCriticaApplication.class, args);
    }
}
