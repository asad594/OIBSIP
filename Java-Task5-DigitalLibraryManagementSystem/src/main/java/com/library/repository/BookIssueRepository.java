package com.library.repository;

import com.library.model.BookIssue;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
    List<BookIssue> findByUser(User user);
    List<BookIssue> findByStatus(String status);
}
