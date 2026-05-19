package com.reiner.greenflix.model;

public class Film {

    private String id;
    private String title;
    private String genre;
    private String image;
    private String coverImage;
    private String description;
    private String rating;
    private String trailer;

    public Film(String id,
                String title,
                String genre,
                String image,
                String coverImage,
                String description,
                String rating,
                String trailer) {

        this.id = id;
        this.title = title;
        this.genre = genre;
        this.image = image;
        this.coverImage = coverImage;
        this.description = description;
        this.rating = rating;
        this.trailer = trailer;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getImage() {
        return image;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getTrailer() {
        return trailer;
    }
}