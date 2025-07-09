package com.afklm.flightreview.repositories;

import com.afklm.flightreview.entities.Flight;
import com.afklm.flightreview.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Review, Long> , NoteRepositoryCustom {
    @Query("SELECT a FROM Review a WHERE a.reference LIKE ?1 ORDER BY a.id DESC")
    List<Review> findLatestReferences(String yearPattern);
}
