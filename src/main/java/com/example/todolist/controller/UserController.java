// src/main/java/com/example/todolist/controller/UserController.java
package com.example.todolist.controller;

import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/profile")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)  // Removido o UserRepository.
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/update")
    public String updateProfile(
            Authentication authentication,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String newPassword
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza os dados básicos
        user.setUsername(name); // Ou adicione um campo 'name' separado na entidade User
        user.setEmail(email);

        // Atualiza a senha (se fornecida)
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
        return "redirect:/profile?success";
    }


}