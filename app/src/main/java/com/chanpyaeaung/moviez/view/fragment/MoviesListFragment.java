package com.chanpyaeaung.moviez.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chanpyaeaung.moviez.R;
import com.chanpyaeaung.moviez.api.ServiceGenerator;
import com.chanpyaeaung.moviez.helpers.Keys;
import com.chanpyaeaung.moviez.model.detail.MovieDetail;
import com.chanpyaeaung.moviez.model.movie.MovieModel;
import com.chanpyaeaung.moviez.model.movie.Result;
import com.chanpyaeaung.moviez.view.MovieListView;
import com.chanpyaeaung.moviez.view.activity.MainActivity;
import com.chanpyaeaung.moviez.view.adapter.MovieListAdapter;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<MovieModel> movielist;

    final CharSequence[] sortBy = { " Release Date ", " Popularity" };

    private int page = 1;
    private boolean isMoreResult = false, isLoading = false;

    private int type = 0;
    AlertDialog mDialog;

    private GridLayoutManager mLayoutManager;


    private MovieListAdapter movieListAdapter;

    public MoviesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sort:
                        showSortDialog();
                        return true;
                    default:
                        return true;
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                movielist = null;
                movieListAdapter.setMovieModels(new ArrayList<MovieModel>());
                getMoviesByType();
            }
        });

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        getMoviesByType();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                showSortDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMoviesByType() {
        if(type == 0) {
            getMoviesByReleaseDate();
        } else {
            getMovies();
        }
    }

    private void showSortDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("Sort movies by");
        builder.setSingleChoiceItems(sortBy, type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                type = item;
                page = 1;
                recyclerView.scrollToPosition(0);
                movielist = null;
                movieListAdapter.setMovieModels(new ArrayList<MovieModel>());
                if (item == 0) {

                    getMoviesByReleaseDate();
                } else {
                    getMovies();
                }

                mDialog.dismiss();
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void renderMovieList(List<MovieModel> movieModels) {
        if(movielist == null) {
            movielist = movieModels;
            movieListAdapter.setMovieModels(movielist);
        } else {
            movieListAdapter.addMore(movieModels);
        }

    }

    @Override
    public void showLoading() {
        isLoading = true;
        if (movielist == null) {
            progressWheel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        error.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        isLoading = false;
        recyclerView.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        progressWheel.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        isLoading = false;
        isMoreResult = false;
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
        recyclerView.setOnScrollListener(new ScrollListener());
        movieListAdapter.setClickListener(new MovieListAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setHasOptionsMenu(false);
                MovieDetailFragment frag = new MovieDetailFragment();
                frag.setMovieId(movieListAdapter.getItem(position).getId());
                ((MainActivity)getActivity()).setFragment(frag);
            }
        });
    }

    private void getMovies() {
        ServiceGenerator.getMovieService().getMovies(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(Result result) {
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if(page < result.getTotalPages()) {
                            isMoreResult = true;
                        } else {
                            isMoreResult = false;
                        }

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

    private void getMoviesByReleaseDate() {
        Date now = new Date();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(now);
        ServiceGenerator.getMovieService().getMoviesWithPrimaryReleaseDate(today, Keys.RELEASE_DATE_DESC, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(Result result) {
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if(page < result.getTotalPages()) {
                            isMoreResult = true;
                        } else {
                            isMoreResult = false;
                        }

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


    private class ScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(isMoreResult && !isLoading) {
                int numberofVisibleItems = recyclerView.getChildCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if(numberofVisibleItems + firstVisibleItem >= movieListAdapter.getItemCount()) {
                    page++;
                    getMoviesByType();
                }
            }

        }
    }
}
