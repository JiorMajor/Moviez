package com.chanpyaeaung.moviez.view.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chanpyaeaung.moviez.R;
import com.chanpyaeaung.moviez.api.Api;
import com.chanpyaeaung.moviez.api.MovieService;
import com.chanpyaeaung.moviez.api.ServiceGenerator;
import com.chanpyaeaung.moviez.helpers.Keys;
import com.chanpyaeaung.moviez.model.detail.MovieDetail;
import com.chanpyaeaung.moviez.view.AlphaColorSpan;
import com.chanpyaeaung.moviez.view.MovieDetailView;
import com.chanpyaeaung.moviez.view.ObservableScrollView;
import com.chanpyaeaung.moviez.view.activity.MainActivity;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements ObservableScrollView.ScrollViewListener, MovieDetailView {


    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.fl_backdrop_container)
    FrameLayout backdropContainer;
    @BindView(R.id.fl_thumbnail_container)
    FrameLayout thumbnailContainer;
    @BindView(R.id.iv_backdrop)
    ImageView ivBackdrop;
    @BindView(R.id.iv_movie_image)
    ImageView ivPoster;
    @BindView(R.id.tvMovieTitle)
    TextView tvMovieTitle;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvRuntimenGenre)
    TextView tvRuntimenGenre;
    @BindView(R.id.tv_vote)
    TextView tvVote;
    @BindView(R.id.tv_overview)
    TextView tvOverview;
    @BindView(R.id.cardBuy)
    CardView cardBuy;
    @BindView(R.id.rb_rating)
    AppCompatRatingBar ratingBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressWheel progressWheel;
    @BindView(R.id.error)
    TextView error;


    private boolean mFabIsShown;
    private int movieId;
    private AppCompatActivity mActivity;
    private int mActionBarTitleColor;
    private AlphaColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;

    private int height;

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        height = calculateHeight(mActivity.getWindowManager());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View detailView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, detailView);

        ViewHelper.setScaleX(thumbnailContainer, 1);
        ViewHelper.setScaleY(thumbnailContainer, 1);

        mActivity.setSupportActionBar(toolbar);

        if(mActivity.getSupportActionBar() != null) {
            ActionBar mActionBar = mActivity.getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle("");
        }

        mActionBarTitleColor = getResources().getColor(R.color.colorPureWhite);
        mAlphaForegroundColorSpan = new AlphaColorSpan(mActionBarTitleColor);
        mSpannableString = new SpannableString("Details");
        getMovieDetails(movieId);
        ivBackdrop.setImageDrawable(getResources().getDrawable(R.drawable.cover));
        scrollView.setScrollViewListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        customRatingBar();


        cardBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebFragment frag = new WebFragment();
                frag.setUrl(Api.CINEMA_PAGE);
                frag.setTitle("Buy Ticket");
                ((MainActivity)getActivity()).setFragment(frag);
            }
        });
        return detailView;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int y, int oldy) {
        float py = y * .5f;
        toolbarFading(scrollView.getScrollY());
        ivBackdrop.setTop((int) py < 0 ? 0 : (int) py);
        int headerHeight = ivBackdrop.getHeight() - toolbar.getHeight();
        float ratio = 0;
        if (oldy > 0 && headerHeight > 0) {
            ratio = (float) Math.min(Math.max(oldy, 0), headerHeight) / headerHeight;
        }
        setTitleAlpha(clamp(5.0F *py  - 4.0F, 0.0F, 1.0F));
        if (y > ratio) {
            hideFab();
        } else {
            showFab();
        }
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(thumbnailContainer).cancel();
            ViewPropertyAnimator.animate(thumbnailContainer).scaleX(1).scaleY(1).setDuration(300).start();

            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(thumbnailContainer).cancel();
            ViewPropertyAnimator.animate(thumbnailContainer).scaleX(0).scaleY(0).setDuration(300).start();
            mFabIsShown = false;
        }
    }

    private void toolbarFading(int scrollY) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / height);
        toolbar.setBackgroundColor(getColorWithAlpha(alpha, baseColor));

        ViewHelper.setTranslationY(ivBackdrop, scrollY / 2);
    }

    private int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(mSpannableString);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    @Override
    public void renderDetail(MovieDetail movieDetail) {

        try {
            String title = movieDetail.getOriginalTitle();
            String runtime = null;
            if (movieDetail.getRuntime() > 0) {
                runtime = formatMinute(movieDetail.getRuntime());
            }
            String genre = null;
            if (movieDetail.getGenres() != null) {
                if (movieDetail.getGenres().size() > 0) {
                    genre = movieDetail.getGenres().get(0).getName();
                }
            }
            String language = movieDetail.getOriginalLanguage();


            //title
            if (title != null ) {
                mSpannableString = new SpannableString(title);
                tvMovieTitle.setText(title);
            } else {
                tvMovieTitle.setVisibility(View.GONE);
            }

            //language
            if (language != null) {
                tvLanguage.setText(language);
            } else {
                tvLanguage.setVisibility(View.GONE);
            }

            //runtime and genre
            if (runtime != null || genre != null ) {
                if(runtime == null) {
                    tvRuntimenGenre.setText(genre);
                } else if(genre == null) {
                    tvRuntimenGenre.setText(runtime);
                } else {
                    tvRuntimenGenre.setText(runtime + ", " + genre);
                }
            } else {
                tvRuntimenGenre.setVisibility(View.GONE);
            }

            //rating and votes
            ratingBar.setRating((float) movieDetail.getVoteAverage());
            tvVote.setText("Total Votes - "+ String.valueOf(movieDetail.getVoteCount()));

            if (movieDetail.getOverview() != null) {
                tvOverview.setText(movieDetail.getOverview());
            } else {
                tvOverview.setText("Nothing special here.");
            }

            // cover
            Glide.with(mActivity)
                    .load(Api.BACKDROP_URL + movieDetail.getBackdropPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.cover)
                    .crossFade()
                    .into(ivBackdrop);


            //poster
            Glide.with(mActivity)
                    .load(Api.PHOTO_URL + movieDetail.getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(ivPoster);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Something went wrong by "+ e.getLocalizedMessage());
        }


    }

    @Override
    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        progressWheel.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        error.setText(message);
    }

    @Override
    public Context context() {
        return getActivity();
    }

    private void customRatingBar() {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setProgressDrawable(stars);
    }

    private void getMovieDetails(int id) {
        ServiceGenerator.getMovieService().getMovieDetail(id, Keys.EN_US)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(MovieDetail movieDetail) {

                        renderDetail(movieDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError("Something went wrong caused by "+ e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    private int calculateHeight(WindowManager w) {
        int measuredWidth;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
        }
        return (int) (measuredWidth / 1.618);
    }


    private String formatMinute(int time) {

        int hour = time/60;
        int min = time % 60;

        if(hour <= 0) {
            return String.valueOf(min) + " min";
        } else {
            return String.valueOf(hour) + " hour " + String.valueOf(min) + " min";
        }

    }

}
