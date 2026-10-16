package br.com.aweb.sistema_vendas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracoesDeSeguranca {

    // Os usuários agora vêm do banco de dados (UsuarioService implements UserDetailsService),
    // por isso o @Bean UserDetailsService em memória foi removido.

    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(req -> {
                // Recursos estáticos, login e página de erro: livres
                req.requestMatchers("/css/**", "/js/**", "/img/**", "/login", "/error").permitAll();

                // Regras da mais específica para a mais genérica
                // PADRAO e ADMIN: operações de pedidos
                req.requestMatchers("/pedidos/**").hasAnyRole("ADMIN", "PADRAO");

                // Somente ADMIN: cadastro de produtos e clientes
                req.requestMatchers("/produtos/**").hasRole("ADMIN");
                req.requestMatchers("/clientes/**").hasRole("ADMIN");

                // Demais rotas (home, logout...): qualquer usuário autenticado
                req.anyRequest().authenticated();
            })
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll())
            .rememberMe(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll())
            .build();
    }

    // Todas as senhas são gravadas com hash BCrypt
    @Bean
    public PasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }
}
