package br.ufms.minhacritica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * Configuração do Spring Security.
 *
 * Visitante  -> página inicial, catálogo, login e cadastro.
 * ROLE_USER  -> detalhes das obras, leitura e publicação de críticas.
 * ROLE_ADMIN -> tudo do usuário + CRUD completo de obras (/admin/**) e moderação de críticas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Após o login, só volta para páginas protegidas de verdade (detalhe de obra ou painel admin).
        // Evita mandar o usuário para endereços aleatórios que o navegador pediu antes (ícones, 404 etc.).
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setRequestMatcher(request -> {
            String caminho = request.getRequestURI().substring(request.getContextPath().length());
            return "GET".equals(request.getMethod())
                    && (caminho.matches("/obras/\\d+") || caminho.startsWith("/admin/"));
        });

        http
            .requestCache(cache -> cache.requestCache(requestCache))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/obras", "/login", "/cadastro", "/acesso-negado", "/error",
                        "/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("senha")
                .defaultSuccessUrl("/obras", false)
                .failureUrl("/login?erro")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?saiu")
                .permitAll())
            .exceptionHandling(ex -> ex.accessDeniedPage("/acesso-negado"));
        // CSRF fica habilitado (padrão): o Thymeleaf injeta o token em todo th:action.
        return http.build();
    }
}
