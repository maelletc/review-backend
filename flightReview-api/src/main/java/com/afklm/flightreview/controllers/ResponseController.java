package com.afklm.flightreview.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.afklm.flightreview.entities.Response;
import com.afklm.flightreview.services.ResponseService;
import com.afklm.flightreview.services.NoteService;
import com.afklm.flightreview.enums.ReviewStatus;

@RestController
@RequestMapping("/responses")
public class ResponseController {

    private ResponseService responseService;
    private NoteService noteService;

    public ResponseController(ResponseService responseService, NoteService noteService) {
        this.responseService = responseService;
        this.noteService = noteService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Tu peux retirer cette annotation si tu renvoies un body
    public ResponseEntity<Response> addResponse(@RequestParam String username, @RequestBody Response response) {
        try {
            Response savedResponse = responseService.addResponseToReview(response.getReview().getId(), response, username);
            noteService.updateStatus(response.getReview().getId(), ReviewStatus.DONE);
            return ResponseEntity.ok(savedResponse);  // renvoie 200 OK avec la réponse en body
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
