package es.carlossc212.anuncios.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import es.carlossc212.anuncios.api.ClienteAnuncio;
import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.entity.Imagen;
import es.carlossc212.anuncios.objects.Historial;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnunciosRepository {

    private MutableLiveData<ArrayList<Anuncio>> anunciosList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Bitmap>> imagenesList = new MutableLiveData<>();
    private MutableLiveData<Integer> editionResult = new MutableLiveData<>();
    private MutableLiveData<Long> insertResult = new MutableLiveData<>();
    private ArrayList<Bitmap> imageMapList = new ArrayList<>();
    private ClienteAnuncio cliente;
    private Context context;


    public AnunciosRepository(Context context){
        this.context = context;
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://informatica.ieszaidinvergeles.org:10017/ad/anunciosApp/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cliente = rf.create(ClienteAnuncio.class);
    }


    public MutableLiveData<ArrayList<Anuncio>> getLiveDataAnuncios(){
        return anunciosList;
    }

    public MutableLiveData<Integer> getLivedataEditResult(){
        return editionResult;
    }

    public void requestAllAnuncios() {
        Call<ArrayList<Anuncio>> llamada = cliente.obtenerAnuncios();
        llamada.enqueue(new Callback<ArrayList<Anuncio>>() {
            @Override
            public void onResponse(Call<ArrayList<Anuncio>> call, Response<ArrayList<Anuncio>> response) {
                //si la se obtiene una respuesta
                //anunciosList = response.body();
                anunciosList.setValue(response.body());
                //anunciosList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Anuncio>> call, Throwable t) {
                //Si falla
                System.out.println("Error" + t.getLocalizedMessage());
            }
        });
    }

    public void deleteAnuncio(long idAnuncio){
        Call<Integer> llamada = cliente.borrarAnuncio(idAnuncio);
        llamada.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println("Eliminado");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("Fallo al eliminar");
            }
        });
    }

    public void editAnuncio(Anuncio a){
        Call<Integer> llamada = cliente.editarAnuncio(a.id, a);
        llamada.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                editionResult.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void requestAnuncioImagenes(long idAnuncio){
        imageMapList = new ArrayList<>();
        Call<ArrayList<Imagen>> llamada = cliente.fotos(idAnuncio);
        llamada.enqueue(new Callback<ArrayList<Imagen>>() {
            @Override
            public void onResponse(Call<ArrayList<Imagen>> call, Response<ArrayList<Imagen>> response) {
                try {
                    for (Imagen i : response.body()) {
                        getImagen(i.id);
                    }
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<ArrayList<Imagen>> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
            }
        });
    }

    public MutableLiveData<ArrayList<Bitmap>> getImageMapLiveList() {
        return imagenesList;
    }

    public void getAnuncio(long id){
        Call<Anuncio> llamada = cliente.obtenerAnuncio(id);
        llamada.enqueue(new Callback<Anuncio>() {
            @Override
            public void onResponse(Call<Anuncio> call, Response<Anuncio> response) {
                Anuncio a = response.body();
                Historial.setCurrentAnuncio(a);
            }

            @Override
            public void onFailure(Call<Anuncio> call, Throwable t) {

            }
        });

    }

    private void getImagen(long id){
        Call<ResponseBody> llamada = cliente.foto(id);
        llamada.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    Bitmap map = BitmapFactory.decodeStream(is);
                    imageMapList.add(map);
                    imagenesList.setValue(imageMapList);
                }catch (NullPointerException e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void insertAnuncio(Anuncio anuncio){
        Call<Long> llamada = cliente.crearAnuncio(anuncio);
        llamada.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                insertResult.setValue(response.body());
                System.out.println("response"+response.body());
                Historial.setLastInsert(response.body());
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
            }
        });
    }

    public void upload(long idAnuncio, ArrayList<File> imagenes) {
        for (int i = 0; i < imagenes.size(); i++) {
            File file = new File(context.getExternalFilesDir(null), "archivo"+i);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            Call<Long> call = cliente.subirAnuncio(body, idAnuncio, "foto "+i, "descripcion "+i);
            call.enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    System.out.println("Bien " + response.body());
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    System.out.println("Mal " + t.getLocalizedMessage());
                }
            });
        }
    }

}