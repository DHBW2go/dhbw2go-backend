package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.ToDo;
import de.dhbw2go.backend.entities.User;
import de.dhbw2go.backend.exceptions.todo.ToDoDifferentOwnerException;
import de.dhbw2go.backend.exceptions.todo.ToDoNotFoundException;
import de.dhbw2go.backend.payload.requests.todo.ToDoCreateRequest;
import de.dhbw2go.backend.services.ToDoService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@Tag(name = "ToDo")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ToDo.class)), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDo[]> list(final Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        final ToDo[] toDos = this.toDoService.loadToDosByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(toDos);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ToDo.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @PutMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDo> create(final Authentication authentication, @Valid @RequestBody final ToDoCreateRequest toDoCreateRequest) {
        final User user = (User) authentication.getPrincipal();
        final ToDo toDo = this.toDoService.createToDo(user, toDoCreateRequest.getText());
        return ResponseEntity.status(HttpStatus.OK).body(toDo);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ToDo.class), mediaType = "application/json")},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @PostMapping(path = "/changeStatus/{todo-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDo> changeStatus(final Authentication authentication, @PathVariable("todo-id") final int todoId) {
        final User user = (User) authentication.getPrincipal();
        try {
            final ToDo changedStatusToDo = this.toDoService.changeToDoStatus(user, todoId);
            return ResponseEntity.status(HttpStatus.OK).body(changedStatusToDo);
        } catch (final ToDoDifferentOwnerException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (final ToDoNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())},
                    description = ""),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())},
                    description = "")
    })
    @DeleteMapping(path = "/delete/{todo-id}")
    public ResponseEntity<?> remove(final Authentication authentication, @PathVariable("todo-id") final int todoId) {
        final User user = (User) authentication.getPrincipal();
        try {
            this.toDoService.deleteToDo(user, todoId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (final ToDoDifferentOwnerException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (final ToDoNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
