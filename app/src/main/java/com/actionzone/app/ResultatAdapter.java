package com.actionzone.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ResultatAdapter extends RecyclerView.Adapter<ResultatAdapter.ResultatViewHolder> {


    private Context context;
    private List<Film> resultats;

    public ResultatAdapter(Context context, List<Film> resultats) {
        this.context = context;
        this.resultats = resultats;
    }


    @NonNull
    @Override
    public ResultatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(context)
                .inflate(R.layout.item_resultat, parent, false);
        return new ResultatViewHolder(vue);
    }


    @Override
    public void onBindViewHolder(@NonNull ResultatViewHolder holder, int position) {
        Film film = resultats.get(position);


        holder.textTitre.setText(film.getTitre());
        holder.textDateSortie.setText("Sortie : " + film.getDateSortie());
        holder.textNoteTmdb.setText(" " + String.format("%.1f", film.getNoteTmdb()) + " / 10");


        // Glide charge l'affiche depuis l'URL TMDB
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
                intent.putExtra("tmdbId", film.getTmdbId());
                intent.putExtra("titre", film.getTitre());
                intent.putExtra("synopsis", film.getSynopsis());
                intent.putExtra("affiche", film.getAffiche());
                intent.putExtra("noteTmdb", film.getNoteTmdb());
                intent.putExtra("dateSortie", film.getDateSortie());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultats.size();
    }

    // ViewHolder pour les résultats de recherche
    public static class ResultatViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAffiche;
        TextView textTitre;
        TextView textDateSortie;
        TextView textNoteTmdb;

        public ResultatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAffiche = itemView.findViewById(R.id.imageAficheResultat);
            textTitre = itemView.findViewById(R.id.textTitreResultat);
            textDateSortie = itemView.findViewById(R.id.textDateSortie);
            textNoteTmdb = itemView.findViewById(R.id.textNoteTmdb);
        }
    }
}