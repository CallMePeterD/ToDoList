// src/main/java/com/example/todolist/controller/RegistrationController.java
package com.example.todolist.controller;

import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;
import com.example.todolist.repository.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/auth")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        // Validação simples
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Usuário já existe");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        // Codifica a senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salva e exibe log
        User savedUser = userRepository.save(user);
        System.out.println("Usuário salvo com ID: " + savedUser.getId());

        redirectAttributes.addFlashAttribute("success", "Registro realizado com sucesso!");
        return "redirect:/login";
    }
}