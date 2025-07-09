package com.afklm.flightreview.repositories;

import com.afklm.flightreview.bean.ReviewSearchParameter;
import com.afklm.flightreview.entities.*;
import com.afklm.flightreview.enums.ReviewStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteRepositoryCustomImpl implements NoteRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Review> search(ReviewSearchParameter params, Pageable pageable) {

        // get list of Review filtered
        List<Review> results = getList(params, pageable);

        // get number of Review filtered
        long count = count(params);
        return new PageImpl<>(results, pageable, count);
    }

    private List<Review> getList(ReviewSearchParameter params, Pageable pageable) {
        // Create query
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);

        // FROM
        Root<Review> review = cq.from(Review.class);

        // WHERE
        where(params, cb, review, cq);

        if(pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(sort -> {
                String property = sort.getProperty();
                Path<?> path;

                // Gérer les propriétés imbriquées
                if ("flightNumber".equals(property)) {
                    // Créer un join avec l'entité Flight pour accéder à flightNumber
                    Join<Review, Flight> flightJoin = review.join(Review_.flight, JoinType.LEFT);
                    path = flightJoin.get(Flight_.flightNumber);
                }else if ("flightDate".equals(property)) {
                    // Créer un join avec l'entité Flight pour accéder à flightDate
                    Join<Review, Flight> flightJoin = review.join(Review_.flight, JoinType.LEFT);
                    path = flightJoin.get(Flight_.flightDate);
                }
                else {
                    // Pour les propriétés directes de Review
                    path = review.get(property);
                }

                if(sort.getDirection().isAscending()) {
                    orders.add(cb.asc(path));
                } else {
                    orders.add(cb.desc(path));
                }
            });
            cq.orderBy(orders);
        }

        // Pagination
        TypedQuery<Review> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Execute query
        return query.getResultList();
    }

    private static void where(ReviewSearchParameter params, CriteriaBuilder cb, Path<Review> review, CriteriaQuery<?> cq) {
        List<Predicate> predicates = new ArrayList<>();

        if (params.getReference() != null) {
            predicates.add(cb.like(review.get(Review_.reference), "%" + params.getReference() + "%"));
        }

        if (params.getStatus() != null) {
            predicates.add(cb.equal(review.get(Review_.status), params.getStatus()));
        }

        if (params.getOutcome() != null) {
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Review> subRoot = subquery.from(Review.class);

            // Join on the 'response' relationship
            Join<Review, Response> responseJoin = subRoot.join(Review_.response);

            // Vérifie si outcome correspond au paramètre
            subquery.select(subRoot.get(Review_.id))
                    .where(
                            cb.and(
                                    cb.equal(subRoot, review),
                                    cb.equal(responseJoin.get(Response_.outcome), params.getOutcome())
                            )
                    );
            predicates.add(cb.exists(subquery));
        }

        if (StringUtils.isNotEmpty(params.getWriterName())) {
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Review> subRoot = subquery.from(Review.class);

            // Join on the 'user' relationship
            Join<Review, DbUser> userJoin = subRoot.join(Review_.dbUser);

            // Check if 'user' starts with the search term
            subquery.select(subRoot.get(Review_.id))
                    .where(
                            cb.and(
                                    cb.equal(subRoot, review),
                                    cb.like(cb.lower(userJoin.get(DbUser_.username)), params.getWriterName().trim().toLowerCase() + "%")
                            )
                    );
            predicates.add(cb.exists(subquery));
        }

        if (StringUtils.isNotEmpty(params.getFlightNumber())) {

            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Review> subRoot = subquery.from(Review.class);

            // Join on the 'flight' relationship
            Join<Review, Flight> flightJoin = subRoot.join(Review_.flight);

            // Check if 'flight' starts with the search term
            subquery.select(subRoot.get(Review_.id))
                    .where(
                            cb.and(
                                    cb.equal(subRoot, review),
                                    cb.like(cb.lower(flightJoin.get(Flight_.flightNumber)), params.getFlightNumber().trim().toLowerCase() + "%")
                            )
                    );
            predicates.add(cb.exists(subquery));
        }

        if (StringUtils.isNotEmpty(params.getAirline())) {
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Review> subRoot = subquery.from(Review.class);

            // Join on the 'flight' relationship
            Join<Review, Flight> flightJoin = subRoot.join(Review_.flight);

            // Check if 'flight' starts with the search term
            subquery.select(subRoot.get(Review_.id))
                    .where(
                            cb.and(
                                    cb.equal(subRoot, review),
                                    cb.like(cb.lower(flightJoin.get(Flight_.airline)), params.getAirline().trim().toLowerCase() + "%")
                            )
                    );
            predicates.add(cb.exists(subquery));
        }

        if (params.getFlightDate() != null) {
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<Review> subRoot = subquery.from(Review.class);

            // Join on the 'flight' relationship
            Join<Review, Flight> flightJoin = subRoot.join(Review_.flight);

            // Check if flightDate equals the search date
            subquery.select(subRoot.get(Review_.id))
                    .where(
                            cb.and(
                                    cb.equal(subRoot, review),
                                    cb.equal(flightJoin.get(Flight_.flightDate), params.getFlightDate())
                            )
                    );
            predicates.add(cb.exists(subquery));
        }

        cq.where(predicates.toArray(new Predicate[0]));
    }

    private long count(ReviewSearchParameter params) {
        // Create query
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        //FROM
        Root<Review> reviewCountRoot = countQuery.from(Review.class);

        //WHERE
        countQuery.select(cb.count(reviewCountRoot));
        where(params, cb, reviewCountRoot, countQuery);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
