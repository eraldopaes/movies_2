package odlare.com.movieapp2.model.RoomEntity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieRoom {

    @PrimaryKey(autoGenerate = true)
    private int idMovie;

    private String id;
    private String title;
    private String release;
    private String poster;
    private String overview;
    private String vote;
    private String language;

    @Ignore
    public MovieRoom(int idMovie, String id, String title, String release, String poster, String overview, String vote, String language) {
        this.idMovie = idMovie;
        this.id = id;
        this.title = title;
        this.release = release;
        this.poster = poster;
        this.overview = overview;
        this.vote = vote;
        this.language = language;
    }

    public MovieRoom(String id, String title, String release, String poster, String overview, String vote, String language) {
        this.id = id;
        this.title = title;
        this.release = release;
        this.poster = poster;
        this.overview = overview;
        this.vote = vote;
        this.language = language;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
