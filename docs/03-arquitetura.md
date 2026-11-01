# Arquitetura e modelo de dados

## Arquitetura: Spring MVC + Thymeleaf (camadas)

```
Navegador ──HTTP──▶ Spring Security (filtros: login, perfis, CSRF)
                         │
                         ▼
                   Controller  ── recebe a requisição, valida o formulário (@Valid), escolhe a view
                         │
                         ▼
                    Service    ── regras de negócio (RN01–RN09), transações (@Transactional)
                         │
                         ▼
                   Repository  ── Spring Data JPA (consultas geradas pelo nome do método)
                         │
                         ▼
                 Banco relacional (H2 em arquivo / PostgreSQL)

   Controller ──Model──▶ View Thymeleaf (templates/*.html) ──HTML──▶ Navegador
```

| Pacote | Responsabilidade |
|---|---|
| `model` | Entidades JPA mapeadas para tabelas |
| `repository` | Acesso a dados (interfaces `JpaRepository`) |
| `service` | Regras de negócio e transações |
| `controller` | Rotas HTTP, validação de formulários e escolha das views |
| `dto` | Objetos de formulário que não são entidades |
| `config` | Segurança e carga inicial de dados |
| `templates` / `static` | Views Thymeleaf, CSS e JS |

## Modelo de dados

```
usuarios                    obras                         criticas
─────────────               ─────────────                 ─────────────
id (PK)                     id (PK)                       id (PK)
nome                        titulo                        nota (1–5)
email (único)               tipo (FILME/SERIE/LIVRO)      comentario
senha (BCrypt)              genero                        data_criacao
perfil (ROLE_USER/ADMIN)    ano_lancamento                autor_id (FK → usuarios)
criado_em                   autor_ou_diretor              obra_id  (FK → obras)
                            sinopse                       UNIQUE (autor_id, obra_id)
                            capa_url (opcional)
                            criado_em
```

- **Usuario 1 — N Critica** (um usuário escreve várias críticas)
- **Obra 1 — N Critica** (uma obra recebe várias críticas; exclusão em cascata)

As tabelas são criadas automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

## Rotas

| Método | Rota | Acesso | Função |
|---|---|---|---|
| GET | `/` | Todos | Página inicial |
| GET | `/obras?q=&tipo=` | Todos | Catálogo com busca e filtro |
| GET/POST | `/cadastro` | Todos | Criar conta |
| GET/POST | `/login`, POST `/logout` | Todos | Autenticação |
| GET | `/obras/{id}` | USER, ADMIN | Detalhes e críticas |
| POST | `/obras/{id}/criticas` | USER, ADMIN | Publicar crítica |
| POST | `/criticas/{id}/excluir` | Autor ou ADMIN | Excluir crítica |
| GET | `/admin/obras` | ADMIN | Listagem (Read) |
| GET/POST | `/admin/obras/nova`, `/admin/obras` | ADMIN | Cadastro (Create) |
| GET/POST | `/admin/obras/{id}/editar`, `/admin/obras/{id}` | ADMIN | Edição (Update) |
| GET/POST | `/admin/obras/{id}/excluir` | ADMIN | Confirmação e exclusão (Delete) |

## Diferenciais implementados

- Interface em tema escuro centrada nas capas: página inicial com a obra mais bem avaliada em destaque, catálogo em grade de pôsteres, gráfico de distribuição das notas e estrelas clicáveis.
- Capa por URL opcional; sem ela, o site desenha uma capa própria com a cor do tipo de obra.
- Busca por título e filtro por tipo no catálogo.
- Média de notas e contagem de críticas por obra.
- Moderação de críticas pelo administrador.
- Página de confirmação antes de excluir, avisando quantas críticas serão apagadas.
- Dois bancos relacionais suportados (H2 e PostgreSQL) por perfis do Spring.
- Testes automatizados das regras de acesso com MockMvc.
- Páginas de erro personalizadas (403, 404 e genérica).
