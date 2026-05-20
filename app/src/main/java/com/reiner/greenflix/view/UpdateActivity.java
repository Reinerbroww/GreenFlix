package com.reiner.greenflix.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.reiner.greenflix.R;
import com.reiner.greenflix.controller.FilmController;
import com.reiner.greenflix.model.Film;

public class UpdateActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etGenre, etPosterUrl, etCoverUrl, etRating, etTrailerUrl, etDescription;
    private MaterialButton btnUpdate;
    private FilmController controller;
    private String filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Aktifkan Edge-to-Edge agar tampilan full layar dan modern
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        controller = new FilmController(this);

        initView();
        setupToolbar();
        getIntentData();
    }

    private void initView() {
        etTitle = findViewById(R.id.etTitle);
        etGenre = findViewById(R.id.etGenre);
        etPosterUrl = findViewById(R.id.etPosterUrl);
        etCoverUrl = findViewById(R.id.etCoverUrl);
        etRating = findViewById(R.id.etRating);
        etTrailerUrl = findViewById(R.id.etTrailerUrl);
        etDescription = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdateMovie);

        btnUpdate.setOnClickListener(v -> updateMovie());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        filmId = intent.getStringExtra("id");
        etTitle.setText(intent.getStringExtra("title"));
        etGenre.setText(intent.getStringExtra("genre"));
        etPosterUrl.setText(intent.getStringExtra("image"));
        etCoverUrl.setText(intent.getStringExtra("cover"));
        etRating.setText(intent.getStringExtra("rating"));
        etTrailerUrl.setText(intent.getStringExtra("trailer"));
        etDescription.setText(intent.getStringExtra("desc"));
    }

    private void updateMovie() {
        String title = etTitle.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String poster = etPosterUrl.getText().toString().trim();
        String cover = etCoverUrl.getText().toString().trim();
        String rating = etRating.getText().toString().trim();
        String trailer = etTrailerUrl.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (title.isEmpty() || genre.isEmpty()) {
            Toast.makeText(this, "Title and Genre are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Film updatedFilm = new Film(filmId, title, genre, poster, cover, desc, rating, trailer);

        btnUpdate.setEnabled(false);
        btnUpdate.setText("Updating...");

        controller.updateFilm(filmId, updatedFilm, new FilmController.SingleDataCallback() {
            @Override
            public void onSuccess(Film film) {
                Toast.makeText(UpdateActivity.this, "Movie updated successfully!", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(UpdateActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                btnUpdate.setEnabled(true);
                btnUpdate.setText("UPDATE MOVIE");
            }
        });
    }
}