package com.chanpyaeaung.moviez.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setMovieModels(List<MovieModel> movies) {
        if(movies == null) {
            throw new IllegalArgumentException("List can't be null");
        }
        movieModels = movies;
        notifyDataSetChanged();
    }

    public void addMore(List<MovieModel> movies) {
        movieModels.addAll(movies);
        notifyItemRangeInserted(movieModels.size() - movies.size(), movies.size());
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


    public MovieModel getItem(int position) {
        return movieModels.get(position);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivPoster)
        ImageView ivPoster;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvPopularity)
        TextView tvPopularity;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(MovieModel movieModel) {
            Glide.with(ivPoster.getContext())
                    .load(Api.PHOTO_URL + movieModel.getPosterPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(ivPoster);
            tvTitle.setText(movieModel.getTitle());
            int popularity = movieModel.getPopularity().intValue();
            tvPopularity.setText(String.valueOf(popularity));

        }

        @Override
        public void onClick(View view) {
            if(clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}
