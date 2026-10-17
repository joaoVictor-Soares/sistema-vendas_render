package br.com.aweb.sistema_vendas.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.aweb.sistema_vendas.enums.UsuarioRole;
import br.com.aweb.sistema_vendas.model.Usuario;
import br.com.aweb.sistema_vendas.repository.UsuarioRepository;

@Component
public class CriarUsuariosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CriarUsuariosIniciais(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        criarSeNaoExistir("Administrador", "admin@email.com", "Admin@123", UsuarioRole.ADMIN);
        criarSeNaoExistir("Usuário Padrão", "padrao@email.com", "Padrao@123", UsuarioRole.PADRAO);
    }

    private void criarSeNaoExistir(String nome, String email, String senha, UsuarioRole role) {
        if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
            return;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setRole(role);
        usuarioRepository.save(usuario);
    }
}
