package es.carlossc212.anuncios.view.activityfragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import es.carlossc212.anuncios.R;
import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.objects.Historial;
import es.carlossc212.anuncios.viewmodel.AnuncioViewModel;
import es.carlossc212.anuncios.viewmodel.ImagenViewModel;

public class DetailFragment extends Fragment {

    private View root;
    private Anuncio anuncio;
    private EditText etTelefono, etEmail, etNombre, etTitulo, etPrecio, etDesc;
    private Button btEdit, ivBack, ivNext, btEliminar;
    private ImageView ivRoller;
    private AnuncioViewModel avm;
    private ImagenViewModel ivm;
    private ArrayList<Bitmap> imagenes = new ArrayList<>();
    private int imagePos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Historial.setCurrentFragment(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_detail, container, false);
        initialize();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Historial.setCurrentFragment(this);
    }

    private void fill(){
        etTelefono.setText(anuncio.telefono);
        etTitulo.setText(anuncio.titulo);
        etPrecio.setText(anuncio.precio+"€");
        etNombre.setText(anuncio.nombre);
        etEmail.setText(anuncio.correo);
        etDesc.setText(anuncio.descripcion);
    }

    private void initialize(){
        avm = new AnuncioViewModel(getActivity().getApplication());
        ivm = new ImagenViewModel(getActivity().getApplication());
        btEliminar = root.findViewById(R.id.btEliminar);
        ivRoller = root.findViewById(R.id.ivRoller);
        ivBack = root.findViewById(R.id.imageBack);
        ivNext = root.findViewById(R.id.imageNext);
        etTelefono = root.findViewById(R.id.etTelefono);
        etTitulo = root.findViewById(R.id.etTitulo);
        etPrecio = root.findViewById(R.id.etPrecio);
        etDesc = root.findViewById(R.id.etDesc);
        etEmail = root.findViewById(R.id.etCorreo);
        etNombre = root.findViewById(R.id.etNombre);
        btEdit = root.findViewById(R.id.btEdit);
        btEliminar.setOnClickListener(v->{
            avm.deleteAnuncio(anuncio.id);
            NavHostFragment.findNavController(this).popBackStack();
        });
        ivBack.setOnClickListener(v->{
            if (imagePos != 0 && imagenes.size()>0){
                imagePos--;
                ivRoller.setImageBitmap(imagenes.get(imagePos));
            }
        });
        ivNext.setOnClickListener(v->{
            if (imagePos != (imagenes.size() - 1) && imagenes.size()>0){
                imagePos++;
                ivRoller.setImageBitmap(imagenes.get(imagePos));
            }
        });
        btEdit.setOnClickListener(v->{
            try {
                if (check()){
                    anuncio.correo = etEmail.getText().toString();
                    anuncio.titulo = etTitulo.getText().toString();
                    anuncio.precio = getPrice();
                    anuncio.descripcion = etDesc.getText().toString();
                    anuncio.nombre = etNombre.getText().toString();
                    anuncio.telefono =etTelefono.getText().toString();
                    avm.updateAnuncio(anuncio);
                    NavHostFragment.findNavController(Historial.getCurrentFragment()).popBackStack();
                }else{

                }
            }catch (NumberFormatException ex){}
        });

        Historial.getLiveAnuncio().observe(getActivity(), a->{
            if (Historial.getCurrentFragment().getClass() == DetailFragment.class){
                anuncio = a;
                ivm.requestAllImagenes(anuncio.id);
                fill();
            }
        });
        ivm.getImageMapLiveList().observe(getActivity(), bitmaps -> {
            imagenes = bitmaps;
            ivRoller.setImageBitmap(imagenes.get(imagePos));
        });
    }

    private boolean check(){
        if (    !etTitulo.getText().toString().trim().equals("") &&
                !etTelefono.getText().toString().trim().equals("") &&
                !etDesc.getText().toString().trim().equals("") &&
                !etEmail.getText().toString().trim().equals("") &&
                !etNombre.getText().toString().trim().equals("") &&
                !etPrecio.getText().toString().trim().equals("")){
            return true;
        }else{
            return false;
        }
    }

    private float getPrice(){
        String aux = etPrecio.getText().toString();
        aux = aux.replace("€", "");
        aux = aux.replace(",", ".");
        return Float.parseFloat(aux);
    }
}