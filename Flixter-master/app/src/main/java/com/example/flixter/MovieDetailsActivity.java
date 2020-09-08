package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    TextView tvDate;
    RatingBar rbRatings;
    ImageView ivPoster;
    TextView tvPop;
    String key;
    String site;
    ImageView playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        assert movie != null;
        Log.d("MovieDetails", "Showing details for: " + movie.getTitle());
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbRatings = binding.rbRating;
        tvDate = binding.tvDate;
        ivPoster = binding.ivPoster;
        playButton = binding.imageView;
        tvPop = binding.tvPop;

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvPop.setText(String.format("Popularity: %s/100", movie.getPopularity()));
        float rating = movie.getVoteAverage().floatValue();
        rbRatings.setRating(rating > 0 ? rating / 2.0f : rating);
        tvDate.setText(String.format("Released: %s", movie.getReleaseDate()));
        String imageURL = movie.getBackdropPath();
        int placeholder = R.drawable.flicks_backdrop_placeholder;
        Glide.with(this)
                .load(imageURL)
                .transform(new RoundedCornersTransformation(30, 10))
                .placeholder(placeholder)
                .into(ivPoster);

        String url = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=13f4a0b4abdb785e3abfdf4c8e005152&language=en-US", movie.getId());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    key = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");
                    site = jsonObject.getJSONArray("results").getJSONObject(0).getString("site");
                    playButton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.e("MovieDetailsActivity", "Hit json exception", e);
                    key = "";
                    playButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("MovieDetailsActivity", "onFailure");
                playButton.setVisibility(View.INVISIBLE);
            }
        });

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!key.equals("") && site.equals("YouTube")) {
                    Intent intent = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                    intent.putExtra("key", key);
                    startActivity(intent);
                } else {
                    failToast();
                }
            }
        });

    }

    private void failToast() {
        Toast.makeText(getApplicationContext(),"Video Unavailable! :(", Toast.LENGTH_SHORT).show();
    }
}