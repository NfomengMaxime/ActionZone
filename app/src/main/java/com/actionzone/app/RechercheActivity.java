package com.actionzone.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class RechercheActivity extends AppCompatActivity {

    private String CLE_API;

    private EditText editTextRecherche;
    private ProgressBar progressBar;
    private TextView textAucunResultat;
    private RecyclerView recyclerView;
    private List<Film> resultats;
    private ResultatAdapter resultatAdapter;

    private android.os.Handler handler = new android.os.Handler();
    private Runnable rechercheRunnable;
    private static final int DELAI_RECHERCHE = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        Toolbar toolbar = findViewById(R.id.toolbarRecherche);
        editTextRecherche = findViewById(R.id.editTextRecherche);
        progressBar = findViewById(R.id.progressBarRecherche);
        textAucunResultat = findViewById(R.id.textAucunResultat);
        recyclerView = findViewById(R.id.recyclerViewResultats);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Lecture clé API depuis SharedPreferences
        SharedPreferences prefs = getSharedPreferences("actionzone_prefs", MODE_PRIVATE);
        CLE_API = prefs.getString("cle_api_tmdb", "");

        if (CLE_API.isEmpty()) {
            Toast.makeText(this,
                    "Configure ta clé API TMDB dans les Paramètres !",
                    Toast.LENGTH_LONG).show();
        }

        resultats = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String texte = s.toString().trim();

                // On annule la recherche précédente planifiée
                handler.removeCallbacks(rechercheRunnable);

                if (texte.length() >= 2) {

                    rechercheRunnable = new Runnable() {
                        @Override
                        public void run() {
                            lancerRecherche(texte);
                        }
                    };
                    handler.postDelayed(rechercheRunnable, DELAI_RECHERCHE);
                } else if (texte.isEmpty()) {

                    resultats.clear();
                    recyclerView.setVisibility(View.GONE);
                    textAucunResultat.setVisibility(View.GONE);
                }
            }
        });


        editTextRecherche.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String texte = editTextRecherche.getText().toString().trim();
                    if (!texte.isEmpty()) {
                        lancerRecherche(texte);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void lancerRecherche(String query) {
        if (CLE_API.isEmpty()) {
            Toast.makeText(this,
                    "Configure ta clé API dans les Paramètres !",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // On affiche la ProgressBar pendant le chargement
        progressBar.setVisibility(View.VISIBLE);
        textAucunResultat.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Encodage de la query pour l'URL
        String queryEncodee = query.replace(" ", "%20");
        String urlComplete = "https://api.themoviedb.org/3/search/movie?api_key="
                + CLE_API
                + "&language=fr-FR&query="
                + queryEncodee;


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String reponse = appelAPI(urlComplete);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (reponse != null) {
                            traiterReponse(reponse);
                        } else {
                            Toast.makeText(RechercheActivity.this,
                                    "Erreur réseau. Vérifie ta connexion.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private String appelAPI(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            connexion.setConnectTimeout(10000);
            connexion.setReadTimeout(10000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connexion.getInputStream()));
            StringBuilder reponse = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                reponse.append(ligne);
            }
            reader.close();
            connexion.disconnect();
            return reponse.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void traiterReponse(String reponseJson) {
        try {
            resultats.clear();
            JSONObject jsonObject = new JSONObject(reponseJson);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject filmJson = results.getJSONObject(i);
                int tmdbId = filmJson.getInt("id");
                String titre = filmJson.optString("title", "Titre inconnu");
                String synopsis = filmJson.optString("overview", "Pas de synopsis");
                String affiche = filmJson.optString("poster_path", "");
                double noteTmdb = filmJson.optDouble("vote_average", 0.0);
                String dateSortie = filmJson.optString("release_date", "");

                Film film = new Film(tmdbId, titre, synopsis,
                        affiche, noteTmdb, dateSortie);
                resultats.add(film);
            }

            if (resultats.isEmpty()) {
                textAucunResultat.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                textAucunResultat.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                resultatAdapter = new ResultatAdapter(this, resultats);
                recyclerView.setAdapter(resultatAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de parsing JSON",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // On relit la clé API à chaque retour sur cet écran
        SharedPreferences prefs = getSharedPreferences("actionzone_prefs", MODE_PRIVATE);
        CLE_API = prefs.getString("cle_api_tmdb", "");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rechercheRunnable != null) {
            handler.removeCallbacks(rechercheRunnable);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (editTextRecherche != null) {
            outState.putString("derniereRecherche",
                    editTextRecherche.getText().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String derniereRecherche = savedInstanceState.getString("derniereRecherche", "");
        if (!derniereRecherche.isEmpty()) {
            editTextRecherche.setText(derniereRecherche);
        }
    }
}