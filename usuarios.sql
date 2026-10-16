-- Execute no pgAdmin4 (Query Tool) conectado ao banco sistema_vendas.
-- A tabela "usuarios" é criada pelo Hibernate na primeira execução da aplicação (ddl-auto=update).
-- As senhas abaixo já estão com hash BCrypt:
--   admin@email.com  -> Admin@123   (ADMIN)
--   padrao@email.com -> Padrao@123  (PADRAO)

INSERT INTO usuarios (nome, email, senha, role) VALUES
 ('Administrador', 'admin@email.com',  '$2a$10$kYwGRGRny.jvj6HXsoBNruuGvVJ.x/hpTCTvUq8kJ7xksoKeAtCEy', 'ADMIN'),
 ('Usuário Padrão', 'padrao@email.com', '$2a$10$35lM2lorzOROoYAh3L7pMumCD4wDEpyaAy26hIOrLOejLDqj2Dn6S', 'PADRAO');

SELECT id, nome, email, role FROM usuarios;
