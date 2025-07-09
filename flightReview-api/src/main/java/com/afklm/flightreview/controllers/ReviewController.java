package com.afklm.flightreview.controllers;

import com.afklm.flightreview.bean.ReviewSearchParameter;
import com.afklm.flightreview.entities.Review;
import com.afklm.flightreview.enums.Outcome;
import com.afklm.flightreview.enums.ReviewStatus;
import com.afklm.flightreview.services.NoteService;
import com.afklm.flightreview.services.ReferenceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(value="/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {
    private final NoteService noteService;
    private final ReferenceService referenceService;

    public ReviewController (NoteService noteService,ReferenceService referenceService) {
        this.noteService = noteService;
        this.referenceService = referenceService;
    }

    @PostMapping()
    public ResponseEntity<Review> addNote(@RequestBody Review review, @RequestParam String username) {
            Review savedReview = noteService.save(review, username);
            return ResponseEntity.ok(savedReview);
    }

    @GetMapping()
    public Page<Review> getAllEntities(@RequestParam(required = false) String reference,
                                       @RequestParam(required = false) String flightNumber,
                                       @RequestParam(required = false) String airline,
                                       @RequestParam(required = false) Integer rating,
                                       @RequestParam(required = false) String comment,
                                       @RequestParam(value = "flightDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final Date flightDate,
                                       @RequestParam(required = false) ReviewStatus status,
                                       @RequestParam(required = false) String writerName,
                                       @RequestParam(required = false) Outcome outcome,
                                       final Pageable pageable) {
        // Creation of Review Beans for filter
        ReviewSearchParameter params = ReviewSearchParameter.builder()
                .reference(reference)
                .flightNumber(flightNumber)
                .airline(airline)
                .rating(rating)
                .comment(comment)
                .writerName(writerName)
                .flightDate(flightDate)
                .status(status)
                .outcome(outcome)
                .build();

        return noteService.getAll(params, pageable);
    }

    @GetMapping("/reference")
    public ResponseEntity<String> getReferences() {
        return ResponseEntity.ok(referenceService.generateReference());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable long id) {
        Optional<Review> reviewOpt = Optional.ofNullable(noteService.findById(id));
        if (reviewOpt.isPresent()) {
            return ResponseEntity.ok(reviewOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
