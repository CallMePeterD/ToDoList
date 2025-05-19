package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.model.User;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listTasks(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Task> tasks = taskRepository.findByUserUsername(user.getUsername());
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }


    @GetMapping("/new")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/form";
    }


    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        task.setUser(user); // Associa a tarefa ao usuário
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setCompleted(true);
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/stats")
    public String showStats(Model model) {
        // Dados para os gráficos (exemplo)
        Map<String, Integer> taskStats = new HashMap<>();
        taskStats.put("Concluídas", taskRepository.countByCompleted(true));
        taskStats.put("Pendentes", taskRepository.countByCompleted(false));

        model.addAttribute("taskStats", taskStats);
        return "tasks/stats";
    }



}

