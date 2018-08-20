package odlare.com.movieapp2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import odlare.com.movieapp2.model.RoomEntity.MovieRoom;
import odlare.com.movieapp2.repository.MovieRoomRepository;

public class MovieRoomViewModel extends AndroidViewModel {

    private MovieRoomRepository movieRoomRepository;
    private LiveData<List<MovieRoom>> movies;

    public MovieRoomViewModel(@NonNull Application application) {
        super(application);
        this.movieRoomRepository = new MovieRoomRepository(application);
        this.movies = movieRoomRepository.findAll();
    }

    public LiveData<List<MovieRoom>> findAll() {
        return this.movies;
    }

    public void insert(MovieRoom movieRoom) {
        movieRoomRepository.insert(movieRoom);
    }

    public void deleteById(String id) {
        movieRoomRepository.deleteById(id);
    }

    public void deleteAll() {
        movieRoomRepository.deleteAll();
    }

    public MovieRoom findById(String id) {
        return movieRoomRepository.findById(id);
    }
}
