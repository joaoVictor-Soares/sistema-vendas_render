package br.com.aweb.sistema_vendas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // O POST /login é tratado automaticamente pelo Spring Security
    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    // Página de confirmação de logout (o formulário POST /logout fica nela)
    @GetMapping("/logout")
    public String exibirLogout() {
        return "logout";
    }
}
