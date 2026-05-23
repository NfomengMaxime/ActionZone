package com.actionzone.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Déclaration des variables
    private RecyclerView recyclerView;
    private TextView textCollectionVide;
    private FloatingActionButton fabRecherche;
    private DatabaseHelper databaseHelper;
    private List<Film> listeFilms;
    private FilmAdapter filmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  relie les variables Java aux éléments XML via leur id
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewCollection);
        textCollectionVide = findViewById(R.id.textCollectionVide);
        fabRecherche = findViewById(R.id.fabRecherche);

        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        chargerCollection();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (listeFilms != null) {
            outState.putInt("nombreFilms", listeFilms.size());
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int nombreFilms = savedInstanceState.getInt("nombreFilms", 0);
    }

    // Méthode qui charge les films depuis SQLite et les affiche
    private void chargerCollection() {

        listeFilms = databaseHelper.getTousLesFilms();

        if (listeFilms.isEmpty()) {

            recyclerView.setVisibility(View.GONE);
            textCollectionVide.setVisibility(View.VISIBLE);
        } else {

            recyclerView.setVisibility(View.VISIBLE);
            textCollectionVide.setVisibility(View.GONE);


            filmAdapter = new FilmAdapter(this, listeFilms);
            recyclerView.setAdapter(filmAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_parametres) {
            Intent intent = new Intent(MainActivity.this, ParametresActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}