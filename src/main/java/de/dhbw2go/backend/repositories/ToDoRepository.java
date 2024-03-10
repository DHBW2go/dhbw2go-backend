package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ToDoRepository extends JpaRepository<ToDo, Integer> {

}
