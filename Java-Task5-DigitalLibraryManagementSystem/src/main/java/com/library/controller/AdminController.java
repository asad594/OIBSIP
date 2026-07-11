package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import com.library.service.ContactService;
import com.library.service.IssueService;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("books", bookService.findAllBooks());
        model.addAttribute("issues", issueService.findAllIssues());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("queries", contactService.findAllQueries());
        return "admin/dashboard";
    }

    @PostMapping("/add-book")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/pay-fine/{id}")
    public String payFine(@PathVariable Long id) {
        issueService.payFine(id);
        return "redirect:/admin/dashboard";
    }
}
