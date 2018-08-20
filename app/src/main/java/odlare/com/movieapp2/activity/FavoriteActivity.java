package odlare.com.movieapp2.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import odlare.com.movieapp2.R;
import odlare.com.movieapp2.adapter.FavoriteAdapter;
import odlare.com.movieapp2.model.RoomEntity.MovieRoom;
import odlare.com.movieapp2.viewmodel.MovieRoomViewModel;

public class FavoriteActivity extends AppCompatActivity {

    private FavoriteAdapter favoriteAdapter;
    private TextView noFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        noFavorites = findViewById(R.id.no_favorites);

        final RecyclerView recyclerView = findViewById(R.id.rv_favorite);
        favoriteAdapter = new FavoriteAdapter(new ArrayList<MovieRoom>());
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MovieRoomViewModel movieRoomViewModel = ViewModelProviders.of(this).get(MovieRoomViewModel.class);

        LiveData<List<MovieRoom>> movies = movieRoomViewModel.findAll();

        movies.observe(this, new Observer<List<MovieRoom>>() {
            @Override
            public void onChanged(@Nullable List<MovieRoom> movieRooms) {

                if (movieRooms.isEmpty()) {
                    noFavorites.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    noFavorites.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                favoriteAdapter.setMovieRooms(movieRooms);
            }
        });
    }
}
