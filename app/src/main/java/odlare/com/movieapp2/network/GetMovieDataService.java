package odlare.com.movieapp2.network;

import odlare.com.movieapp2.BuildConfig;
import odlare.com.movieapp2.model.MovieList;
import odlare.com.movieapp2.model.ReviewList;
import odlare.com.movieapp2.model.TrailerList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetMovieDataService {

    @GET("top_rated?api_key=" + BuildConfig.API_KEY_MOVIE)
    Call<MovieList> getMovieByTopRated();

    @GET("popular?api_key=" + BuildConfig.API_KEY_MOVIE)
    Call<MovieList> getMovieByPopular();

    @GET("{id}/videos?api_key=" + BuildConfig.API_KEY_MOVIE)
    Call<TrailerList> getTrailer(@Path("id") int id);

    @GET("{id}/reviews?api_key=" + BuildConfig.API_KEY_MOVIE)
    Call<ReviewList> getReview(@Path("id") int id);
}
