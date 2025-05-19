package com.example.todolist.config;

import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se já existe algum usuário
            if (userRepository.count() == 0) {
                // Cria usuário admin padrão
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123"));
                admin.setEmail("admin@example.com");

                userRepository.save(admin);
                System.out.println("Usuário admin criado com sucesso!");
            }
        };
    }
}