package com.chanpyaeaung.moviez.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chanpyaeaung.moviez.R;
import com.chanpyaeaung.moviez.api.Api;
import com.chanpyaeaung.moviez.model.movie.MovieModel;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan Pyae Aung on 4/3/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {


    List<MovieModel> movieModels;

    public void setMovieModels(List<MovieModel> movies) {
        if(movies == null) {
            throw new IllegalArgumentException("List can't be null");
        }
        movieModels = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movieModels.get(position));
    }

    @Override
    public int getItemCount() {
        return movieModels!=null ? movieModels.size() : 0;
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPoster)
        ImageView ivPoster;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvPopularity)
        TextView tvPopularity;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(MovieModel movieModel) {
            Glide.with(ivPoster.getContext())
                    .load(Api.PHOTO_URL + movieModel.getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(ivPoster);
            tvTitle.setText(movieModel.getTitle());
            tvTitle.setVisibility(View.GONE);
            tvPopularity.setText(String.valueOf(movieModel.getVoteAverage()));

        }
    }
}
