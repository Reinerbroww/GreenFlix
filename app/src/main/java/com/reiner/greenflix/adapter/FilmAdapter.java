package com.reiner.greenflix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.reiner.greenflix.R;
import com.reiner.greenflix.model.Film;
import com.reiner.greenflix.view.DetailActivity;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Film> filmList;

    public FilmAdapter(Context context, ArrayList<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Film film = filmList.get(position);

        holder.txtTitle.setText(film.getTitle());
        holder.txtGenre.setText(film.getGenre());

        Glide.with(context)
                .load(film.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.imgFilm);

        holder.cardFilm.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", film.getId());
            intent.putExtra("title", film.getTitle());
            intent.putExtra("genre", film.getGenre());
            intent.putExtra("desc", film.getDescription());
            intent.putExtra("image", film.getImage());
            intent.putExtra("cover", film.getCoverImage());
            intent.putExtra("rating", film.getRating());
            intent.putExtra("trailer", film.getTrailer());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filmList != null ? filmList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFilm;
        TextView txtTitle, txtGenre;
        CardView cardFilm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFilm = itemView.findViewById(R.id.imgFilm);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtGenre = itemView.findViewById(R.id.txtGenre);
            cardFilm = itemView.findViewById(R.id.cardFilm);
        }
    }
}