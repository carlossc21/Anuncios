package es.carlossc212.anuncios.objects;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.repository.OnInsert;

public class Historial {
    private static Context appContext;
    private static Fragment currentFragment;
    private static MutableLiveData<Anuncio> anuncio = new MutableLiveData<>();
    private static MutableLiveData<Long> lastInsert = new MutableLiveData<>();
    private static OnInsert listener;

    public static void setContext(Context context){
        appContext = context;
    }

    private static File getHistoryFile(){
        return new File(appContext.getExternalFilesDir(null), "historial.txt");
    }

    public static BufferedWriter getWriter(){
        BufferedWriter writer = null;
        try {
            writer =  new BufferedWriter(new FileWriter(getHistoryFile(), true));
        } catch (IOException e) {}

        return writer;
    }

    public static String getHistorial() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(getHistoryFile()));

        String linea;
        String texto = "";
        while((linea=br.readLine())!=null)
            texto+=linea + "\n";

        return texto;
    }

    public static void setCurrentFragment(Fragment f){
        currentFragment = f;
    }

    public static Fragment getCurrentFragment(){
        return currentFragment;
    }

    public static void setCurrentAnuncio(Anuncio a){
        anuncio.setValue(a);
    }

    public static MutableLiveData<Anuncio> getLiveAnuncio(){
        return anuncio;
    }

    public static void setLastInsert(Long l){
        lastInsert.setValue(l);
        listener.onInsert(l);
    }

    public static void setOnInsertAnuncioListener(OnInsert l){
        listener = l;
    }
}
