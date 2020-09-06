package com.example.ec.domain;

import java.util.Arrays;
import java.util.Optional;

public enum Region {

    CENTRAL_COAST("Central Coast"),

    SOUTHERN_CALIFORNIA("Southern California"),

    NORTHERN_CALIFORNIA("Northern California"),

    VARIES("Varies");

    private String label;

    Region(String label){
        this.label = label;
    }

    public static Optional<Region> findByLabel(String label){

        Optional<Region> region = Arrays.stream(Region.values())
                                        .filter(e -> e.getLabel().equals(label))
                                        .findFirst();

        return region;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
