package com.reiner.greenflix.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.reiner.greenflix.R;
import com.reiner.greenflix.adapter.FilmAdapter;
import com.reiner.greenflix.controller.FilmController;
import com.reiner.greenflix.model.Film;

import java.util.ArrayList;
import java.util.Random;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerFilm;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private FilmAdapter adapter;
    private ImageView imgBanner;
    private TextView txtBannerTitle, txtBannerGenre;
    private MaterialCardView cardBanner;
    private FilmController controller;
    private Film bannerFilm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        controller = new FilmController(getContext());
        
        imgBanner = view.findViewById(R.id.imgBanner);
        txtBannerTitle = view.findViewById(R.id.txtBannerTitle);
        txtBannerGenre = view.findViewById(R.id.txtBannerGenre);
        cardBanner = view.findViewById(R.id.cardBanner);
        recyclerFilm = view.findViewById(R.id.recyclerFilm);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        recyclerFilm.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerFilm.setHasFixedSize(false);

        cardBanner.setOnClickListener(v -> {
            if (bannerFilm != null) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", bannerFilm.getId());
                intent.putExtra("title", bannerFilm.getTitle());
                intent.putExtra("genre", bannerFilm.getGenre());
                intent.putExtra("desc", bannerFilm.getDescription());
                intent.putExtra("image", bannerFilm.getImage());
                intent.putExtra("cover", bannerFilm.getCoverImage());
                intent.putExtra("rating", bannerFilm.getRating());
                intent.putExtra("trailer", bannerFilm.getTrailer());
                startActivity(intent);
            }
        });

        setupRefresh();
        getFilmData();

        return view;
    }

    private void setupRefresh() {
        swipeRefresh.setOnRefreshListener(this::getFilmData);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_green_light);
    }

    private void getFilmData() {
        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        controller.getFilms(new FilmController.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Film> films) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);

                if (films != null && !films.isEmpty()) {
                    adapter = new FilmAdapter(getContext(), films);
                    recyclerFilm.setAdapter(adapter);
                    setBannerMovie(films);
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBannerMovie(ArrayList<Film> films) {
        if (films == null || films.isEmpty()) return;

        Random random = new Random();
        bannerFilm = films.get(random.nextInt(films.size()));

        txtBannerTitle.setText(bannerFilm.getTitle());
        txtBannerGenre.setText(bannerFilm.getGenre());

        Glide.with(this)
                .load(bannerFilm.getCoverImage() != null && !bannerFilm.getCoverImage().isEmpty() ? bannerFilm.getCoverImage() : bannerFilm.getImage())
                .thumbnail(0.2f)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(imgBanner);
    }
}