package com.actionzone.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ParametresActivity extends AppCompatActivity {

    private static final String NOM_PREFS = "actionzone_prefs";
    private static final String CLE_NOM_UTILISATEUR = "nom_utilisateur";
    private static final String CLE_API_TMDB = "cle_api_tmdb";

    private static final String VALEUR_CLE_API = "f9d7e0049eea4312066758b92e6433b9";

    private EditText editTextNom;
    private Button buttonSauvegarder;
    private TextView textThematique;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Toolbar toolbar = findViewById(R.id.toolbarParametres);
        editTextNom = findViewById(R.id.editTextNomUtilisateur);
        buttonSauvegarder = findViewById(R.id.buttonEnregistrer);
        textThematique = findViewById(R.id.textThematique);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = getSharedPreferences(NOM_PREFS, MODE_PRIVATE);
        textThematique.setText("Thématique : Films d'Action");

        sauvegarderCleApiSilencieusement();
        chargerParametres();

        buttonSauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sauvegarderParametres();
            }
        });
    }

    private void sauvegarderCleApiSilencieusement() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLE_API_TMDB, VALEUR_CLE_API);
        editor.apply();
    }

    private void chargerParametres() {
        String nom = sharedPreferences.getString(CLE_NOM_UTILISATEUR, "");
        editTextNom.setText(nom);
    }

    private void sauvegarderParametres() {
        String nom = editTextNom.getText().toString().trim();
        if (nom.isEmpty()) {
            Toast.makeText(this, "Entre ton nom !", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLE_NOM_UTILISATEUR, nom);
        editor.apply();
        Toast.makeText(this, "Paramètres sauvegardés ✓", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        String nom = editTextNom.getText().toString().trim();
        if (!nom.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CLE_NOM_UTILISATEUR, nom);
            editor.apply();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nom", editTextNom.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editTextNom.setText(savedInstanceState.getString("nom", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}