package es.carlossc212.anuncios.api;

import java.util.ArrayList;

import es.carlossc212.anuncios.entity.Anuncio;
import es.carlossc212.anuncios.entity.Imagen;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ClienteAnuncio {

    @DELETE("anuncio/{id}")
    Call<Integer> borrarAnuncio(@Path("id") long id);

    @GET("anuncio/{id}")
    Call<Anuncio> obtenerAnuncio(@Path("id") long id);

    @GET("anuncio")
    Call<ArrayList<Anuncio>> obtenerAnuncios();

    @POST("anuncio")
    Call<Long> crearAnuncio(@Body Anuncio anuncio);

    @PUT("anuncio/{id}")
    Call<Integer> editarAnuncio(@Path("id") long id, @Body Anuncio anuncio);

    @Multipart
    @POST("subir")
    Call<Long> subir(@Part MultipartBody.Part file, @Part("id") long idAnuncio);

    @Multipart
    @POST("subiranuncio")
    Call<Long> subirAnuncio(@Part MultipartBody.Part file, @Part("idanuncio") long idAnuncio, @Part("nombre") String nombre, @Part("descripcion") String descripcion);

    @GET("fotos/{anuncio}")
    Call<ArrayList<Imagen>> fotos(@Path("anuncio") long idanuncio);

    @GET("foto/{imagen}")
    Call<ResponseBody> foto(@Path("imagen") long id);
}
