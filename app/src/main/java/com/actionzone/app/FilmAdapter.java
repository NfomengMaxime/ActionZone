package com.actionzone.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private Context context;
    private List<Film> listeFilms;
    private DatabaseHelper databaseHelper;

    public FilmAdapter(Context context, List<Film> listeFilms) {
        this.context = context;
        this.listeFilms = listeFilms;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = listeFilms.get(position);

        holder.textTitre.setText(film.getTitre());
        holder.textNotePerso.setText(film.getNotePerso() + " / 5 ");

        holder.textStatut.setText(film.getStatut());
        switch (film.getStatut()) {
            case "vu":

                holder.textStatut.setBackgroundColor(0xFF2E7D32);
                break;
            case "en cours":

                holder.textStatut.setBackgroundColor(0xFFE65100);
                break;
            default:

                holder.textStatut.setBackgroundColor(0xFFCC0000);
                break;
        }


        String avis = film.getAvis();
        if (avis != null && !avis.trim().isEmpty()) {
            holder.textAvis.setVisibility(View.VISIBLE);
            holder.textAvis.setText("\" " + avis + " \"");
        } else {
            holder.textAvis.setVisibility(View.GONE);
        }

        if (film.getAffiche() != null && !film.getAffiche().isEmpty()) {
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w200" + film.getAffiche())
                    .placeholder(R.drawable.affiche_defaut)
                    .error(R.drawable.affiche_defaut)
                    .into(holder.imageAffiche);
        } else {
            holder.imageAffiche.setImageResource(R.drawable.affiche_defaut);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("filmId", film.getId());
                intent.putExtra("tmdbId", film.getTmdbId());
                intent.putExtra("titre", film.getTitre());
                intent.putExtra("synopsis", film.getSynopsis());
                intent.putExtra("affiche", film.getAffiche());
                intent.putExtra("noteTmdb", film.getNoteTmdb());
                intent.putExtra("dateSortie", film.getDateSortie());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                afficherMenuContextuel(film, position);
                return true;
            }
        });
    }

    private void afficherMenuContextuel(Film film, int position) {
        String[] options = {"Modifier le statut", "Supprimer de la collection"};

        new AlertDialog.Builder(context)
                .setTitle(film.getTitre())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        modifierStatut(film, position);
                    } else if (which == 1) {
                        confirmerSuppression(film, position);
                    }
                })
                .show();
    }

    private void modifierStatut(Film film, int position) {
        String[] statuts = {"à voir", "en cours", "vu"};

        new AlertDialog.Builder(context)
                .setTitle("Changer le statut")
                .setItems(statuts, (dialog, which) -> {
                    film.setStatut(statuts[which]);
                    databaseHelper.modifierFilm(film);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Statut mis à jour ", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void confirmerSuppression(Film film, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Supprimer")
                .setMessage("Supprimer " + film.getTitre() + " de ta collection ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    databaseHelper.supprimerFilm(film.getId());
                    listeFilms.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Film supprimé ", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return listeFilms.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAffiche;
        TextView textTitre;
        TextView textStatut;
        TextView textNotePerso;
        TextView textAvis;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAffiche = itemView.findViewById(R.id.imageAffiche);
            textTitre = itemView.findViewById(R.id.textTitre);
            textStatut = itemView.findViewById(R.id.textStatut);
            textNotePerso = itemView.findViewById(R.id.textNotePerso);
            // On ajoute textAvis au ViewHolder
            textAvis = itemView.findViewById(R.id.textAvis);
        }
    }
}