package es.carlossc212.anuncios.entity;


public class Imagen {

    public long id = 0;

    public long idanuncio;

    public String nombre;

    public String descripcion;

    public String toString(){
        return descripcion;
    }
}
