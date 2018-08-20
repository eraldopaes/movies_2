package odlare.com.movieapp2.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import odlare.com.movieapp2.model.RoomEntity.MovieRoom;

@Dao
public interface MovieRoomDAO {

    @Insert
    void save(MovieRoom movieRoom);

    @Query("SELECT * FROM movies")
    LiveData<List<MovieRoom>> findAll();

    @Query("SELECT * FROM movies WHERE id =:id limit 1")
    MovieRoom findById(String id);

    @Query("DELETE FROM movies WHERE id =:id")
    void deleteById(String id);

    @Query("DELETE FROM movies")
    void deleteAll();
}
