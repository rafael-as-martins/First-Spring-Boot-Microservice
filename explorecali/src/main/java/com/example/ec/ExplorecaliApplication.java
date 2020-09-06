package com.example.ec;

import com.example.ec.domain.Difficulty;
import com.example.ec.domain.Region;
import com.example.ec.service.TourPackageService;
import com.example.ec.service.TourService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class ExplorecaliApplication implements CommandLineRunner {

    @Value("${ec.importfile}")
    private String importFile;

    private final TourPackageService tourPackageService;
    private final TourService tourService;

    @Autowired
    public ExplorecaliApplication(TourService tourService, TourPackageService tourPackageService) {
        this.tourService = tourService;
        this.tourPackageService = tourPackageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExplorecaliApplication.class, args);
    }


    private void createTours() throws IOException {

        TourFromFile.read(importFile).forEach(tour ->
                tourService.createTour(tour.getTitle(), tour.getDescription(),
                               tour.getBlurb(), tour.getPrice(), tour.getBullets(),
                               tour.getKeywords(), tour.getPackageType(), tour.getDifficulty(),
                               (tour.getRegion().isPresent()) ? tour.getRegion().get() : null));


    }

    private void createTourPackages() {
        tourPackageService.createTourPackage("BC", "Backpack Cal");
        tourPackageService.createTourPackage("CC", "California Calm");
        tourPackageService.createTourPackage("CH", "California Hot springs");
        tourPackageService.createTourPackage("CY", "Cycle California");
        tourPackageService.createTourPackage("DS", "From Desert to Sea");
        tourPackageService.createTourPackage("KC", "Kids California");
        tourPackageService.createTourPackage("NW", "Nature Watch");
        tourPackageService.createTourPackage("SC", "Snowboard Cali");
        tourPackageService.createTourPackage("TC", "Taste of California");
    }

    @Override
    public void run(String... args) throws Exception {

        createTourPackages();
        long numOfPackages = tourPackageService.total();

        createTours();
        long numOfTours = tourService.total();

    }

    protected static class TourFromFile {

        private String packageType, title, description,
                blurb, price, length, bullets, keywords,
                difficulty, region;

        protected TourFromFile() {
        }

        static List<TourFromFile> read(String fileToImport) throws IOException {

            return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                    .readValue(new FileInputStream(fileToImport), new TypeReference<>() {
                    });

        }

        String getPackageType() {
            return packageType;
        }

        String getTitle() {
            return title;
        }

        String getDescription() {
            return description;
        }

        String getBlurb() {
            return blurb;
        }

        Integer getPrice() {
            return Integer.parseInt(price);
        }

        String getLength() {
            return length;
        }

        String getBullets() {
            return bullets;
        }

        String getKeywords() {
            return keywords;
        }

        Difficulty getDifficulty() {
            return Difficulty.valueOf(difficulty);
        }

        Optional<Region> getRegion() {
            return Region.findByLabel(region);
        }

    }
}
