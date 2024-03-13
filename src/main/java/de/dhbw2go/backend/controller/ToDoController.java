package de.dhbw2go.backend.controller;

import de.dhbw2go.backend.entities.ToDo;
import de.dhbw2go.backend.payload.requests.todo.ToDoCreateRequest;
import de.dhbw2go.backend.security.SecurityUserDetails;
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

import javax.naming.OperationNotSupportedException;
import java.rmi.NoSuchObjectException;

@RestController
@RequestMapping("/todo")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@Tag(name = "ToDo")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ToDo.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})
    })
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDo[]> list(final Authentication authentication) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        final ToDo[] toDos = this.toDoService.loadToDosByUser(securityUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(toDos);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ToDo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())})
    })
    @PutMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDo> create(final Authentication authentication, @Valid @RequestBody final ToDoCreateRequest toDoCreateRequest) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        final ToDo toDo = this.toDoService.createToDo(securityUserDetails.getUser(), toDoCreateRequest.getText());
        return ResponseEntity.status(HttpStatus.OK).body(toDo);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ToDo.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @PostMapping(path = "/changeStatus/{todo-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDo> changeStatus(final Authentication authentication, @PathVariable("todo-id") final int todoId) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        try {
            final ToDo changedStatusToDo = this.toDoService.changeToDoStatus(securityUserDetails.getUser(), todoId);
            return ResponseEntity.status(HttpStatus.OK).body(changedStatusToDo);
        } catch (final OperationNotSupportedException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (final NoSuchObjectException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping(path = "/delete/{todo-id}")
    public ResponseEntity<?> remove(final Authentication authentication, @PathVariable("todo-id") final int todoId) {
        final SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
        try {
            this.toDoService.deleteToDo(securityUserDetails.getUser(), todoId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (final OperationNotSupportedException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (final NoSuchObjectException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
