package com.library.service;

import com.library.model.Book;
import com.library.model.BookIssue;
import com.library.model.User;
import com.library.repository.BookIssueRepository;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.model.BookBooking;
import com.library.repository.BookBookingRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    private BookIssueRepository bookIssueRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookBookingRepository bookBookingRepository;

    public List<BookIssue> findAllIssues() {
        return bookIssueRepository.findAll();
    }

    public List<BookIssue> findIssuesByUser(User user) {
        return bookIssueRepository.findByUser(user);
    }

    @Transactional
    public void issueBook(Book book, User user) {
        if (book.getAvailableQuantity() > 0) {
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            bookRepository.save(book);

            BookIssue issue = new BookIssue();
            issue.setBook(book);
            issue.setUser(user);
            issue.setStatus("ISSUED");
            issue.setIssueDate(LocalDate.now());
            issue.setDueDate(LocalDate.now().plusDays(14)); // 14 days borrow period
            issue.setFineAmount(0.0);
            issue.setFinePaid(false);
            
            bookIssueRepository.save(issue);
        } else {
            throw new RuntimeException("Book is not available");
        }
    }

    @Transactional
    public void returnBook(Long issueId) {
        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        if ("ISSUED".equals(issue.getStatus())) {
            issue.setStatus("RETURNED");
            issue.setReturnDate(LocalDate.now());
            
            // Calculate fine if overdue (e.g. 10 per day)
            long daysOverdue = ChronoUnit.DAYS.between(issue.getDueDate(), issue.getReturnDate());
            if (daysOverdue > 0) {
                issue.setFineAmount((double) (daysOverdue * 10)); // 10 currency units per day
            }

            Book book = issue.getBook();
            List<BookBooking> pendingBookings = bookBookingRepository.findByBookAndStatusOrderByBookingDateAsc(book, "PENDING");
            if (!pendingBookings.isEmpty()) {
                BookBooking oldestBooking = pendingBookings.get(0);
                oldestBooking.setStatus("COMPLETED");
                bookBookingRepository.save(oldestBooking);

                BookIssue newIssue = new BookIssue();
                newIssue.setBook(book);
                newIssue.setUser(oldestBooking.getUser());
                newIssue.setStatus("ISSUED");
                newIssue.setIssueDate(LocalDate.now());
                newIssue.setDueDate(LocalDate.now().plusDays(14));
                newIssue.setFineAmount(0.0);
                newIssue.setFinePaid(false);
                bookIssueRepository.save(newIssue);
            } else {
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                bookRepository.save(book);
            }
            
            bookIssueRepository.save(issue);
        }
    }

    public void payFine(Long issueId) {
        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        issue.setFinePaid(true);
        bookIssueRepository.save(issue);
    }
}
