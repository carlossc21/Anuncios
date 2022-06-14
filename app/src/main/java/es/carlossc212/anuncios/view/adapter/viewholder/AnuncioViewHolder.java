package es.carlossc212.anuncios.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import es.carlossc212.anuncios.R;
import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.objects.Historial;

public class AnuncioViewHolder extends RecyclerView.ViewHolder{

    public Anuncio anuncio;
    public TextView tvTitulo, tvDesc, tvLocalidad;
    public ImageView ivAnuncio;

    public AnuncioViewHolder(@NonNull View itemView) {
        super(itemView);

        tvTitulo = itemView.findViewById(R.id.tvTitulo);
        ivAnuncio = itemView.findViewById(R.id.ivAnuncio);
        tvDesc = itemView.findViewById(R.id.tvDesc);
        tvLocalidad = itemView.findViewById(R.id.tvLocalidd);

        itemView.setOnClickListener(v->{
            Historial.setCurrentAnuncio(anuncio);
            NavHostFragment.findNavController(Historial.getCurrentFragment()).navigate(R.id.see_details);
        });
    }
}
