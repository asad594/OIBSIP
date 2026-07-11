package com.library.config;

import com.library.model.User;
import com.library.model.Book;
import com.library.repository.UserRepository;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed Users
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setFullName("Library Admin");
            admin.setEmail("admin@library.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setFullName("John Doe");
            user.setEmail("user@library.com");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("USER");
            userRepository.save(user);
        }

        // Seed Books if catalog is empty
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book(null, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", "Fiction", 5, 5));
            bookRepository.save(new Book(null, "To Kill a Mockingbird", "Harper Lee", "9780446310789", "Fiction", 3, 3));
            bookRepository.save(new Book(null, "A Brief History of Time", "Stephen Hawking", "9780553380163", "Science", 2, 2));
            bookRepository.save(new Book(null, "Introduction to Algorithms", "Thomas H. Cormen", "9780262033848", "Technology", 4, 4));
            bookRepository.save(new Book(null, "Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "9780062316097", "History", 6, 6));
        }
    }
}
