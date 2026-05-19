package com.reiner.greenflix.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reiner.greenflix.R;
import com.reiner.greenflix.adapter.FilmAdapter;
import com.reiner.greenflix.model.Film;
import com.reiner.greenflix.utils.FavoriteManager;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerFavorite;
    private LinearLayout layoutEmpty;
    private FavoriteManager favoriteManager;
    private FilmAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        favoriteManager = new FavoriteManager(getContext());
        recyclerFavorite = view.findViewById(R.id.recyclerFavorite);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);

        recyclerFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerFavorite.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        ArrayList<Film> favoriteFilms = favoriteManager.getFavorites();

        if (favoriteFilms.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            recyclerFavorite.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            recyclerFavorite.setVisibility(View.VISIBLE);
            
            adapter = new FilmAdapter(getContext(), favoriteFilms);
            recyclerFavorite.setAdapter(adapter);
        }
    }
}