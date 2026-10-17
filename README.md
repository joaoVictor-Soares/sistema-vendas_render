# Sistema de Vendas

Aplicação web monolítica de vendas (produtos, clientes e pedidos) com autenticação e autorização por perfil. Projeto 1 da disciplina de Aplicações Web — Faculdade de Tecnologia SENAI Antonio Adolpho Lobbe (Análise e Desenvolvimento de Sistemas).

**Aplicação publicada:** https://sistema-vendas-render-1.onrender.com

> No plano gratuito do Render a aplicação "dorme" após um período sem acesso. A primeira requisição pode levar cerca de um minuto.

## Tecnologias

- Java 21 e Spring Boot 4.1
- Spring Web, Spring Data JPA (Hibernate), Bean Validation
- Spring Security 7 + Thymeleaf Extras (namespace `sec`)
- Thymeleaf e Bootstrap
- PostgreSQL 15
- Docker e Docker Compose
- Deploy no Render (Web Service + PostgreSQL)

## Funcionalidades

- **CRUD de Produtos** e **CRUD de Clientes** (validação de CPF, e-mail, etc.)
- **Pedidos:** criar pedido para um cliente, adicionar e remover itens, baixa e devolução automática de estoque, cálculo do valor total, cancelamento. Controle de concorrência otimista (`@Version`).
- **Tratamento de erros:** pedir mais do que o estoque disponível (ou quantidade inválida) não gera tela de erro; o usuário volta à edição do pedido com uma mensagem explicativa.
- **Segurança:**
  - Login e logout personalizados (`/login` e `/logout`)
  - Usuários no banco, senhas com hash BCrypt
  - Autorização por URL (`requestMatchers`) e visual (`sec:authorize`)
  - Opção "Lembrar de mim" (remember-me) e proteção CSRF
- **Home** após o login, com painéis conforme o perfil.

## Perfis de acesso

| Recurso | ADMIN | PADRAO |
|---|:---:|:---:|
| Home | todos os painéis | somente Pedidos |
| Produtos (`/produtos/**`) | ✅ | ❌ (403) |
| Clientes (`/clientes/**`) | ✅ | ❌ (403) |
| Pedidos (`/pedidos/**`) | ✅ | ✅ |

### Usuários de teste
Cadastrados automaticamente na inicialização (classe `CriarUsuariosIniciais`, somente se o e-mail ainda não existir):

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | admin@email.com | Admin@123 |
| PADRAO | padrao@email.com | Padrao@123 |

> São credenciais de teste da avaliação. Em um sistema real, não devem ficar no código. Veja `DECISAO_USUARIOS_INICIAIS.txt`.

## Executando localmente

### Pré-requisitos
- JDK 21
- Docker Desktop (para o PostgreSQL)

### Passo a passo
1. Suba o banco:
   ```bash
   docker compose up -d
   ```
2. Rode a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   No Windows: `mvnw.cmd spring-boot:run`
3. Acesse http://localhost:8080 e faça login com um dos usuários de teste.

Sem variáveis de ambiente, a aplicação usa os valores padrão do `docker-compose.yml`:

| Variável | Padrão |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/sistema_vendas` |
| `DB_USER` | `usuario` |
| `DB_PASSWORD` | `senha123` |
| `PORT` | `8080` |

As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`).

### Banco no pgAdmin4
O passo a passo para conectar no PostgreSQL pelo pgAdmin4 (local e Render) está em [`PASSO_A_PASSO_PGADMIN.md`](PASSO_A_PASSO_PGADMIN.md). O script [`usuarios.sql`](usuarios.sql) cadastra os usuários de teste manualmente, caso prefira esse caminho.

## Deploy no Render

1. Crie um **PostgreSQL** no Render e um **Web Service** (runtime Docker) **na mesma região**.
2. No Web Service, defina as variáveis de ambiente:

   | Variável | Valor |
   |---|---|
   | `DB_URL` | `jdbc:postgresql://<hostname-interno>:5432/<database>` |
   | `DB_USER` | usuário do banco |
   | `DB_PASSWORD` | senha do banco |

   O `DB_URL` deve começar com `jdbc:postgresql://`. Não use a *Internal Database URL* sem esse prefixo.
3. O `Dockerfile` faz o build em duas etapas (Maven + JRE 21) e o Render injeta a variável `PORT`, que a aplicação já lê.
4. A cada push na branch configurada o Render faz um novo deploy.

## Estrutura do projeto

```
src/main/java/br/com/aweb/sistema_vendas
├── controller   Login, Home, Produto, Cliente, Pedido
├── service      Regras de negócio e UsuarioService (UserDetailsService)
├── repository   Interfaces Spring Data JPA
├── model        Entidades: Usuario, Produto, Cliente, Pedido, ItemPedido
├── enums        UsuarioRole (ADMIN, PADRAO)
└── security     ConfiguracoesDeSeguranca e CriarUsuariosIniciais

src/main/resources
├── templates    home, login, logout, fragments/navbar, produto/, cliente/, pedido/
└── static       css e js
```

### Modelo de dados
`clientes` 1:N `pedidos` 1:N `itens_pedido` N:1 `produtos`, mais a tabela independente `usuarios`. O diagrama em formato dbdiagram.io está em `estruturaBanco.txt`.

## Documentação adicional

- [`PASSO_A_PASSO_PGADMIN.md`](PASSO_A_PASSO_PGADMIN.md): conexão e cadastro de usuários pelo pgAdmin4
- [`DECISAO_USUARIOS_INICIAIS.txt`](DECISAO_USUARIOS_INICIAIS.txt): por que os usuários de teste são criados no código
