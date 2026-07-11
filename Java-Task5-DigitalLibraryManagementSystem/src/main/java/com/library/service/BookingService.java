package com.library.service;

import com.library.model.Book;
import com.library.model.BookBooking;
import com.library.model.User;
import com.library.repository.BookBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookBookingRepository bookingRepository;

    public List<BookBooking> findBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    @Transactional
    public void createBooking(Book book, User user) {
        BookBooking booking = new BookBooking();
        booking.setBook(book);
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("PENDING");
        bookingRepository.save(booking);
    }
}
