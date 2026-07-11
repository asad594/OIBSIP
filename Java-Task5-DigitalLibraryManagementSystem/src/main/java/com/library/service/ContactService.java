package com.library.service;

import com.library.model.ContactQuery;
import com.library.repository.ContactQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactQueryRepository contactQueryRepository;

    public void saveQuery(ContactQuery query) {
        contactQueryRepository.save(query);
    }

    public List<ContactQuery> findAllQueries() {
        return contactQueryRepository.findAllByOrderByCreatedAtDesc();
    }
}
