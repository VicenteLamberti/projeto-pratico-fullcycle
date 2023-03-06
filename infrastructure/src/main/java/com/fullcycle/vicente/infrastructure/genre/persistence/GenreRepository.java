package com.fullcycle.vicente.infrastructure.genre.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreJPAEntity,String> {
}
