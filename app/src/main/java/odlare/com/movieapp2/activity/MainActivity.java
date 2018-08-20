package odlare.com.movieapp2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import odlare.com.movieapp2.R;
import odlare.com.movieapp2.adapter.MovieAdapter;
import odlare.com.movieapp2.model.Movie;
import odlare.com.movieapp2.model.MovieList;
import odlare.com.movieapp2.network.GetMovieDataService;
import odlare.com.movieapp2.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private GetMovieDataService getMovieDataService;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_movie_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        recyclerView.setAdapter(movieAdapter);

        getMovieDataService = RetrofitInstance.getRetrofitInstance().create(GetMovieDataService.class);

        Call<MovieList> call = getMovieDataService.getMovieByTopRated();

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                movieAdapter.setMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Intent intent = new Intent(getBaseContext(), FavoriteActivity.class);
                startActivity(intent);
            }
        });
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

        Call<MovieList> call;

        switch (itemSelected) {

            case R.id.top_rated_menu:

                call = getMovieDataService.getMovieByTopRated();

                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        movieAdapter.setMovies(response.body().getMovies());
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Intent intent = new Intent(getBaseContext(), FavoriteActivity.class);
                        startActivity(intent);
                    }
                });
                return true;
            case R.id.popular_menu:

                call = getMovieDataService.getMovieByPopular();

                call.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        movieAdapter.setMovies(response.body().getMovies());
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        Intent intent = new Intent(getBaseContext(), FavoriteActivity.class);
                        startActivity(intent);
                    }
                });
                return true;
            case R.id.favorites_menu:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
