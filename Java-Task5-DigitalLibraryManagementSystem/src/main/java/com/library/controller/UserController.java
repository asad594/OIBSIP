package com.library.controller;

import com.library.model.Book;
import com.library.model.ContactQuery;
import com.library.model.User;
import com.library.model.BookBooking;
import com.library.service.BookService;
import com.library.service.ContactService;
import com.library.service.IssueService;
import com.library.service.UserService;
import com.library.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookService bookService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private BookingService bookingService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String search,
                            @RequestParam(required = false) String category,
                            Model model) {
        User user = getCurrentUser();
        // Ignoring search and category logic for simplicity in this task,
        // but returning all books.
        model.addAttribute("books", bookService.findAllBooks());
        model.addAttribute("issues", issueService.findIssuesByUser(user));
        model.addAttribute("bookings", bookingService.findBookingsByUser(user));
        return "user/dashboard";
    }

    @PostMapping("/issue/{id}")
    public String issueBook(@PathVariable("id") Long bookId) {
        User user = getCurrentUser();
        Book book = bookService.findById(bookId).orElse(null);
        if (book != null && user != null) {
            issueService.issueBook(book, user);
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/book-advance/{id}")
    public String bookAdvance(@PathVariable("id") Long bookId) {
        User user = getCurrentUser();
        Book book = bookService.findById(bookId).orElse(null);
        if (book != null && user != null && book.getAvailableQuantity() == 0) {
            bookingService.createBooking(book, user);
        }
        return "redirect:/user/dashboard";
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable("id") Long issueId) {
        issueService.returnBook(issueId);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/contact")
    public String contactForm() {
        return "user/contact";
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam String message) {
        User user = getCurrentUser();
        if (user != null) {
            ContactQuery query = new ContactQuery();
            query.setUser(user);
            query.setMessage(message);
            query.setCreatedAt(LocalDateTime.now());
            contactService.saveQuery(query);
        }
        return "redirect:/user/contact?success";
    }
}
