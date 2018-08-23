package odlare.com.movieapp2.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import odlare.com.movieapp2.R;
import odlare.com.movieapp2.adapter.MovieAdapter;
import odlare.com.movieapp2.model.Movie;
import odlare.com.movieapp2.model.MovieList;
import odlare.com.movieapp2.model.RoomEntity.MovieRoom;
import odlare.com.movieapp2.network.GetMovieDataService;
import odlare.com.movieapp2.network.RetrofitInstance;
import odlare.com.movieapp2.viewmodel.MovieRoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TOP_RATED = "TOP_RATED";
    private static final String POPULAR = "POPULAR";
    private static final String FAVORITE = "FAVORITE";

    private String action = TOP_RATED;

    private GetMovieDataService getMovieDataService;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_movie_list);



        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        recyclerView.setAdapter(movieAdapter);
        getMovieDataService = RetrofitInstance.getRetrofitInstance().create(GetMovieDataService.class);

        if (savedInstanceState != null) {

            this.action = savedInstanceState.getString("action");
            this.state = savedInstanceState.getParcelable("state");

            if (this.action.equals(FAVORITE)) {
                retrieveFavoritesFromRoom();
            } else if (this.action.equals(TOP_RATED)) {
                retrieveTopRatedFromNetwork();
            } else if (this.action.equals(POPULAR)) {
                retrievePopularFromNetwork();
            }

        } else {

            retrieveTopRatedFromNetwork();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.action.equals(FAVORITE)) {
            retrieveFavoritesFromRoom();
        }
        recyclerView.getLayoutManager().onRestoreInstanceState(this.state);
    }

    @Override
    public void onClick(int position) {

        Movie movie = movieAdapter.getMovie(position);

        Intent intent = new Intent(this, MovieDetailActivity.class);

        intent.putExtra("id", movie.getId());
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("poster", movie.getPosterPath());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("release", movie.getReleaseDate());
        intent.putExtra("vote", movie.getVoteAverage());
        intent.putExtra("language", movie.getOriginalLanguage());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelected = item.getItemId();

        switch (itemSelected) {

            case R.id.top_rated_menu:
                retrieveTopRatedFromNetwork();
                this.action = TOP_RATED;
                resetRecyclerScrollPosition();
                return true;
            case R.id.popular_menu:
                retrievePopularFromNetwork();
                this.action = POPULAR;
                resetRecyclerScrollPosition();
                return true;
            case R.id.favorites_menu:
                retrieveFavoritesFromRoom();
                this.action = FAVORITE;
                resetRecyclerScrollPosition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("action", this.action);
        Parcelable state = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("state", state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.action = savedInstanceState.getString("action");
        this.state = savedInstanceState.getParcelable("state");
    }

    private void retrieveFavoritesFromRoom() {
        MovieRoomViewModel movieRoomViewModel = ViewModelProviders.of(this).get(MovieRoomViewModel.class);
        movieRoomViewModel.getMovies().observe(this, new Observer<List<MovieRoom>>() {
            @Override
            public void onChanged(@Nullable List<MovieRoom> movieRooms) {

                List<Movie> movies1 = new ArrayList<>();

                for (MovieRoom movieRoom : movieRooms) {
                    Movie movie = new Movie(0, Integer.valueOf(movieRoom.getId()), false, Double.valueOf(movieRoom.getVote()), movieRoom.getTitle(),
                            0, movieRoom.getPoster(), movieRoom.getLanguage(), null, null, false, movieRoom.getOverview(), movieRoom.getRelease(), null);
                    movies1.add(movie);
                }

                movieAdapter.setMovies(movies1);
                retrieveRecyclerScrollPosition();
            }
        });
        movieRoomViewModel.findAll();
    }

    private void retrievePopularFromNetwork() {
        Call<MovieList> call = getMovieDataService.getMovieByPopular();

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                movieAdapter.setMovies(response.body().getMovies());
                retrieveRecyclerScrollPosition();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Intent intent = new Intent(getBaseContext(), MovieDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveTopRatedFromNetwork() {
        Call<MovieList> call = getMovieDataService.getMovieByTopRated();
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                movieAdapter.setMovies(response.body().getMovies());
                retrieveRecyclerScrollPosition();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Intent intent = new Intent(getBaseContext(), MovieDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveRecyclerScrollPosition(){
        recyclerView.getLayoutManager().onRestoreInstanceState(this.state);
    }

    private void resetRecyclerScrollPosition() {
        this.state = null;
        this.recyclerView.getLayoutManager().scrollToPosition(0);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }
}
