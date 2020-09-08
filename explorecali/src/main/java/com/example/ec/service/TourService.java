package com.example.ec.service;

import com.example.ec.domain.Tour;
import com.example.ec.domain.TourPackage;
import com.example.ec.repo.TourPackageRepository;
import com.example.ec.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
     * Creates a tour
     *
     * @param title
     * @param tourPackageName
     * @param details
     * @return
     */
    public Tour createTour(String title, String tourPackageName, Map<String, String> details){

        TourPackage tourPackage = this.tourPackageRepository.findByName(tourPackageName)
                                    .orElseThrow(() -> new RuntimeException("Tour Package doesn't exist with :" + tourPackageName));

        return this.tourRepository.save(new Tour(title, tourPackage, details));

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
