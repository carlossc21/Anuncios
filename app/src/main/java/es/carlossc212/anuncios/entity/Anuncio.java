package es.carlossc212.anuncios.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class Anuncio implements Parcelable {

    public long id = 0;

    public String titulo;

    public String descripcion;

    public String telefono;

    public String correo;

    public String localidad;

    public float precio;

    public String nombre;

    public Anuncio(){

    }

    protected Anuncio(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        descripcion = in.readString();
        telefono = in.readString();
        correo = in.readString();
        localidad = in.readString();
        precio = in.readFloat();
        nombre = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(telefono);
        dest.writeString(correo);
        dest.writeString(localidad);
        dest.writeFloat(precio);
        dest.writeString(nombre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Anuncio> CREATOR = new Creator<Anuncio>() {
        @Override
        public Anuncio createFromParcel(Parcel in) {
            return new Anuncio(in);
        }

        @Override
        public Anuncio[] newArray(int size) {
            return new Anuncio[size];
        }
    };

    @Override
    public String toString() {
        return "Anuncio{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", localidad='" + localidad + '\'' +
                ", precio=" + precio +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
