// src/main/java/com/example/todolist/controller/LoginController.java
package com.example.todolist.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Usuário ou senha inválidos!");
        }

        if (logout != null) {
            model.addAttribute("message", "Logout realizado com sucesso!");
        }

        return "auth/login";
    }

    @GetMapping("/test-login")
    @ResponseBody
    public String testLogin(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "Usuário autenticado: " + authentication.getName();
        }
        return "Não autenticado";
    }
}