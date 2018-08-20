package odlare.com.movieapp2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import odlare.com.movieapp2.R;
import odlare.com.movieapp2.adapter.MovieAdapter;
import odlare.com.movieapp2.adapter.ReviewAdapter;
import odlare.com.movieapp2.adapter.TrailerAdapter;
import odlare.com.movieapp2.model.Review;
import odlare.com.movieapp2.model.ReviewList;
import odlare.com.movieapp2.model.RoomEntity.MovieRoom;
import odlare.com.movieapp2.model.Trailer;
import odlare.com.movieapp2.model.TrailerList;
import odlare.com.movieapp2.network.GetMovieDataService;
import odlare.com.movieapp2.network.RetrofitInstance;
import odlare.com.movieapp2.repository.MovieRoomRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private MovieRoomRepository movieRoomRepository;

    private int id;
    private boolean isFavorite;

    private TextView txtLanguage;
    private TextView txtReleasedOn;
    private TextView txtVoteAverage;
    private TextView txtTitle;
    private TextView txtOverview;
    private ImageView favoriteStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieRoomRepository = new MovieRoomRepository(getApplication());

        favoriteStar = findViewById(R.id.favorite_star);

        setupTrailerRecycler();
        setupReviewRecycler();

        Intent intent = getIntent();
        if (intent != null)
            setupIntent(intent);

        setupFavorite(String.valueOf(this.id));
    }

    private void setupReviewRecycler() {
        RecyclerView reviewRecyclerView = findViewById(R.id.rv_reviews);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(linearLayoutManager2);
        reviewRecyclerView.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    private void setupTrailerRecycler() {
        RecyclerView trailerRecyclerView = findViewById(R.id.rv_trailers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);
        trailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        trailerRecyclerView.setAdapter(trailerAdapter);
    }

    void setupFavorite(String id) {

        MovieRoom movieRoom = movieRoomRepository.findById(id);

        if (movieRoom != null) {
            favoriteStar.setImageResource(R.drawable.favorite_star);
            isFavorite = true;
        } else {
            favoriteStar.setImageResource(R.drawable.unfavorite_star);
            isFavorite = false;
        }
    }

    void setupIntent(Intent intent) {

        this.id = intent.getIntExtra("id", -1);

        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String poster = intent.getStringExtra("poster");
        String overview = intent.getStringExtra("overview");
        String release = intent.getStringExtra("release");
        double vote = intent.getDoubleExtra("vote", 0.00);
        String language = intent.getStringExtra("language");

        ImageView imgPoster = findViewById(R.id.poster);
        txtTitle = findViewById(R.id.txtTitle);
        txtLanguage = findViewById(R.id.txtLanguage);
        txtVoteAverage = findViewById(R.id.txtVoteAverage);
        txtReleasedOn = findViewById(R.id.txtReleasedOn);
        txtOverview = findViewById(R.id.txtOverview);

        txtTitle.setText(title);
        txtLanguage.setText(language);
        txtVoteAverage.setText(String.valueOf(vote));
        txtReleasedOn.setText(release);
        txtOverview.setText(overview);

        Glide.with(this).load(MovieAdapter.IMAGE_BASE_URL + poster).into(imgPoster);

        GetMovieDataService getMovieDataService = RetrofitInstance.getRetrofitInstance().create(GetMovieDataService.class);

        Call<TrailerList> call = getMovieDataService.getTrailer(id);

        call.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                trailerAdapter.setTrailers(response.body().getTrailers());
            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {

            }
        });

        Call<ReviewList> call2 = getMovieDataService.getReview(id);

        call2.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                reviewAdapter.setReviews(response.body().getReviews());
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {

            }
        });
    }

    public void setFavoriteOptions(final View view) {

        final MovieRoom movieRoom = new MovieRoom(String.valueOf(this.id), txtTitle.getText().toString(), txtReleasedOn.getText().toString(), "", txtOverview.getText().toString(), txtVoteAverage.getText().toString(), txtLanguage.getText().toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFavorite) {
                    favoriteStar.setImageResource(R.drawable.unfavorite_star);
                    movieRoomRepository.deleteById(movieRoom.getId());
                    isFavorite = false;
                } else {
                    favoriteStar.setImageResource(R.drawable.favorite_star);
                    movieRoomRepository.insert(movieRoom);
                    isFavorite = true;
                }
            }
        });
    }

    @Override
    public void onClick(int position) {

        Trailer trailer = trailerAdapter.getTrailer(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailer.getKey()));
        startActivity(intent);
    }
}
