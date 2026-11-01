package br.ufms.minhacritica.config;

import br.ufms.minhacritica.model.*;
import br.ufms.minhacritica.repository.CriticaRepository;
import br.ufms.minhacritica.repository.ObraRepository;
import br.ufms.minhacritica.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Popula o banco na primeira execução com usuários de teste, obras e críticas,
 * para que a aplicação já possa ser demonstrada.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;
    private final CriticaRepository criticaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, ObraRepository obraRepository,
                           CriticaRepository criticaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.obraRepository = obraRepository;
        this.criticaRepository = criticaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        log.info("Banco vazio: criando dados iniciais de demonstração.");

        usuarioRepository.save(new Usuario("Administrador", "admin@minhacritica.com",
                passwordEncoder.encode("admin123"), Perfil.ROLE_ADMIN));
        Usuario ana = usuarioRepository.save(new Usuario("Ana Souza", "ana@minhacritica.com",
                passwordEncoder.encode("user123"), Perfil.ROLE_USER));
        Usuario bruno = usuarioRepository.save(new Usuario("Bruno Lima", "bruno@minhacritica.com",
                passwordEncoder.encode("user123"), Perfil.ROLE_USER));

        List<Obra> obras = obraRepository.saveAll(List.of(
                new Obra("Cidade de Deus", TipoObra.FILME, "Drama", 2002, "Fernando Meirelles",
                        "Retrato de décadas de crescimento do crime organizado em uma comunidade do Rio de Janeiro, "
                        + "contado pelo olhar de um jovem que sonha em ser fotógrafo."),
                new Obra("Central do Brasil", TipoObra.FILME, "Drama", 1998, "Walter Salles",
                        "Uma ex-professora que escreve cartas para analfabetos na estação Central do Brasil "
                        + "acaba atravessando o país com um menino em busca do pai dele."),
                new Obra("O Auto da Compadecida", TipoObra.FILME, "Comédia", 2000, "Guel Arraes",
                        "As aventuras de João Grilo e Chicó no sertão nordestino, cheias de mentiras, "
                        + "golpes e um julgamento final inesquecível."),
                new Obra("Sintonia", TipoObra.SERIE, "Drama", 2019, "KondZilla",
                        "Três amigos que cresceram juntos em uma comunidade de São Paulo seguem caminhos "
                        + "diferentes entre a música, a religião e o crime."),
                new Obra("Cangaço Novo", TipoObra.SERIE, "Ação", 2023, "Aly Muritiba e Fábio Mendonça",
                        "Um bancário de São Paulo descobre suas origens no sertão e se envolve com um grupo "
                        + "de assaltantes que reacende a lenda do cangaço."),
                new Obra("Dom Casmurro", TipoObra.LIVRO, "Romance", 1899, "Machado de Assis",
                        "Bento Santiago relembra sua vida e o casamento com Capitu, alimentando a dúvida "
                        + "mais famosa da literatura brasileira."),
                new Obra("Vidas Secas", TipoObra.LIVRO, "Romance", 1938, "Graciliano Ramos",
                        "Uma família de retirantes atravessa o sertão fugindo da seca, em uma narrativa "
                        + "seca e poderosa sobre miséria e dignidade."),
                new Obra("Torto Arado", TipoObra.LIVRO, "Romance", 2019, "Itamar Vieira Junior",
                        "Duas irmãs do sertão baiano têm a vida marcada por um acidente na infância e pela "
                        + "luta por terra e liberdade de sua comunidade.")
        ));

        criticaRepository.saveAll(List.of(
                new Critica(5, "Uma obra-prima do cinema nacional. Montagem e fotografia impecáveis.", ana, obras.get(0)),
                new Critica(4, "Muito forte e bem atuado, mas algumas cenas são difíceis de assistir.", bruno, obras.get(0)),
                new Critica(5, "Emocionante do começo ao fim. Fernanda Montenegro está incrível.", ana, obras.get(1)),
                new Critica(5, "Rio do começo ao fim, um clássico que nunca cansa.", bruno, obras.get(2)),
                new Critica(4, "A dúvida sobre Capitu continua genial mais de um século depois.", ana, obras.get(5)),
                new Critica(3, "Leitura importante, mas o ritmo lento pode afastar alguns leitores.", bruno, obras.get(6)),
                new Critica(5, "Um dos melhores livros brasileiros recentes. Narrativa belíssima.", bruno, obras.get(7))
        ));

        log.info("Dados iniciais criados: admin@minhacritica.com / admin123 e ana@minhacritica.com / user123");
    }
}
