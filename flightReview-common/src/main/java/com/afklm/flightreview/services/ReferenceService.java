package com.afklm.flightreview.services;

import com.afklm.flightreview.entities.Review;
import com.afklm.flightreview.repositories.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReferenceService {
    private static final String REFERENCE_FORMAT = "%d/%05d";
    private final NoteRepository noteRepository;

    public ReferenceService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    @Transactional
    public String generateReference() {
        int year = LocalDate.now().getYear();
        String yearPattern = year + "/%";

        List<Review> latestReviews = noteRepository.findLatestReferences(yearPattern);

        int nextSequence = 1;

        if (!latestReviews.isEmpty()) {
            Review latestReview = latestReviews.get(0);
            String[] parts = latestReview.getReference().split("/");
            if (parts.length == 2) {
                nextSequence = Integer.parseInt(parts[1]) + 1;
            }
        }

        return String.format(REFERENCE_FORMAT, year, nextSequence);
    }
}
