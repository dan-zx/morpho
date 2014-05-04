package com.morpho.android.ws.impl.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Crea/maneja la base datos usada por Tedroid y provee acceso a ella.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
public class MorphoSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int CURRENT_VERSION = 1;
    private static final String TAG = MorphoSQLiteOpenHelper.class.getSimpleName();
    private static final String SCHEMA_FILE_PATH = "db/schema.sql";
    private static final String DATA_FILE_PATH = "db/data.sql";
    private static final String NAME = "morpho";

    private final Context context;

    /**
     * Crea un nuevo objeto usando el context proporcionado.
     * 
     * @param context el contexto de la aplicación.
     */
    public MorphoSQLiteOpenHelper(Context context) {
        super(context, NAME, null, CURRENT_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            InputStream schemaFileStream = context.getAssets().open(SCHEMA_FILE_PATH);
            String[] statements = SQLFileParser.getSqlStatements(schemaFileStream);
            for (String statement : statements) database.execSQL(statement);
            InputStream dataFileStream = context.getAssets().open(DATA_FILE_PATH);
            statements = SQLFileParser.getSqlStatements(dataFileStream);
            for (String statement : statements) database.execSQL(statement);
        } catch (IOException | SQLException ex) {
            Log.e(TAG, "Unable to execute schema", ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        destroyDb(context);
        onCreate(database);
    }

    /**
     * Destruye la base datos de Tedroid.
     * 
     * @param context el contexto de la aplicación.
     */
    public static void destroyDb(Context context) {
        context.deleteDatabase(NAME);
    }
}