package com.example.ec.service;

import com.example.ec.domain.Difficulty;
import com.example.ec.domain.Region;
import com.example.ec.domain.Tour;
import com.example.ec.domain.TourPackage;
import com.example.ec.repo.TourPackageRepository;
import com.example.ec.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final TourPackageRepository tourPackageRepository;

    @Autowired
    public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository){
        this.tourRepository = tourRepository;
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
     *
     * Creates a tour Object and saves it in DB
     *
     * @param title field
     * @param description field
     * @param blurb field
     * @param price field
     * @param bullets field
     * @param keywords field
     * @param tourPackageName field
     * @param difficulty field
     * @param region field
     *
     * @return the created Tour
     */
    public Tour createTour(String title, String description, String blurb, Integer price,
                           String bullets, String keywords, String tourPackageName,
                           Difficulty difficulty, Region region){

        TourPackage tourPackage = this.tourPackageRepository.findByName(tourPackageName)
                                    .orElseThrow(() -> new RuntimeException("Tour Package doesn't exist with :" + tourPackageName));

        return this.tourRepository.save(new Tour(title, description, blurb, price, bullets, keywords, tourPackage, difficulty, region));

    }


    /**
     *
     * Lookup All tour
     *
     * @return all tour
     */
    public long total(){
        return this.tourRepository.count();
    }

}
