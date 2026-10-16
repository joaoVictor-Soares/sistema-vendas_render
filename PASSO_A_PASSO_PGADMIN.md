# Instanciar o banco no pgAdmin4

## 1. Subir o PostgreSQL (docker-compose.yml do projeto)
1. Abra o Docker Desktop e espere iniciar.
2. Na pasta do projeto, no terminal: `docker compose up -d`
3. Confira: `docker ps` deve listar o container `postgres_sistema` (porta 5432).

## 2. Registrar o servidor no pgAdmin4
1. Abra o pgAdmin4. No painel esquerdo: clique com o botão direito em **Servers** → **Register** → **Server...**
2. Aba **General**: Name = `sistema-vendas-local`
3. Aba **Connection**:
   - Host name/address: `localhost`
   - Port: `5432`
   - Maintenance database: `sistema_vendas`
   - Username: `usuario`
   - Password: `senha123` (marque *Save password*)
4. **Save**. O servidor aparece com o banco `sistema_vendas` em Databases
   (já criado pelo `POSTGRES_DB` do compose; não precisa criar outro).

## 3. Criar as tabelas (rodar a aplicação uma vez)
1. Execute a aplicação (`./mvnw spring-boot:run` ou pelo VSCode).
2. No pgAdmin: Servers → sistema-vendas-local → Databases → sistema_vendas → Schemas → public → **Tables**
   → botão direito → **Refresh**. Devem existir: `clientes`, `produtos`, `pedidos`, `itens_pedido` e `usuarios`.
3. Se você já tinha uma tabela `usuarios` com estrutura antiga, apague-a (botão direito → Delete) e rode a aplicação de novo.

## 4. Cadastrar os usuários de teste (senha com hash BCrypt)
1. Clique com o botão direito em **sistema_vendas** → **Query Tool**.
2. Abra o arquivo `usuarios.sql` (ícone de pasta) ou cole o conteúdo dele e execute (F5 / ▶).
3. O `SELECT` final deve mostrar 2 linhas: ADMIN e PADRAO.

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | admin@email.com | Admin@123 |
| PADRAO | padrao@email.com | Padrao@123 |

> Para outras senhas: gere o hash em https://bcrypt-generator.com/ (rounds 10) e troque o valor da coluna `senha`.
> Nunca grave a senha em texto puro: o login só funciona com o hash BCrypt.

## 5. Testar
1. Acesse http://localhost:8080 → será redirecionado para `/login`.
2. ADMIN: a home mostra os painéis Produtos, Clientes e Pedidos.
3. PADRAO: a home mostra só Pedidos; acessar `/produtos` ou `/clientes` pela URL retorna 403.

## 6. Mesmo procedimento no Render (banco de produção)
1. No Render crie o **PostgreSQL** (mesma região do Web Service) e copie *External Database URL* / host, porta, database, user e password.
2. No pgAdmin: Register → Server com esses dados (aba **SSL**: SSL mode = `require`).
3. Rode o `usuarios.sql` depois que o Web Service subir pela primeira vez (para a tabela existir).
4. Variáveis no Web Service: `DB_URL=jdbc:postgresql://<host>:5432/<database>`, `DB_USER`, `DB_PASSWORD`.
