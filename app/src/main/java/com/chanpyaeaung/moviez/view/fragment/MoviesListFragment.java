package com.chanpyaeaung.moviez.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chanpyaeaung.moviez.R;
import com.chanpyaeaung.moviez.api.ServiceGenerator;
import com.chanpyaeaung.moviez.model.movie.MovieModel;
import com.chanpyaeaung.moviez.model.movie.Result;
import com.chanpyaeaung.moviez.view.MovieListView;
import com.chanpyaeaung.moviez.view.adapter.MovieListAdapter;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Chan Pyae Aung on 4/3/17.
 */

public class MoviesListFragment extends Fragment implements MovieListView {


    @BindView(R.id.recycler_view_movies)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressWheel progressWheel;
    @BindView(R.id.error)
    TextView error;


    private RecyclerView.LayoutManager mLayoutManager;


    private MovieListAdapter movieListAdapter;

    public MoviesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        getMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void renderMovieList(List<MovieModel> movieModels) {
        movieListAdapter.setMovieModels(movieModels);
    }

    @Override
    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        recyclerView.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        error.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
        error.setText(message);
    }

    @Override
    public Context context() {
        return getActivity();
    }


    private void initUI() {

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieListAdapter = new MovieListAdapter();
        recyclerView.setAdapter(movieListAdapter);
        recyclerView.setVisibility(View.GONE);

    }

    private void getMovies() {
        ServiceGenerator.getMovieService().getMovies(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(Result result) {
                        renderMovieList(result.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError("Something went wrong caused by " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

}
