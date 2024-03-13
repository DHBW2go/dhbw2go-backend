package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.ToDo;
import de.dhbw2go.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ToDoRepository extends JpaRepository<ToDo, Integer> {

    List<ToDo> findAllByUser(final User user);
}
