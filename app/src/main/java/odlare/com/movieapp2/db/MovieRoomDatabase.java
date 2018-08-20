package odlare.com.movieapp2.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import odlare.com.movieapp2.dao.MovieRoomDAO;
import odlare.com.movieapp2.model.RoomEntity.MovieRoom;

@Database(entities = {MovieRoom.class}, version = 1)
public abstract class MovieRoomDatabase extends RoomDatabase {

    private static MovieRoomDatabase INSTANCE;

    public static MovieRoomDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    return Room.databaseBuilder(context, MovieRoomDatabase.class, "movie").build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract MovieRoomDAO movieRoomDAO();
}
