package com.reiner.greenflix.controller;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.reiner.greenflix.model.Film;
import com.reiner.greenflix.network.VolleySingleton;
import com.reiner.greenflix.utils.ApiConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilmController {

    private final Context context;
    private final String BASE_URL = "https://68ff8dfbe02b16d1753e765d.mockapi.io/film";

    public FilmController(Context context) {
        this.context = context;
    }

    public interface DataCallback {
        void onSuccess(ArrayList<Film> films);
        void onError(String errorMessage);
    }

    public interface SingleDataCallback {
        void onSuccess(Film film);
        void onError(String errorMessage);
    }

    public void getFilms(DataCallback callback) {
        ArrayList<Film> filmList = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            Film film = new Film(
                                    obj.optString("id"),
                                    obj.optString("judul"),
                                    obj.optString("kategori"),
                                    obj.optString("gambar_poster"),
                                    obj.optString("gambar_sampul"),
                                    obj.optString("ringkasan"),
                                    obj.optString("skor_rating"),
                                    obj.optString("url_trailer")
                            );
                            filmList.add(film);
                        }
                        callback.onSuccess(filmList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("Gagal memproses data");
                    }
                },
                error -> callback.onError("Gagal mengambil data dari server")
        );

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void addFilm(Film film, SingleDataCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("judul", film.getTitle());
            postData.put("kategori", film.getGenre());
            postData.put("gambar_poster", film.getImage());
            postData.put("gambar_sampul", film.getCoverImage());
            postData.put("ringkasan", film.getDescription());
            postData.put("skor_rating", film.getRating());
            postData.put("url_trailer", film.getTrailer());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                postData,
                response -> {
                    Film newFilm = new Film(
                            response.optString("id"),
                            response.optString("judul"),
                            response.optString("kategori"),
                            response.optString("gambar_poster"),
                            response.optString("gambar_sampul"),
                            response.optString("ringkasan"),
                            response.optString("skor_rating"),
                            response.optString("url_trailer")
                    );
                    callback.onSuccess(newFilm);
                },
                error -> callback.onError("Gagal menambah film")
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void updateFilm(String id, Film film, SingleDataCallback callback) {
        JSONObject putData = new JSONObject();
        try {
            putData.put("judul", film.getTitle());
            putData.put("kategori", film.getGenre());
            putData.put("gambar_poster", film.getImage());
            putData.put("gambar_sampul", film.getCoverImage());
            putData.put("ringkasan", film.getDescription());
            putData.put("skor_rating", film.getRating());
            putData.put("url_trailer", film.getTrailer());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                BASE_URL + "/" + id,
                putData,
                response -> {
                    Film updatedFilm = new Film(
                            response.optString("id"),
                            response.optString("judul"),
                            response.optString("kategori"),
                            response.optString("gambar_poster"),
                            response.optString("gambar_sampul"),
                            response.optString("ringkasan"),
                            response.optString("skor_rating"),
                            response.optString("url_trailer")
                    );
                    callback.onSuccess(updatedFilm);
                },
                error -> callback.onError("Gagal memperbarui film")
        );

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}