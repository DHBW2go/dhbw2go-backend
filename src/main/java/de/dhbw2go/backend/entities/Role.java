package de.dhbw2go.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"type"})})
public class Role {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleType type;

    public enum RoleType {
        USER, ADMINISTRATOR
    }
}
