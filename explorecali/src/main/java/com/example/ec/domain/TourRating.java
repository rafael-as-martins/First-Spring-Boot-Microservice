package com.example.ec.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class TourRating {

    @EmbeddedId
    private TourRatingPK pk;

    @Column(nullable = false)
    private Integer score;

    @Column
    private String comment;

    public TourRating() {
    }

    public TourRating(TourRatingPK pk, Integer score, String comment) {
        this.pk = pk;
        this.score = score;
        this.comment = comment;
    }

    public TourRatingPK getPk() {
        return pk;
    }

    public void setPk(TourRatingPK pk) {
        this.pk = pk;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
