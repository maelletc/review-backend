package com.afklm.flightreview.services;

import com.afklm.flightreview.entities.DbUser;
import com.afklm.flightreview.entities.Response;
import com.afklm.flightreview.entities.Review;
import com.afklm.flightreview.enums.Outcome;
import com.afklm.flightreview.enums.ReviewStatus;
import com.afklm.flightreview.repositories.DbUserRepository;
import com.afklm.flightreview.repositories.NoteRepository;
import com.afklm.flightreview.repositories.ResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ResponseService {

    private ResponseRepository responseRepository;
    private NoteRepository noteRepository;
    private DbUserService dbUserService;

    public ResponseService(ResponseRepository responseRepository, NoteRepository noteRepository,DbUserService dbUserService, NoteService noteService){
        this.responseRepository = responseRepository;
        this.dbUserService = dbUserService;
        this.noteRepository = noteRepository;
    }

    @Transactional
    public Response addResponseToReview(Long reviewId, Response response, String username) {
        Review review = noteRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        DbUser user = dbUserService.getDbUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        response.setReview(review);
        response.setDbUser(user);

        try {
            response.setOutcome(Outcome.valueOf(String.valueOf(response.getOutcome())));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Outcome invalide : " + response.getOutcome());
        }

        Response savedResponse = responseRepository.save(response);
        review.setResponse(savedResponse);
        noteRepository.save(review);

        return savedResponse;
    }


}
