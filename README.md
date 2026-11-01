# Minha Crítica

Plataforma web para catalogar **filmes, séries e livros** e publicar **críticas com nota de 1 a 5 estrelas**.
Trabalho prático de Desenvolvimento de Aplicação Web com Spring Boot — UFMS, 2026.

## Integrantes

- Guilberth William Ferreira Tavares
- Pedro Paulo Pereira Lacerda
- Lucas Gonçalves Cavanha

Repositório: _colar aqui o link do GitHub_

## O problema que a aplicação resolve

Quem quer escolher o que assistir ou ler costuma procurar opiniões espalhadas em redes sociais, vídeos e
sites diferentes. O **Minha Crítica** junta em um só lugar um catálogo organizado (curado por um
administrador) e as avaliações dos próprios usuários, com média de notas por obra.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5 (Spring MVC) |
| Views | Thymeleaf + HTML5 semântico, CSS e JavaScript puros |
| Segurança | Spring Security 6 (login por formulário, BCrypt, CSRF) |
| Persistência | Spring Data JPA / Hibernate |
| Banco relacional | **H2 em arquivo** (padrão, sem instalação) ou **PostgreSQL** (perfil `postgres`) |
| Validação | Bean Validation (Jakarta Validation) |
| Testes | JUnit 5 + Spring Security Test (MockMvc) |

Arquitetura escolhida: **Spring MVC com Thymeleaf** (renderização no servidor).

## Como executar

Pré-requisitos: **JDK 17 ou superior** e **Maven** (ou uma IDE como IntelliJ IDEA / VS Code / Eclipse, que já trazem o Maven).

```bash
mvn spring-boot:run
```

Ou, na IDE, abra a pasta do projeto e execute a classe `MinhaCriticaApplication`.

Acesse **http://localhost:8080**.

Na primeira execução o banco é criado na pasta `./data` e populado com dados de exemplo:

| Perfil | E-mail | Senha |
|---|---|---|
| Administrador (ROLE_ADMIN) | `admin@minhacritica.com` | `admin123` |
| Usuário (ROLE_USER) | `ana@minhacritica.com` | `user123` |
| Usuário (ROLE_USER) | `bruno@minhacritica.com` | `user123` |

Para recomeçar do zero, apague a pasta `data/`.

### Usando PostgreSQL

```sql
CREATE DATABASE minhacritica;
```

```bash
# usuário/senha padrão: postgres/postgres (ou defina DB_USER e DB_PASSWORD)
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Testes

```bash
mvn test
```

Os testes verificam as regras de acesso de cada perfil, o CRUD do administrador, a validação de formulários
e o cadastro público.

## Funcionalidades por perfil

| Ator | Pode fazer |
|---|---|
| **Visitante** | Ver página inicial e catálogo, buscar/filtrar obras, criar conta, entrar |
| **Usuário (ROLE_USER)** | Tudo do visitante + ver detalhes e críticas, publicar crítica, excluir a própria crítica |
| **Administrador (ROLE_ADMIN)** | Tudo do usuário + **CRUD completo de obras** (cadastrar, listar, editar, excluir) e moderar (excluir) qualquer crítica |

## Estrutura do projeto

```
src/main/java/br/ufms/minhacritica
├── MinhaCriticaApplication.java   ponto de entrada
├── config/        SecurityConfig (Spring Security) e DataInitializer (dados iniciais)
├── model/         Entidades JPA: Usuario, Obra, Critica + enums Perfil e TipoObra
├── repository/    Interfaces Spring Data JPA
├── service/       Regras de negócio (UsuarioService, ObraService, CriticaService)
├── controller/    Controllers MVC (Home, Auth, Obra, Critica, AdminObra)
└── dto/           Objetos de formulário (CadastroForm, CriticaForm)

src/main/resources
├── templates/     Views Thymeleaf (fragments, obras, admin/obras, erros)
├── static/        css/style.css e js/app.js
└── application*.properties

docs/              Documentação: regras de negócio, casos de uso, arquitetura, segurança e acessibilidade
```

## Documentação

- [Regras de negócio](docs/01-regras-de-negocio.md)
- [Casos de uso](docs/02-casos-de-uso.md) (diagrama em `docs/diagrama-casos-de-uso.svg`)
- [Arquitetura e modelo de dados](docs/03-arquitetura.md)
- [Segurança e acessibilidade](docs/04-seguranca-acessibilidade.md)
- [Divisão de tarefas](docs/05-divisao-de-tarefas.md)
