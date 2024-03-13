package de.dhbw2go.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Data
@Entity
@Table(name = "refresh_token", uniqueConstraints = {@UniqueConstraint(columnNames = {"token"})})
public class RefreshToken {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String token;

    @NotNull
    private Instant expiration;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;
}
