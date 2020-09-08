package com.example.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    String releaseDate;
    Double voteAverage;
    Integer id;
    Double popularity;

    public Movie(JSONObject j) throws JSONException {
        posterPath = j.getString("poster_path");
        backdropPath = j.getString("backdrop_path");
        title = j.getString("title");
        voteAverage = j.getDouble("vote_average");
        overview = j.getString("overview");
        releaseDate = j.getString("release_date");
        id = j.getInt("id");
        popularity = j.getDouble("popularity");
    }

    public Movie(){ }
    public static List<Movie> fromJSONArray(JSONArray array) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            movies.add(new Movie(array.getJSONObject(i)));
        }
        return movies;

    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", backdropPath);
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public Double getPopularity() {
        return popularity;
    }
}
