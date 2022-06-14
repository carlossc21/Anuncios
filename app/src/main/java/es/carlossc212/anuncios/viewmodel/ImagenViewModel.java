package es.carlossc212.anuncios.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;

import es.carlossc212.anuncios.repository.AnunciosRepository;

public class ImagenViewModel extends AndroidViewModel {

    private AnunciosRepository repository;

    public ImagenViewModel(@NonNull Application application) {
        super(application);

        repository = new AnunciosRepository(application);
    }

    public void insertImagen(long idAnuncio, ArrayList<File> imagenes){ repository.upload(idAnuncio, imagenes); }

    public void requestAllImagenes(long idAnundio){
        repository.requestAnuncioImagenes(idAnundio);
    }
    public MutableLiveData<ArrayList<Bitmap>> getImageMapLiveList(){
        return repository.getImageMapLiveList();
    }



}
