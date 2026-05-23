package com.actionzone.app;

public class Film {


    private int id;
    private int tmdbId;
    private String titre;
    private String synopsis;
    private String affiche;
    private double noteTmdb;
    private String dateSortie;
    private String statut;
    private float notePerso;
    private String avis;
    private String dateVisionnage;

    public Film() {}

    public Film(int tmdbId, String titre, String synopsis,
                String affiche, double noteTmdb, String dateSortie) {
        this.tmdbId = tmdbId;
        this.titre = titre;
        this.synopsis = synopsis;
        this.affiche = affiche;
        this.noteTmdb = noteTmdb;
        this.dateSortie = dateSortie;

        this.statut = "à voir";
        this.notePerso = 0;
        this.avis = "";
        this.dateVisionnage = "";
    }


    public int getId() { return id; }

    public int getTmdbId() { return tmdbId; }

    public String getTitre() { return titre; }

    public String getSynopsis() { return synopsis; }

    public String getAffiche() { return affiche; }

    public double getNoteTmdb() { return noteTmdb; }

    public String getDateSortie() { return dateSortie; }

    public String getStatut() { return statut; }

    public float getNotePerso() { return notePerso; }

    public String getAvis() { return avis; }

    public String getDateVisionnage() { return dateVisionnage; }


    public void setId(int id) { this.id = id; }

    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    public void setTitre(String titre) { this.titre = titre; }

    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public void setAffiche(String affiche) { this.affiche = affiche; }

    public void setNoteTmdb(double noteTmdb) { this.noteTmdb = noteTmdb; }

    public void setDateSortie(String dateSortie) { this.dateSortie = dateSortie; }

    public void setStatut(String statut) { this.statut = statut; }

    public void setNotePerso(float notePerso) { this.notePerso = notePerso; }

    public void setAvis(String avis) { this.avis = avis; }

    public void setDateVisionnage(String dateVisionnage) { this.dateVisionnage = dateVisionnage; }
}