package com.reiner.greenflix.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reiner.greenflix.R;
import com.reiner.greenflix.adapter.FilmAdapter;
import com.reiner.greenflix.controller.FilmController;
import com.reiner.greenflix.model.Film;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerSearch;
    private ProgressBar progressBar;
    private SearchView searchFilm;
    private FilmAdapter adapter;
    private FilmController controller;
    private ArrayList<Film> allFilms = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        controller = new FilmController(getContext());
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        progressBar = view.findViewById(R.id.progressBar);
        searchFilm = view.findViewById(R.id.searchFilm);

        recyclerSearch.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerSearch.setHasFixedSize(true);

        loadAllFilms();
        setupSearch();

        return view;
    }

    private void loadAllFilms() {
        progressBar.setVisibility(View.VISIBLE);
        controller.getFilms(new FilmController.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Film> films) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                allFilms = films;
                // Optional: show all films initially or keep empty
            }

            @Override
            public void onError(String errorMessage) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterFilm(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFilm(newText);
                return true;
            }
        });
    }

    private void filterFilm(String text) {
        if (text.isEmpty()) {
            recyclerSearch.setAdapter(null);
            return;
        }

        ArrayList<Film> filteredList = new ArrayList<>();
        for (Film film : allFilms) {
            if (film.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                film.getGenre().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(film);
            }
        }
        
        adapter = new FilmAdapter(getContext(), filteredList);
        recyclerSearch.setAdapter(adapter);
    }
}