package com.afklm.flightreview.services;

import com.afklm.flightreview.bean.ReviewSearchParameter;
import com.afklm.flightreview.entities.DbUser;
import com.afklm.flightreview.entities.Review;
import com.afklm.flightreview.enums.ReviewStatus;
import com.afklm.flightreview.repositories.NoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final DbUserService dbUserService;
    public NoteService(NoteRepository noteRepository, DbUserService dbUserService) {
        this.noteRepository = noteRepository;
        this.dbUserService = dbUserService;
    }

    public List<Review> findAll() {
        return noteRepository.findAll();
    }

    @Transactional
    public Review save(Review review, String username) {
        if (review == null || username == null) {
            throw new IllegalArgumentException("Review and username must not be null");
        }

        DbUser user = dbUserService.getDbUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        review.setDbUser(user);
        return noteRepository.save(review);
    }

    public Page<Review> getAll(ReviewSearchParameter params, Pageable pageable) {
        return noteRepository.search(params, pageable);
    }

    public Review findById(Long id) {
        return noteRepository.findById(id).get();
    }

    @Transactional
    public void updateStatus(Long reviewId, ReviewStatus status) {
        Review review = findById(reviewId);
        review.setStatus(status);
        noteRepository.save(review);
    }
}
