package com.reiner.greenflix.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
    private MaterialButton btnTrailer, btnUpdate;
    private FloatingActionButton fabFavorite;
    private Toolbar toolbar;
    private String trailerUrl;
    private FavoriteManager favoriteManager;
    private Film currentFilm;
    private static final int UPDATE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        
        setContentView(R.layout.activity_detail);

        favoriteManager = new FavoriteManager(this);
        initView();
        setupToolbar();
        getData();
        setupFavorite();
        
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
            intent.putExtra("id", currentFilm.getId());
            intent.putExtra("title", currentFilm.getTitle());
            intent.putExtra("genre", currentFilm.getGenre());
            intent.putExtra("image", currentFilm.getImage());
            intent.putExtra("cover", currentFilm.getCoverImage());
            intent.putExtra("rating", currentFilm.getRating());
            intent.putExtra("trailer", currentFilm.getTrailer());
            intent.putExtra("desc", currentFilm.getDescription());
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });
    }

    private void initView() {
        imgPoster = findViewById(R.id.imgPoster);
        txtTitle = findViewById(R.id.txtTitle);
        txtGenre = findViewById(R.id.txtGenre);
        txtDesc = findViewById(R.id.txtDesc);
        txtRating = findViewById(R.id.txtRating);
        btnTrailer = findViewById(R.id.btnTrailer);
        btnUpdate = findViewById(R.id.btnUpdate);
        fabFavorite = findViewById(R.id.fabFavorite);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
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

        displayData();
    }

    private void displayData() {
        txtTitle.setText(currentFilm.getTitle());
        txtGenre.setText(currentFilm.getGenre());
        txtDesc.setText(currentFilm.getDescription());
        txtRating.setText(currentFilm.getRating());

        String displayImage = (currentFilm.getCoverImage() != null && !currentFilm.getCoverImage().isEmpty()) 
                ? currentFilm.getCoverImage() : currentFilm.getImage();

        Glide.with(this)
                .load(displayImage)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgPoster);

        btnTrailer.setOnClickListener(v -> {
            if (currentFilm.getTrailer() != null && !currentFilm.getTrailer().isEmpty()) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(currentFilm.getTrailer()));
                startActivity(i);
            } else {
                Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFavorite() {
        updateFavoriteIcon();

        fabFavorite.setOnClickListener(v -> {
            if (favoriteManager.isFavorite(currentFilm.getId())) {
                favoriteManager.removeFavorite(currentFilm.getId());
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            } else {
                favoriteManager.addFavorite(currentFilm);
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh data could be complex with MockAPI latency, 
            // but for simple cases we just finish and let the user go back or we can show a refresh toast.
            Toast.makeText(this, "Please refresh list to see changes", Toast.LENGTH_LONG).show();
            finish(); // Go back to list after update
        }
    }
}