package com.example.ec.web;


import com.example.ec.domain.Tour;
import com.example.ec.domain.TourRating;
import com.example.ec.repo.TourRatingRepository;
import com.example.ec.repo.TourRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {


    private final TourRatingRepository tourRatingRepository;
    private final TourRepository tourRepository;

    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    @PostMapping(path = "/tours/{tourId}/rating")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@PathVariable String tourId, @RequestBody @Validated TourRating rating){
        verifyTour(tourId);
        tourRatingRepository.save(new TourRating(tourId, rating.getCustomerId(),  rating.getScore(), rating.getComment()));
    }

    @GetMapping(path = "/tours/{tourId}/ratings")
    public List<TourRating> getAllRatingsForTour(@PathVariable String tourId){
        verifyTour(tourId);
        return tourRatingRepository.findByTourId(tourId);
    }

    /**
     * Verify and return the TourRating for a particular tourId and Customer
     * @param tourId tour identifier
     * @param customerId customer identifier
     * @return the found TourRating
     * @throws NoSuchElementException if no TourRating found
     */
    private TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException {
        return tourRatingRepository.findByTourIdAndCustomerId(tourId, customerId).orElseThrow(() ->
                new NoSuchElementException("Tour-Rating pair for request("
                        + tourId + " for customer" + customerId));
    }


    /**
     * Update score and comment of a Tour Rating
     *
     * @param tourId tour identifier
     * @param tourRating rating Data Transfer Object
     * @return The modified Rating DTO.
     */
    @PutMapping
    public TourRating updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated TourRating tourRating) {
        TourRating rating = verifyTourRating(tourId, tourRating.getCustomerId());
        rating.setScore(tourRating.getScore());
        rating.setComment(tourRating.getComment());
        return tourRatingRepository.save(rating);
    }

    /**
     * Delete a Rating of a tour made by a customer
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     */
    @DeleteMapping(path = "/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        TourRating rating = verifyTourRating(tourId, customerId);
        tourRatingRepository.delete(rating);
    }

    /**
     * Lookup a page of Ratings for a tour.
     *
     * @param tourId Tour Identifier
     * @param pageable paging details
     * @return Requested page of Tour Ratings as RatingDto's
     */
    @GetMapping
    public Page<TourRating> getRatings(@PathVariable(value = "tourId") String tourId,
                                      Pageable pageable){
        return tourRatingRepository.findByTourId(tourId, pageable);
    }

    /**
     * Update score or comment of a Tour Rating
     *
     * @param tourId tour identifier
     * @param tourRating rating Data Transfer Object
     * @return The modified Rating DTO.
     */
    @PatchMapping
    public TourRating updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated TourRating tourRating) {
        TourRating rating = verifyTourRating(tourId, tourRating.getCustomerId());
        if (tourRating.getScore() != null) {
            rating.setScore(tourRating.getScore());
        }
        if (tourRating.getComment() != null) {
            rating.setComment(tourRating.getComment());
        }
        return tourRatingRepository.save(rating);
    }

    @GetMapping(path = "/tours/{tourId}/ratings/average")
    public Map<String, Double> getAverage(@PathVariable String tourId){

        verifyTour(tourId);

        return Map.of("average", tourRatingRepository.findByTourId(tourId).stream()
                                    .mapToInt(TourRating::getScore).average()
                                    .orElseThrow(()-> new NoSuchElementException("Tour has no ratings")));


    }


    private Tour verifyTour(String tourId) throws NoSuchElementException{
        return this.tourRepository.findById(tourId).orElseThrow(() -> new NoSuchElementException("No tour exists with the code " + tourId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return404(NoSuchElementException ex){
        return ex.getMessage();
    }







}
