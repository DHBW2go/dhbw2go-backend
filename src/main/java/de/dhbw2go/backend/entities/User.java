package de.dhbw2go.backend.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotBlank
    @Size(max = 32)
    private String username;

    @Email
    @NotNull
    @NotBlank
    @Size(max = 64)
    private String email;

    @NotNull
    @NotBlank
    @Size(max = 128)
    private String password;

    @NotNull
    private int level;

    @Nullable
    private int experience;

    @Nullable
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Role> roles = new HashSet<>();

    public String getLevelAsString() {
        if (level == 1) {
            return "Novice Meme Enthusiast";
        } else if (level == 2) {
            return "Meme Rookie";
        } else if (level == 3) {
            return "Meme Connoisseur";
        } else if (level == 4) {
            return "Meme Trendsetter";
        } else if (level == 5) {
            return "Meme Master";
        } else if (level == 6) {
            return "Memelord";
        }
        return null;
    }

    public void addRole(final Role role) {
        this.roles.add(role);
    }

    public void removeRole(final Role role) {
        this.roles.remove(role);
    }
}
