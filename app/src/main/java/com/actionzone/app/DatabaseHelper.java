package com.actionzone.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOM_BASE = "actionzone.db";

    private static final int VERSION = 1;

    private static final String TABLE_FILMS = "films";

    private static final String COL_ID = "id";
    private static final String COL_TMDB_ID = "tmdb_id";
    private static final String COL_TITRE = "titre";
    private static final String COL_SYNOPSIS = "synopsis";
    private static final String COL_AFFICHE = "affiche";
    private static final String COL_NOTE_TMDB = "note_tmdb";
    private static final String COL_DATE_SORTIE = "date_sortie";
    private static final String COL_STATUT = "statut";
    private static final String COL_NOTE_PERSO = "note_perso";
    private static final String COL_AVIS = "avis";
    private static final String COL_DATE_VISIONNAGE = "date_visionnage";

    public DatabaseHelper(Context context) {
        super(context, NOM_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String creerTable = "CREATE TABLE " + TABLE_FILMS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TMDB_ID + " INTEGER, " +
                COL_TITRE + " TEXT, " +
                COL_SYNOPSIS + " TEXT, " +
                COL_AFFICHE + " TEXT, " +
                COL_NOTE_TMDB + " REAL, " +
                COL_DATE_SORTIE + " TEXT, " +
                COL_STATUT + " TEXT DEFAULT 'à voir', " +
                COL_NOTE_PERSO + " REAL DEFAULT 0, " +
                COL_AVIS + " TEXT, " +
                COL_DATE_VISIONNAGE + " TEXT" +
                ")";
        db.execSQL(creerTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        onCreate(db);
    }

    public long ajouterFilm(Film film) {

        SQLiteDatabase db = this.getWritableDatabase();

        // ContentValues c'est comme un dictionnaire clé-valeur pour les données à insérer
        ContentValues valeurs = new ContentValues();
        valeurs.put(COL_TMDB_ID, film.getTmdbId());
        valeurs.put(COL_TITRE, film.getTitre());
        valeurs.put(COL_SYNOPSIS, film.getSynopsis());
        valeurs.put(COL_AFFICHE, film.getAffiche());
        valeurs.put(COL_NOTE_TMDB, film.getNoteTmdb());
        valeurs.put(COL_DATE_SORTIE, film.getDateSortie());
        valeurs.put(COL_STATUT, film.getStatut());
        valeurs.put(COL_NOTE_PERSO, film.getNotePerso());
        valeurs.put(COL_AVIS, film.getAvis());
        valeurs.put(COL_DATE_VISIONNAGE, film.getDateVisionnage());

        long id = db.insert(TABLE_FILMS, null, valeurs);
        db.close();
        return id;
    }


    public List<Film> getTousLesFilms() {
        List<Film> listeFilms = new ArrayList<>();

        // getReadableDatabase() ouvre la base en lecture seule
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FILMS, null);

        if (cursor.moveToFirst()) {
            do {
                Film film = new Film();
                film.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                film.setTmdbId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TMDB_ID)));
                film.setTitre(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE)));
                film.setSynopsis(cursor.getString(cursor.getColumnIndexOrThrow(COL_SYNOPSIS)));
                film.setAffiche(cursor.getString(cursor.getColumnIndexOrThrow(COL_AFFICHE)));
                film.setNoteTmdb(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_NOTE_TMDB)));
                film.setDateSortie(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE_SORTIE)));
                film.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUT)));
                film.setNotePerso(cursor.getFloat(cursor.getColumnIndexOrThrow(COL_NOTE_PERSO)));
                film.setAvis(cursor.getString(cursor.getColumnIndexOrThrow(COL_AVIS)));
                film.setDateVisionnage(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE_VISIONNAGE)));
                listeFilms.add(film);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listeFilms;
    }

    public int modifierFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valeurs = new ContentValues();
        valeurs.put(COL_STATUT, film.getStatut());
        valeurs.put(COL_NOTE_PERSO, film.getNotePerso());
        valeurs.put(COL_AVIS, film.getAvis());
        valeurs.put(COL_DATE_VISIONNAGE, film.getDateVisionnage());

        // update() modifie la ligne où id = film.getId()
        int lignesModifiees = db.update(TABLE_FILMS, valeurs, COL_ID + "=?",
                new String[]{String.valueOf(film.getId())});
        db.close();
        return lignesModifiees;
    }

    public void supprimerFilm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILMS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean filmExiste(int tmdbId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_FILMS + " WHERE " + COL_TMDB_ID + "=?",
                new String[]{String.valueOf(tmdbId)}
        );
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }
}