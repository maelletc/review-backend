package com.afklm.flightreview.repositories;

import com.afklm.flightreview.bean.ReviewSearchParameter;
import com.afklm.flightreview.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteRepositoryCustom {
    Page<Review> search(ReviewSearchParameter params, Pageable pageable);
}