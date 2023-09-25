package com.nighthawk.spring_portfolio.mvc.note;

import java.util.List;

import javax.transaction.Transactional;

import com.nighthawk.spring_portfolio.mvc.person.Person;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteJpaRepository extends JpaRepository<Note, Long> {
    List<Person> findByPersonId(Long id);

    @Transactional
    void deleteByPersonId(long id);
}

