package mx.itson.macondo.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mx.itson.macondo.entidades.LugarEntidad;

/**
 * @author  ramos.isw@gmail.com
 * Created by Julio C. Ramos on 01/05/2015.
 */
public class MacondoDbManger {
    public static String
            INT_TYPE = "INTEGER",
            DOUBLE_TYPE = "REAL",
            FLOAT_TYPE = DOUBLE_TYPE,
            STRING_TYPE = "TEXT",
            AUTOINCREMENT = "AUTOINCREMENT",
            PRIMARY_KEY = "PRIMARY KEY";
    private final MacondoDBHelper helper;
    private final SQLiteDatabase wDb;

    public MacondoDbManger(Context context) {
        helper = new MacondoDBHelper(context, null);
        wDb = helper.getWritableDatabase();
    }

    /**
     * @param name    nombre de la tabla
     * @param key     clave, su nombre, su tipo, si es incrementable
     * @param columns nombres y tipo de cada una de las columnas
     * @return Query para crear la tabla
     */
    public static String getQueryTable(String name, Key key, Columm[] columns) {
        String query = String.format("CREATE TABLE %s (", name);
        query += String.format(" %s %s %s ", key.NAME, key.TYPE, PRIMARY_KEY) + (key.AUTOINCREMENT ? AUTOINCREMENT : "");
        for (Columm columm : columns) {
            query += String.format(", %s %s ", columm.NAME, columm.TYPE);
        }
        return query + ");";
    }

    /**
     * @param id del lugar a eliminar
     */
    public void eliminarLugar(int id) {
        wDb.delete(
                LugarTable.NAME,
                LugarTable.Columns.ID + "=?",
                new String[]{Integer.toString(id)}
        );
    }

    /**
     * @param lugarEntidad objeto que contiene los datos del lugar
     */
    public void modificar(LugarEntidad lugarEntidad) {
        wDb.update(
                LugarTable.NAME,
                generarContenValuesLugar(lugarEntidad),
                LugarTable.Columns.ID + "=?",
                new String[]{Integer.toString(lugarEntidad.getId())}
        );
    }

    /**
     * @param lugarEntidad objeto que sera insertado en la base de datos
     */
    public void insertar(LugarEntidad lugarEntidad) {
        // TODO Auto-generated method stub

        long id = wDb.insert(
                LugarTable.NAME,
                null,
                generarContenValuesLugar(lugarEntidad)
        );
        lugarEntidad.setId((int) id);
    }

    /**
     * @param lugarEntidad que se convertira en un objeto contentValues
     * @return el objeto contentValues con los datos del lugar convertidos
     */
    private ContentValues generarContenValuesLugar(LugarEntidad lugarEntidad) {
        ContentValues lugarContenValues = new ContentValues();
        lugarContenValues.put(LugarTable.Columns.NOMBRE, lugarEntidad.getNombre());
        lugarContenValues.put(LugarTable.Columns.DIRECCION, lugarEntidad.getDireccion());
        lugarContenValues.put(LugarTable.Columns.REFERENCIAS, lugarEntidad.getReferencias());
        lugarContenValues.put(LugarTable.Columns.CARACTERISTICAS, lugarEntidad.getCaracteristicas());
        lugarContenValues.put(LugarTable.Columns.PATH_FOTO, lugarEntidad.getUri_foto());
        lugarContenValues.put(LugarTable.Columns.THUMB_FOTO, lugarEntidad.getUri_thumb_foto());
        lugarContenValues.put(LugarTable.Columns.LATITUD, lugarEntidad.getLatitud());
        lugarContenValues.put(LugarTable.Columns.LONGITUD, lugarEntidad.getLongitud());
        return lugarContenValues;
    }

