package com.reiner.greenflix.controller;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.reiner.greenflix.model.Film;
import com.reiner.greenflix.network.VolleySingleton;
import com.reiner.greenflix.utils.ApiConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilmController {

    private final Context context;

    public FilmController(Context context) {
        this.context = context;
    }

    public interface DataCallback {
        void onSuccess(ArrayList<Film> films);
        void onError(String errorMessage);
    }

    public void getFilms(DataCallback callback) {
        ArrayList<Film> filmList = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                ApiConfig.BASE_URL,
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
                error -> {
                    String message = "Gagal mengambil data dari server";
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        message = "Data tidak ditemukan";
                    }
                    callback.onError(message);
                }
        );

        // Menambahkan RetryPolicy untuk mempercepat respon jika terjadi timeout kecil
        // dan menghindari request ganda yang tidak perlu
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 detik timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Memastikan request tidak di-cache jika data sering berubah, 
        // atau biarkan default jika ingin lebih cepat pada pemanggilan kedua
        request.setShouldCache(true);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}