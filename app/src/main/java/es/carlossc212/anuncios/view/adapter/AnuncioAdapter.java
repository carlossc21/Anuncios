package es.carlossc212.anuncios.view.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import es.carlossc212.anuncios.R;
import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.view.adapter.viewholder.AnuncioViewHolder;
import es.carlossc212.anuncios.viewmodel.ImagenViewModel;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioViewHolder>{

    private ArrayList<Anuncio> anunciosList;
    private FragmentActivity context;
    private ImagenViewModel ivm;

    public AnuncioAdapter(FragmentActivity context){
        this.context = context;
    }

    @NonNull
    @Override
    public AnuncioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new AnuncioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnuncioViewHolder avh, int i) {
        ivm = new ImagenViewModel(context.getApplication());

        Anuncio anuncio = anunciosList.get(i);
        ivm.requestAllImagenes(anuncio.id);
        avh.anuncio = anuncio;
        avh.tvTitulo.setText(anuncio.titulo);

        String desc_aux = "";
        char[] chars = anuncio.descripcion.toCharArray();
        if (chars.length > 125){
            for (int j = 0; j < 125; j++) {
                desc_aux += chars[j];
            }
            desc_aux += " ...";
            avh.tvDesc.setText(desc_aux);

        }else{avh.tvDesc.setText(anuncio.descripcion);}

        avh.tvLocalidad.setText(" " + anuncio.localidad);

        Uri uri = Uri.parse("https://bitsofco.de/content/images/2018/12/broken-1.png");
        Glide.with(context).load(uri).into(avh.ivAnuncio);

        ivm.getImageMapLiveList().observe(context, bitmaps -> {
            avh.ivAnuncio.setImageBitmap(bitmaps.get(0));
        });
    }

    @Override
    public int getItemCount() {
        if (anunciosList == null) {
            return 0;
        }
        return anunciosList.size();
    }

    public void setAnunciosList(ArrayList<Anuncio> anunciosList){
        this.anunciosList = anunciosList;
        notifyDataSetChanged();
    }
}