    /**
     * @return una lista de lugares que esten en la base de datos
     */
    public List<LugarEntidad> cargarLugares() {
        String[] columnas = new String[]{
                LugarTable.Columns.ID,
                LugarTable.Columns.NOMBRE,
                LugarTable.Columns.DIRECCION,
                LugarTable.Columns.REFERENCIAS,
                LugarTable.Columns.CARACTERISTICAS,
                LugarTable.Columns.PATH_FOTO,
                LugarTable.Columns.THUMB_FOTO,
                LugarTable.Columns.LATITUD,
                LugarTable.Columns.LONGITUD
        };
        Cursor cursor = wDb.query(
                LugarTable.NAME,
                columnas,
                null,
                null,
                null,
                null,
                null
        );
        List<LugarEntidad> lugares = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                LugarEntidad lugar = new LugarEntidad();
                lugar.setId(cursor.getInt(cursor.getColumnIndex(LugarTable.Columns.ID)));
                lugar.setNombre(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.NOMBRE)));
                lugar.setDireccion(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.DIRECCION)));
                lugar.setReferencias(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.REFERENCIAS)));
                lugar.setCaracteristicas(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.CARACTERISTICAS)));
                lugar.setUri_foto(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.PATH_FOTO)));
                lugar.setUri_thumb_foto(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.THUMB_FOTO)));
                lugar.setLatitud(cursor.getDouble(cursor.getColumnIndex(LugarTable.Columns.LATITUD)));
                lugar.setLongitud(cursor.getDouble(cursor.getColumnIndex(LugarTable.Columns.LONGITUD)));
                lugares.add(lugar);
            } while (cursor.moveToNext());
        }
        return lugares;
    }

    public LugarEntidad cargarLugar(int id) {
        String[] columnas = new String[]{
                LugarTable.Columns.ID,
                LugarTable.Columns.NOMBRE,
                LugarTable.Columns.DIRECCION,
                LugarTable.Columns.REFERENCIAS,
                LugarTable.Columns.CARACTERISTICAS,
                LugarTable.Columns.PATH_FOTO,
                LugarTable.Columns.THUMB_FOTO,
                LugarTable.Columns.LATITUD,
                LugarTable.Columns.LONGITUD
        };
        Cursor cursor = wDb.query(
                LugarTable.NAME,
                columnas,
                LugarTable.Columns.ID + "=?",
                new String[]{Integer.toString(id)},
                null,
                null,
                null
        );
        LugarEntidad lugar = new LugarEntidad();
        if (cursor.moveToFirst()) {
            lugar.setId(cursor.getInt(cursor.getColumnIndex(LugarTable.Columns.ID)));
            lugar.setNombre(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.NOMBRE)));
            lugar.setDireccion(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.DIRECCION)));
            lugar.setReferencias(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.REFERENCIAS)));
            lugar.setCaracteristicas(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.CARACTERISTICAS)));
            lugar.setUri_foto(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.PATH_FOTO)));
            lugar.setUri_thumb_foto(cursor.getString(cursor.getColumnIndex(LugarTable.Columns.THUMB_FOTO)));
            lugar.setLatitud(cursor.getDouble(cursor.getColumnIndex(LugarTable.Columns.LATITUD)));
            lugar.setLongitud(cursor.getDouble(cursor.getColumnIndex(LugarTable.Columns.LONGITUD)));
        }
        return lugar;
    }

    public static class Columm {
        public String NAME;
        public String TYPE;

        public Columm(String name, String type) {
            this.NAME = name;
            this.TYPE = type;
        }
    }

    /**
     * Class Key representa a una clave de la base de datos
     * NAME = nombre de la clave
     * TYPE = typo de dato para la clave
     * AUTOINCREMENT = en caso de que sea numerica si es autoincrementable o no
     */
    public static class Key {
        public String NAME;
        public String TYPE;
        public boolean AUTOINCREMENT;

        public Key(String name, String type, boolean autoincrement) {
            this.NAME = name;
            this.TYPE = type;
            this.AUTOINCREMENT = autoincrement;
        }
    }

    /**
     * Clase para generar una tabla apartir de sus atributos
     * NAME = nombre de la tabla
     * KEY = nombre de indice primario, su tipo y si es autoincrementable
     * COLUMNS = cada una de las columnas que guardaran los datos de la tabla asi como su nombre y tipo
     */
    public static class LugarTable {
        public static String NAME = "tblLugar";
        public static Key KEY = new Key(Columns.ID, INT_TYPE, true);
        public static Columm[] COLUMNS = {
                new Columm(Columns.NOMBRE, STRING_TYPE),
                new Columm(Columns.LATITUD, DOUBLE_TYPE),
                new Columm(Columns.LONGITUD, DOUBLE_TYPE),
                new Columm(Columns.REFERENCIAS, STRING_TYPE),
                new Columm(Columns.DIRECCION, STRING_TYPE),
                new Columm(Columns.CARACTERISTICAS, STRING_TYPE),
                new Columm(Columns.PATH_FOTO, STRING_TYPE),
                new Columm(Columns.THUMB_FOTO, STRING_TYPE)
        };

        public static class Columns {
            public static String
                    ID = "ID",
                    NOMBRE = "NOMBRE",
                    LATITUD = "LATITUD",
                    LONGITUD = "LONGITUD",
                    REFERENCIAS = "REFERENCIAS",
                    DIRECCION = "DIRECCION",
                    CARACTERISTICAS = "CARACTERISTICAS",
                    PATH_FOTO = "PATH_FOTO",
                    THUMB_FOTO = "THUMB_FTO";
        }
    }
}
