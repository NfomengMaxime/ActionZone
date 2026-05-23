package com.actionzone.app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageAffiche;
    private TextView textTitre;
    private TextView textSynopsis;
    private TextView textNoteTmdb;
    private TextView textDateSortie;
    private Spinner spinnerStatut;
    private RatingBar ratingBarPerso;
    private EditText editTextAvis;
    private TextView textDateVisionnage;
    private Button buttonDateVisionnage;
    private Button buttonAjouter;

    private DatabaseHelper databaseHelper;
    private Film film;
    private String dateVisionnageChoisie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        imageAffiche = findViewById(R.id.imageAfficheDetail);
        textTitre = findViewById(R.id.textTitreDetail);
        textSynopsis = findViewById(R.id.textSynopsisDetail);
        textNoteTmdb = findViewById(R.id.textNoteTmdbDetail);
        textDateSortie = findViewById(R.id.textDateSortieDetail);
        spinnerStatut = findViewById(R.id.spinnerStatut);
        ratingBarPerso = findViewById(R.id.ratingBarPerso);
        editTextAvis = findViewById(R.id.editTextAvis);
        textDateVisionnage = findViewById(R.id.textDateVisionnage);
        buttonDateVisionnage = findViewById(R.id.buttonDateVisionnage);
        buttonAjouter = findViewById(R.id.buttonAjouter);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        databaseHelper = new DatabaseHelper(this);

        int tmdbId = getIntent().getIntExtra("tmdbId", 0);
        String titre = getIntent().getStringExtra("titre");
        String synopsis = getIntent().getStringExtra("synopsis");
        String affiche = getIntent().getStringExtra("affiche");
        double noteTmdb = getIntent().getDoubleExtra("noteTmdb", 0.0);
        String dateSortie = getIntent().getStringExtra("dateSortie");

        film = new Film(tmdbId, titre, synopsis, affiche, noteTmdb, dateSortie);

        textTitre.setText(titre);
        textSynopsis.setText(synopsis);
        textNoteTmdb.setText("Note TMDB :  " + String.format("%.1f", noteTmdb) + " / 10");
        textDateSortie.setText("Date de sortie : " + dateSortie);

        if (affiche != null && !affiche.isEmpty()) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + affiche)
                    .placeholder(R.drawable.affiche_defaut)
                    .error(R.drawable.affiche_defaut)
                    .into(imageAffiche);
        }

        String[] statuts = {"à voir", "en cours", "vu"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statuts);
        adapterSpinner.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerStatut.setAdapter(adapterSpinner);

        buttonDateVisionnage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int annee = calendar.get(Calendar.YEAR);
                int mois = calendar.get(Calendar.MONTH);
                int jour = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DetailActivity.this,
                        (view, anneeChoisie, moisChoisi, jourChoisi) -> {
                            dateVisionnageChoisie = jourChoisi + "/" +
                                    (moisChoisi + 1) + "/" + anneeChoisie;
                            textDateVisionnage.setText(
                                    "Date choisie : " + dateVisionnageChoisie);
                        },
                        annee, mois, jour
                );
                datePickerDialog.show();
            }
        });

        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterALaCollection();
            }
        });

        if (databaseHelper.filmExiste(tmdbId)) {
            buttonAjouter.setText("Déjà dans ta collection");
            buttonAjouter.setEnabled(false);
        }
    }

    private void ajouterALaCollection() {
        String statutChoisi = spinnerStatut.getSelectedItem().toString();
        float notePerso = ratingBarPerso.getRating();
        String avis = editTextAvis.getText().toString().trim();

        film.setStatut(statutChoisi);
        film.setNotePerso(notePerso);
        film.setAvis(avis);
        film.setDateVisionnage(dateVisionnageChoisie);

        long id = databaseHelper.ajouterFilm(film);

        if (id != -1) {
            Toast.makeText(this,
                    film.getTitre() + " ajouté à ta collection !",
                    Toast.LENGTH_SHORT).show();
            buttonAjouter.setText("Déjà dans ta collection");
            buttonAjouter.setEnabled(false);
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("dateVisionnage", dateVisionnageChoisie);
        if (editTextAvis != null)
            outState.putString("avis", editTextAvis.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dateVisionnageChoisie = savedInstanceState.getString("dateVisionnage", "");
        String avis = savedInstanceState.getString("avis", "");
        if (!dateVisionnageChoisie.isEmpty())
            textDateVisionnage.setText("Date choisie : " + dateVisionnageChoisie);
        if (!avis.isEmpty())
            editTextAvis.setText(avis);
    }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    protected void onResume() { super.onResume(); }
}