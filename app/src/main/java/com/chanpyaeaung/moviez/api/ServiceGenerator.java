package com.chanpyaeaung.moviez.api;

/**
 * Created by Chan Pyae Aung on 5/3/17.
 */

public class ServiceGenerator {

    private static MovieService movieService;
    public static MovieService getMovieService() {
        if(movieService == null) {
            movieService = RetrofitHelper.getRetrofit().create(MovieService.class);
        }

        return movieService;
    }

}
