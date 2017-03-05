package com.chanpyaeaung.moviez.view;

import android.content.Context;

/**
 * Created by Chan Pyae Aung on 4/3/17.
 */

public interface LoadDataView {

    /**
     * show a view with a progress bar indicating a loading process.
     */
    void showLoading();


    /**
     * Hide a loading view.
     */
    void hideLoading();

    /**
     * Show an error messge
     *
     * @param message
     */
    void showError(String message);


    /**
     * Get android context
     * @return
     */
    Context context();


}
