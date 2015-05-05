package mx.itson.macondo.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author  ramos.isw@gmail.com
 * Created by Julio C. Ramos on 13/04/2015.
 */
public class MacondoDBHelper extends SQLiteOpenHelper {
    private static int VERSION = 1;
    private static String NAME = "mx.itson.macondo.db";

    public MacondoDBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = MacondoDbManger.getQueryTable(MacondoDbManger.LugarTable.NAME, MacondoDbManger.LugarTable.KEY, MacondoDbManger.LugarTable.COLUMNS);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
