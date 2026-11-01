# Regras de negócio — Minha Crítica

## Tema

Plataforma de críticas de **filmes, séries e livros**. O administrador mantém o catálogo de obras; os usuários
cadastrados leem as críticas e publicam as suas, com nota de 1 a 5 estrelas. Cada obra exibe a média das notas.

## Perfis de acesso

| Perfil | Origem | Permissões |
|---|---|---|
| Visitante (não autenticado) | — | Página inicial, catálogo (lista com busca e filtro), cadastro e login |
| `ROLE_USER` | Cadastro público | Visualização completa (detalhes e críticas) + publicar e excluir a própria crítica |
| `ROLE_ADMIN` | Criado pelo sistema | Tudo do usuário + CRUD completo de obras em `/admin/obras` + excluir qualquer crítica |

## Regras

| Código | Regra | Onde está implementada |
|---|---|---|
| RN01 | Visitantes veem o catálogo, mas detalhes e críticas exigem login. | `SecurityConfig` |
| RN02 | Todo cadastro público recebe o perfil `ROLE_USER`. Não é possível se cadastrar como administrador. | `UsuarioService.cadastrar` |
| RN03 | Cada usuário publica **no máximo uma crítica por obra**, com nota inteira de 1 a 5 e comentário de 10 a 1000 caracteres. | `CriticaService.publicar`, `CriticaForm`, restrição única `uk_critica_autor_obra` |
| RN04 | O autor pode excluir a própria crítica; o administrador pode excluir qualquer crítica (moderação). | `CriticaService.excluir` |
| RN05 | Só o administrador cadastra, edita e exclui obras. Excluir uma obra apaga também as críticas dela, após confirmação. | `/admin/**` + `cascade` em `Obra.criticas` + tela `excluir.html` |
| RN06 | A média de notas de uma obra é calculada a partir das críticas; sem críticas, exibe “–”. | `Obra.getMediaNotas` |
| RN07 | Não pode haver duas obras com o mesmo título, tipo e ano. | `ObraService.criar/atualizar` |
| RN08 | O e-mail é único; a senha tem no mínimo 6 caracteres, precisa ser confirmada e é salva com hash BCrypt. | `UsuarioService`, `CadastroForm` |
| RN09 | Obra exige título (até 150), tipo (Filme, Série ou Livro), gênero, ano entre 1800 e 2100 e sinopse de 20 a 2000 caracteres. A capa é opcional e, se informada, precisa ser um endereço http(s). | Anotações de validação em `Obra` |
