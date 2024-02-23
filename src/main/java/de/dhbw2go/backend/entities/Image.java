package de.dhbw2go.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotBlank
    @Size(max = 1024)
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 128)
    private String type;

    @Lob
    @NotNull
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] data;
}
