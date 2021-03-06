package com.morpho.android.ws.impl.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.morpho.android.util.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Metodos y constantes genéricas para bases de datos SQLite.
 * 
 * @author Daniel Pedraza-Arcega
 * @since 1.0
 */
class SQLiteUtils {

    /**
     * Formatos de tiempo de SQLite.
     * 
     * @see <a href="http://sqlite.org/lang_datefunc.html">SQLite - Date And Time Functions</a>
     * @author Daniel Pedraza-Arcega
     * @since 1.0
     */
    static enum TimeString {
        DATE ("yyyy-MM-dd"), 
        DATETIME ("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"), 
        TIMESTAMP ("yyyy-MM-dd HH:mm:ss.SSS"), 
        TIME ("HH:mm:ss.SSS", "HH:mm:ss", "HH:mm");

        private final String[] formats;

        TimeString(String... formats) {
            this.formats = formats;
        }

        public String[] getFormats() {
            return formats;
        }
    }

    /**
     * El valor que {@link android.database.Cursor#getColumnIndex(String)} devuelve cuando la
     * columna no se encuentra.
     */
    static final int COLUMN_NOT_FOUND = -1;

    private static final String TAG = SQLiteUtils.class.getSimpleName();

    /** NO INVOCAR. */
    private SQLiteUtils() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    /**
     * Termina la transacción en curso si es posible.
     * 
     * @param database un objeto SQLiteDatabase.
     */
    static void endTransaction(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.endTransaction();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close database correctly");
            }
        }
    }

    /**
     * Cierra el objeto SQLiteDatabase provisto.
     * 
     * @param database un objeto SQLiteDatabase.
     */
    static void close(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.close();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close database correctly");
            }
        }
    }

    /**
     * Cierra el objeto Cursor provisto.
     * 
     * @param cursor un objeto Cursor.
     */
    static void close(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close cursor correctly");
            }
        }
    }

    /**
     * Cierra el objeto SQLiteStatement provisto.
     * 
     * @param statement un objeto SQLiteStatement.
     */
    static void close(SQLiteStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Log.e(TAG, "Couldn't close statement correctly");
            }
        }
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return si contiene o no la columna.
     */
    static boolean containsColumn(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName) != COLUMN_NOT_FOUND;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Byte getByte(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? (byte) cursor.getShort(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Short getShort(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getShort(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Integer getInteger(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getInt(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Long getLong(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getLong(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Float getFloat(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getFloat(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Double getDouble(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getDouble(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Boolean getBoolean(Cursor cursor, String columnName) {
        Short value = getShort(cursor, columnName);
        if (value != null) return value == 1 ? true : value == 0 ? false : null;
        return null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Character getCharacter(Cursor cursor, String columnName) {
        String value = getString(cursor, columnName);
        if (value != null) return value.charAt(0);
        return null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static String getString(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getString(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static byte[] getBlob(Cursor cursor, String columnName) {
        return containsColumn(cursor, columnName) && !cursor.isNull(cursor.getColumnIndex(columnName))
                ? cursor.getBlob(cursor.getColumnIndex(columnName))
                : null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Date getDateFromUnixTime(Cursor cursor, String columnName) {
        Long value = getLong(cursor, columnName);
        if (value != null) return new Date(value * 1000L);
        return null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @param timeString el formato de la fecha.
     * @return el valor de la columna. Si no existe la columna o el valor de la columna es
     *         {@code null} entonces {@code null}.
     */
    static Date getDateFromString(Cursor cursor, String columnName, TimeString timeString) {
        String value = getString(cursor, columnName);
        if (!Strings.isNullOrBlank(value)) {
            for (String format : timeString.formats) {
                try {
                    return new SimpleDateFormat(format, Locale.ENGLISH).parse(value);
                } catch (ParseException ex) {
                    // Si no es el formato correcto, se ignora y sigue con el siguiente formato.
                }
            }
        }

        return null;
    }
    
    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @param enumType el tipo de enumerado.
     * @return el valor de la columna como enumerado. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    static <E extends Enum<E>> E getEnumFromName(Cursor cursor, String columnName, Class<E> enumType) {
        String value = getString(cursor, columnName);
        if (!Strings.isNullOrBlank(value)) return Enum.valueOf(enumType, value);
        return null;
    }

    /**
     * @param cursor un objeto Cursor.
     * @param columnName el nombre de la columna.
     * @param enumType el tipo de enumerado.
     * @return el valor de la columna como enumerado. Si no existe la columna o el valor de la
     *         columna es {@code null} entonces {@code null}.
     */
    static <E extends Enum<E>> E getEnumFromOrdinal(Cursor cursor, String columnName, Class<E> enumType) {
        Integer value = getInteger(cursor, columnName);
        if (value != null) return enumType.getEnumConstants()[value];
        return null;
    }

    /**
     * @param numberOfPlaceholders la cantidad de '?' 
     * @return "?, ?, ..., ?"
     */
    static String placeholdersForInClause(int numberOfPlaceholders) {
        if (numberOfPlaceholders >= 1) {
            StringBuilder sb = new StringBuilder(numberOfPlaceholders * 2 - 1).append('?');
            for (int i = 1; i < numberOfPlaceholders; i++) sb.append(", ?");
            return sb.toString();
        }
        
        Log.w(TAG, "No placeholders, will lead to an invalid query!!");
        return null;
    }
}