# Segurança e acessibilidade

## Spring Security

Configuração em `config/SecurityConfig.java`:

- **Autenticação por formulário** (`/login`), usando o e-mail como nome de usuário.
  O `UsuarioService` implementa `UserDetailsService` e carrega o usuário do banco.
- **Senhas com BCrypt** (`BCryptPasswordEncoder`): nenhuma senha fica em texto puro.
- **Autorização por perfil**:
  - rotas públicas: `/`, `/obras`, `/login`, `/cadastro`, arquivos estáticos;
  - `/admin/**` → somente `ROLE_ADMIN`;
  - qualquer outra rota → usuário autenticado.
- **Regra no nível do serviço**: excluir crítica só é permitido ao autor ou ao admin (`AccessDeniedException`).
- **CSRF habilitado**: o Thymeleaf injeta o token automaticamente em todo formulário com `th:action`.
  Por isso as ações que alteram dados (excluir, sair) usam `POST`, nunca links `GET`.
- **Proteção contra mass assignment**: `@InitBinder` impede que o formulário altere `id`, `criticas` e `criadoEm`.
- **Página de acesso negado** (`/acesso-negado`, HTTP 403) quando um usuário comum tenta acessar o painel.
- Na view, o menu muda conforme o perfil com `sec:authorize` (Thymeleaf Extras Spring Security).

## Acessibilidade (WCAG 2.1 nível AA)

| Critério | O que foi feito |
|---|---|
| 1.1.1 Conteúdo não textual | Estrelas decorativas com `aria-hidden`; a nota é anunciada como “Nota 4 de 5” (`role="img"` + `aria-label`). |
| 1.3.1 Informação e relações | Tags semânticas (`header`, `nav`, `main`, `article`, `section`, `footer`), `label` ligado a cada campo, `fieldset`/`legend` na nota, tabela com `caption` e `th scope`. |
| 1.4.3 Contraste mínimo | Todas as combinações de texto têm contraste ≥ 4.5:1 (ex.: texto 15.5:1, botão primário 10:1, texto secundário 7.2:1). Valores no topo do `style.css`. |
| 1.4.11 Contraste não textual | Bordas de campos com 3.6:1; foco visível com contorno azul de 3px. |
| 2.1.1 Teclado | Tudo funciona por teclado; a nota usa botões de rádio nativos. |
| 2.4.1 Pular blocos | Link “Pular para o conteúdo principal” no topo de todas as páginas. |
| 2.4.2 Título da página | Cada página tem `<title>` próprio (“Catálogo | Minha Crítica”). |
| 2.4.7 Foco visível | Estilo `:focus-visible` em todos os elementos interativos. |
| 2.5.5 Tamanho do alvo | Botões com altura mínima de 44px. |
| 3.1.1 Idioma | `<html lang="pt-BR">`. |
| 3.3.1 / 3.3.3 Erros | Mensagens de erro em texto, ligadas ao campo com `aria-describedby` e `aria-invalid`; o foco vai para o primeiro campo com erro. |
| 4.1.3 Mensagens de status | Avisos de sucesso com `role="status"`/`aria-live="polite"`; erros com `role="alert"`; contador de caracteres anunciado. |
| Movimento | `prefers-reduced-motion` respeitado. |
