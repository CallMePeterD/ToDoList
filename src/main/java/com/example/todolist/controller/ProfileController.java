package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks/profile")
    public String showProfile(Model model, Authentication authentication) {
        String username = authentication.getName();

        // Busca as tarefas do usuário logado
        List<Task> userTasks = taskRepository.findByUserUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("tasks", userTasks);

        return "tasks/profile";
    }
}