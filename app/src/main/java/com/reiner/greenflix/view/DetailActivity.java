package com.reiner.greenflix.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.reiner.greenflix.R;
import com.reiner.greenflix.model.Film;
import com.reiner.greenflix.utils.FavoriteManager;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgPoster;
    private TextView txtTitle, txtGenre, txtDesc, txtRating;
    private MaterialButton btnTrailer;
    private FloatingActionButton fabFavorite;
    private Toolbar toolbar;
    private String trailerUrl;
    private FavoriteManager favoriteManager;
    private Film currentFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Membuat activity full screen dan transparan pada status bar
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        
        setContentView(R.layout.activity_detail);

        favoriteManager = new FavoriteManager(this);
        initView();
        setupToolbar();
        getData();
        setupFavorite();
    }

    private void initView() {
        imgPoster = findViewById(R.id.imgPoster);
        txtTitle = findViewById(R.id.txtTitle);
        txtGenre = findViewById(R.id.txtGenre);
        txtDesc = findViewById(R.id.txtDesc);
        txtRating = findViewById(R.id.txtRating);
        btnTrailer = findViewById(R.id.btnTrailer);
        fabFavorite = findViewById(R.id.fabFavorite);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        // Mengatur margin top toolbar agar tidak tertutup status bar jika diperlukan
        // Namun dengan FLAG_LAYOUT_NO_LIMITS, kita biasanya mengatur padding di layout
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String genre = intent.getStringExtra("genre");
        String desc = intent.getStringExtra("desc");
        String image = intent.getStringExtra("image");
        String cover = intent.getStringExtra("cover");
        String rating = intent.getStringExtra("rating");
        trailerUrl = intent.getStringExtra("trailer");

        currentFilm = new Film(id, title, genre, image, cover, desc, rating, trailerUrl);

        txtTitle.setText(title);
        txtGenre.setText(genre);
        txtDesc.setText(desc);
        txtRating.setText(rating);

        String displayImage = (cover != null && !cover.isEmpty()) ? cover : image;

        Glide.with(this)
                .load(displayImage)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgPoster);

        btnTrailer.setOnClickListener(v -> {
            if (trailerUrl != null && !trailerUrl.isEmpty()) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                startActivity(i);
            }
        });
    }

    private void setupFavorite() {
        updateFavoriteIcon();

        fabFavorite.setOnClickListener(v -> {
            if (favoriteManager.isFavorite(currentFilm.getId())) {
                favoriteManager.removeFavorite(currentFilm.getId());
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                favoriteManager.addFavorite(currentFilm);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon();
        });
    }

    private void updateFavoriteIcon() {
        if (favoriteManager.isFavorite(currentFilm.getId())) {
            fabFavorite.setImageResource(R.drawable.ic_favorite);
            fabFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.primaryGreen)));
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite);
            fabFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.textGrey)));
        }
    }
}