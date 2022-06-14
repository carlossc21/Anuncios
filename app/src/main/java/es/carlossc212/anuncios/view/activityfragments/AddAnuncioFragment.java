package es.carlossc212.anuncios.view.activityfragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import es.carlossc212.anuncios.R;
import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.objects.Historial;
import es.carlossc212.anuncios.repository.OnInsert;
import es.carlossc212.anuncios.viewmodel.AnuncioViewModel;
import es.carlossc212.anuncios.viewmodel.ImagenViewModel;


public class AddAnuncioFragment extends Fragment {

    private View root;
    private Anuncio anuncio;
    private EditText etTelefono, etEmail, etNombre, etTitulo, etPrecio, etDesc, etLocalidad;
    private Button btSubir ,btAñadirFoto;
    private AnuncioViewModel avm;
    private ImagenViewModel ivm;
    private ArrayList<File> imagenes = new ArrayList<>();

    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                copyData(data);
                            } catch (IOException e) {e.printStackTrace();}
                        }
                    }
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Historial.setCurrentFragment(this);
        avm = new AnuncioViewModel(getActivity().getApplication());
        ivm = new ImagenViewModel(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_anuncio, container, false);
        initialize();
        return root;
    }

    private void initialize() {
        etLocalidad = root.findViewById(R.id.etLocalidad);
        etTelefono = root.findViewById(R.id.etTelefonoAnuncio);
        etTitulo = root.findViewById(R.id.etTituloAnuncio);
        etPrecio = root.findViewById(R.id.etPrecioAnuncio);
        etDesc = root.findViewById(R.id.etDescAnuncio);
        etEmail = root.findViewById(R.id.etCorreoAnuncio);
        etNombre = root.findViewById(R.id.etNombreAnuncio);
        btSubir = root.findViewById(R.id.btSubir);
        btAñadirFoto =root.findViewById(R.id.subirFoto);
        btSubir.setOnClickListener(v->{
            try {
                if (check()) {
                    OnInsert insert = new OnInsert() {
                        @Override
                        public void onInsert(long id) {
                            ivm.insertImagen(id, imagenes);
                        }
                    };

                    Historial.setOnInsertAnuncioListener(insert);

                    anuncio = new Anuncio();
                    createAnuncioFromFields();
                    avm.insertAnuncio(anuncio);
                    NavHostFragment.findNavController(Historial.getCurrentFragment()).popBackStack();
                }
            }catch (NumberFormatException e){}
        });

        btAñadirFoto.setOnClickListener(v->{
            selectImage();
            System.out.println(imagenes.size());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Historial.setCurrentFragment(this);
    }

    void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE);
        resultLauncher.launch(intent);
    }

    void copyData(Intent data) throws IOException {
        Uri uri = data.getData();
        InputStream in = null;
        OutputStream out = null;
        in = getActivity().getContentResolver().openInputStream(uri);
        File file = new File(getActivity().getExternalFilesDir(null), "archivo" + imagenes.size());
        out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        imagenes.add(file);
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

    private void createAnuncioFromFields(){
        anuncio.titulo = etTitulo.getText().toString();
        anuncio.telefono = etTelefono.getText().toString();
        anuncio.descripcion = etDesc.getText().toString();
        anuncio.correo = etEmail.getText().toString();
        anuncio.nombre = etNombre.getText().toString();
        anuncio.precio = getPrice();
        anuncio.localidad = etLocalidad.getText().toString();
    }

    private float getPrice(){
        String aux = etPrecio.getText().toString();
        aux = aux.replace("€", "");
        aux = aux.replace(",", ".");
        return Float.parseFloat(aux);
    }
}