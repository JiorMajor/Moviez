package com.chanpyaeaung.moviez.view;

import com.chanpyaeaung.moviez.model.detail.MovieDetail;

/**
 * Created by Chan Pyae Aung on 5/3/17.
 */

public interface MovieDetailView extends LoadDataView {

    void renderDetail(MovieDetail movieDetail);

}
