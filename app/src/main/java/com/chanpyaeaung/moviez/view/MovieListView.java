package com.chanpyaeaung.moviez.view;

import com.chanpyaeaung.moviez.model.movie.MovieModel;

import java.util.List;

/**
 * Created by Chan Pyae Aung on 4/3/17.
 */

public interface MovieListView extends LoadDataView {

    void renderMovieList(List<MovieModel> movieModels);

}
