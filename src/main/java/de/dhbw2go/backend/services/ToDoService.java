package de.dhbw2go.backend.services;

import de.dhbw2go.backend.entities.ToDo;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.exceptions.todo.ToDoDifferentOwnerException;
import de.dhbw2go.backend.exceptions.todo.ToDoNotFoundException;
import de.dhbw2go.backend.repositories.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    public ToDo[] loadToDosByUser(final User user) {
        return this.toDoRepository.findAllByUser(user).toArray(new ToDo[0]);
    }

    public ToDo createToDo(final User user, final String text) {
        final ToDo toDo = new ToDo();
        toDo.setText(text);
        toDo.setDone(false);
        toDo.setUser(user);
        return this.toDoRepository.save(toDo);
    }

    public ToDo changeToDoStatus(final User user, final int todoId) throws ToDoNotFoundException, ToDoDifferentOwnerException {
        final Optional<ToDo> toDoOptional = this.toDoRepository.findById(todoId);
        if (toDoOptional.isPresent()) {
            final ToDo toDo = toDoOptional.get();
            if (toDo.getUser().getId() == user.getId()) {
                toDo.setDone(!toDo.isDone());
                return this.toDoRepository.save(toDo);
            }
            throw new ToDoDifferentOwnerException(user.getName(), todoId);
        }
        throw new ToDoNotFoundException(todoId);
    }

    public void deleteToDo(final User user, final int todoId) throws ToDoNotFoundException, ToDoDifferentOwnerException {
        final Optional<ToDo> toDoOptional = this.toDoRepository.findById(todoId);
        if (toDoOptional.isPresent()) {
            final ToDo toDo = toDoOptional.get();
            if (toDo.getUser().getId() == user.getId()) {
                this.toDoRepository.delete(toDo);
                return;
            }
            throw new ToDoDifferentOwnerException(user.getName(), todoId);
        }
        throw new ToDoNotFoundException(todoId);
    }
}
