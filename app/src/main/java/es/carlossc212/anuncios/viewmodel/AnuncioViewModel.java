package es.carlossc212.anuncios.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.repository.AnunciosRepository;

public class AnuncioViewModel extends AndroidViewModel {

    private AnunciosRepository repository;

    public AnuncioViewModel(@NonNull Application application) {
        super(application);

        repository = new AnunciosRepository(application);
    }

    public void insertAnuncio(Anuncio anuncio){
        repository.insertAnuncio(anuncio);
    }

    public void updateAnuncio(Anuncio anuncio){
        repository.editAnuncio(anuncio);
    }

    public MutableLiveData<ArrayList<Anuncio>> getLivedataAnunios(){
        return repository.getLiveDataAnuncios();
    }

    public void requestAllAnuncios() {
        repository.requestAllAnuncios();
    }

    public void getAnuncio(long id){
        repository.getAnuncio(id);
    }

    public void deleteAnuncio(long idAnuncio){
        repository.deleteAnuncio(idAnuncio);
    }
}
