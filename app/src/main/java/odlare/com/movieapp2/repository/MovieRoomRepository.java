package odlare.com.movieapp2.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import odlare.com.movieapp2.dao.MovieRoomDAO;
import odlare.com.movieapp2.db.MovieRoomDatabase;
import odlare.com.movieapp2.model.RoomEntity.MovieRoom;

public class MovieRoomRepository {

    private MovieRoomDAO movieRoomDao;
    private LiveData<List<MovieRoom>> movieRooms;

    public MovieRoomRepository(Application application) {
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        this.movieRoomDao = db.movieRoomDAO();
        findAll();
    }

    public void findAll() {
        this.movieRooms = this.movieRoomDao.findAll();
    }

    public LiveData<List<MovieRoom>> getMovieRooms() {
        return movieRooms;
    }

    public void insert(MovieRoom player) {
        new InsertAsyncTask(this.movieRoomDao).execute(player);
    }

    public void deleteById(String id) {
        new DeleteAsyncTask(movieRoomDao).execute(id);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(movieRoomDao).execute();
    }

    public MovieRoom findById(String id) {

        AsyncTask<String, Void, MovieRoom> execute = new FindByIdAsyncTask(movieRoomDao).execute(id);
        try {
            return execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class FindByIdAsyncTask extends AsyncTask<String, Void, MovieRoom> {

        private MovieRoomDAO dao;

        private FindByIdAsyncTask(MovieRoomDAO dao) {
            this.dao = dao;
        }

        @Override
        protected MovieRoom doInBackground(String... strings) {
            MovieRoom byId = dao.findById(strings[0]);
            return byId;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {

        private MovieRoomDAO dao;

        private DeleteAsyncTask(MovieRoomDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... id) {
            dao.deleteById(id[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private MovieRoomDAO dao;

        private DeleteAllAsyncTask(MovieRoomDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<MovieRoom, Void, Void> {

        private MovieRoomDAO dao;

        InsertAsyncTask(MovieRoomDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(MovieRoom... movieRooms) {
            dao.save(movieRooms[0]);
            return null;
        }
    }
}
