package de.dhbw2go.backend.repositories;

import de.dhbw2go.backend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
