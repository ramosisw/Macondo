package mx.itson.macondo.entidades;

/**
 * @author ramos.isw@gmail.com
 * Created by Julio C. Ramos on 13/04/2015.
 */
public class LugarEntidad {
    private int id;
    private String nombre;
    private String direccion;
    private String referencias;
    private String caracteristicas;
    private String uri_foto;
    private String uri_thumb_foto;
    private double latitud;
    private double longitud;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

    public String getUri_foto() {
        return uri_foto;
    }

    public void setUri_foto(String uri_foto) {
        this.uri_foto = uri_foto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getUri_thumb_foto() {
        return uri_thumb_foto;
    }

    public void setUri_thumb_foto(String uri_thumb_foto) {
        this.uri_thumb_foto = uri_thumb_foto;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}
