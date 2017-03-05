package com.chanpyaeaung.moviez.api;

import io.reactivex.Observable;

import com.chanpyaeaung.moviez.BuildConfig;
import com.chanpyaeaung.moviez.model.detail.MovieDetail;
import com.chanpyaeaung.moviez.model.movie.Result;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.chanpyaeaung.moviez.api.Api.DISCOVER_MOVIE;
import static com.chanpyaeaung.moviez.api.Api.MOVIE;
import static com.chanpyaeaung.moviez.helpers.Keys.API_KEY;
import static com.chanpyaeaung.moviez.helpers.Keys.PRIMARY_RELEASE_DATE_LTE;
import static com.chanpyaeaung.moviez.helpers.Keys.SORT_BY;
import static com.chanpyaeaung.moviez.helpers.Keys.PAGE;
import static com.chanpyaeaung.moviez.helpers.Keys.LANGUAGE;


/**
 * Created by Chan Pyae Aung on 5/3/17.
 */



public interface MovieService {

    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY)
    Observable<Result> getMovies(@Query(PAGE) int page);

    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY)
    Observable<Result> getMoviesWithPrimaryReleaseDate(@Query(PRIMARY_RELEASE_DATE_LTE) String primaryDate, @Query(SORT_BY) String sortBy, @Query(PAGE) int page);

    @GET(DISCOVER_MOVIE + "?" + API_KEY + "=" + BuildConfig.KEY)
    Observable<Result> getMoviesBySort(@Query(SORT_BY) String sortBy, @Query(PAGE) int page);

    @GET(MOVIE + "/{id}" + "?" + API_KEY + "=" + BuildConfig.KEY)
    Observable<MovieDetail> getMovieDetail(@Path("id") int id, @Query(LANGUAGE) String language);


}
