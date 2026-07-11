package com.library.repository;

import com.library.model.BookBooking;
import com.library.model.Book;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBookingRepository extends JpaRepository<BookBooking, Long> {
    List<BookBooking> findByUser(User user);
    List<BookBooking> findByBookAndStatusOrderByBookingDateAsc(Book book, String status);
}
