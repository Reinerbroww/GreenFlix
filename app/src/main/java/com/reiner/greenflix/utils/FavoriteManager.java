package com.reiner.greenflix.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reiner.greenflix.model.Film;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteManager {

    private static final String PREF_NAME = "greenflix_prefs";
    private static final String KEY_FAVORITES = "favorite_films";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public FavoriteManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void addFavorite(Film film) {
        ArrayList<Film> favorites = getFavorites();
        // Check if already exists
        boolean exists = false;
        for (Film f : favorites) {
            if (f.getId().equals(film.getId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            favorites.add(film);
            saveFavorites(favorites);
        }
    }

    public void removeFavorite(String filmId) {
        ArrayList<Film> favorites = getFavorites();
        favorites.removeIf(film -> film.getId().equals(filmId));
        saveFavorites(favorites);
    }

    public boolean isFavorite(String filmId) {
        ArrayList<Film> favorites = getFavorites();
        for (Film f : favorites) {
            if (f.getId().equals(filmId)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Film> getFavorites() {
        String json = sharedPreferences.getString(KEY_FAVORITES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Film>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveFavorites(ArrayList<Film> favorites) {
        String json = gson.toJson(favorites);
        sharedPreferences.edit().putString(KEY_FAVORITES, json).apply();
    }
}