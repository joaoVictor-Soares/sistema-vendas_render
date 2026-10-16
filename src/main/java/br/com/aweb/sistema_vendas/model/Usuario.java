package br.com.aweb.sistema_vendas.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.aweb.sistema_vendas.enums.UsuarioRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "usuarios")
public class Usuario implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UsuarioRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna a role do usuário convertida para GrantedAuthority do Spring Security.
        // O Spring Security geralmente espera o prefixo "ROLE_", por isso usamos "ROLE_" + role.name()
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna o campo de senha criptografada (hash)
    }

    @Override
    public String getUsername() {
        return this.email; // No seu projeto, o identificador de login é o e-mail
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Retorne true se a conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Retorne true se a conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Retorne true se as credenciais (senha) não expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // Retorne true se o usuário estiver ativo
    }

    // --- Getters e Setters ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void setSenha(String senha) { this.senha = senha; }

    public UsuarioRole getRole() { return role; }
    public void setRole(UsuarioRole role) { this.role = role; }
}
