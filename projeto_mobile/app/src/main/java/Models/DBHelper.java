package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DbProjeto";
    private static final int DB_VERSION = 1;
    private final SQLiteDatabase db;
    private static final String TABLE_PRODUTOS = "produtos", TABLE_FATURAS = "faturas", TABLE_LINHAS = "linhasFatura", TABLE_CATEGORIAS = "categorias", TABLE_MARCAS = "marcas";
    //nome de campos
    private static final String ID = "id", NIF = "nif",
            MORADA = "morada", DATA = "data", VALORTOTAL = "valorTotal", VALORIVA ="valorIva",
            PRODUTO = "produto_nome", REFERENCIA = "produto_referencia", QUANTIDADE = "quantidade",
            VALOR = "valor", ID_FATURA = "id_fatura", NOME = "nome", SUBTOTAL = "subtotal", VALORDESCONTO = "valorDesconto";

    public DBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableFaturas = "CREATE TABLE "+TABLE_FATURAS+"("+ID+" INTEGER PRIMARY KEY,"+
                NIF+" TEXT NOT NULL,"+
                MORADA+" TEXT NOT NULL,"+
                DATA+" TEXT NOT NULL,"+
                VALORTOTAL+" DOUBLE NOT NULL,"+
                VALORIVA+" DOUBLE NOT NULL,"+
                VALORDESCONTO+" DOUBLE NOT NULL,"+
                SUBTOTAL+" DOUBLE NOT NULL);";
        sqLiteDatabase.execSQL(createTableFaturas);

        String createTableLinhasFatura = "CREATE TABLE "+TABLE_LINHAS+"("+ID+" INTEGER PRIMARY KEY,"+
                ID_FATURA+" INTEGER NOT NULL,"+
                PRODUTO+" TEXT NOT NULL,"+
                REFERENCIA+" TEXT NOT NULL,"+
                QUANTIDADE+" INTEGER NOT NULL,"+
                VALOR+" DOUBLE NOT NULL,"+
                VALORIVA+" DOUBLE NOT NULL);";

        sqLiteDatabase.execSQL(createTableLinhasFatura);
        String createTableCategorias = "CREATE TABLE "+TABLE_CATEGORIAS+"("+ID+" INTEGER PRIMARY KEY,"+
                NOME+" TEXT NOT NULL);";

        sqLiteDatabase.execSQL(createTableCategorias);

        String createTableMarcas = "CREATE TABLE "+TABLE_MARCAS+"("+ID+" INTEGER PRIMARY KEY,"+
                NOME+" TEXT NOT NULL);";

        sqLiteDatabase.execSQL(createTableMarcas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String deleteSQLTableFaturas = "DROP TABLE IF EXISTS "+TABLE_FATURAS;
        sqLiteDatabase.execSQL(deleteSQLTableFaturas);
        String deleteSQLTableLinhasFatura = "DROP TABLE IF EXISTS "+TABLE_LINHAS;
        sqLiteDatabase.execSQL(deleteSQLTableLinhasFatura);
        String deleteSQLTableCategorias = "DROP TABLE IF EXISTS "+TABLE_CATEGORIAS;
        sqLiteDatabase.execSQL(deleteSQLTableCategorias);
        String deleteSQLTableMarcas = "DROP TABLE IF EXISTS "+TABLE_MARCAS;
        sqLiteDatabase.execSQL(deleteSQLTableMarcas);
        this.onCreate(sqLiteDatabase);
    }

    public void addFaturaDB(Fatura f){
        ContentValues values = new ContentValues();
            values.put(ID, f.getId());
            values.put(NIF, f.getNif());
            values.put(MORADA, f.getMorada());
            values.put(DATA, f.getData());
            values.put(VALORTOTAL, f.getValorTotal());
            values.put(VALORIVA, f.getValorIva());
            values.put(VALORDESCONTO, f.getValorDesconto());
            values.put(SUBTOTAL, f.getSubtotal());
            db.insert(TABLE_FATURAS, null, values);

    }

    public void addLinhasDB(ArrayList<LinhaFatura> linhas){
        ContentValues values = new ContentValues();
        for(LinhaFatura l:linhas) {
            values.put(ID, l.getId());
            values.put(ID_FATURA, l.getId_Fatura());
            values.put(PRODUTO, l.getProduto_nome());
            values.put(REFERENCIA, l.getProduto_referencia());
            values.put(QUANTIDADE, l.getQuantidade());
            values.put(VALOR, l.getValor());
            values.put(VALORIVA, l.getValorIva());
            db.insert(TABLE_LINHAS, null, values);
        }
    }

    public ArrayList<Fatura> getAllFaturasDB(){
        ArrayList<Fatura> faturas = new ArrayList<>();
        Cursor cursor = db.query(TABLE_FATURAS, new String[]{ID, NIF, MORADA, DATA, VALORTOTAL, VALORIVA, VALORDESCONTO, SUBTOTAL},
                null, null, null, null, ID);
        if(cursor.moveToFirst()){
            do {
                Fatura auxFatura = new Fatura(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7));
                faturas.add(auxFatura);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return faturas;
    }

    public ArrayList<LinhaFatura> getAllLinhasFaturaDB(int idFatura){
        ArrayList<LinhaFatura> linhas = new ArrayList<>();
        Cursor cursor = db.query(TABLE_LINHAS, new String[]{ID, ID_FATURA, PRODUTO, REFERENCIA, QUANTIDADE, VALOR, VALORIVA},
                ID_FATURA+"=?", new String[]{String.valueOf(idFatura)}, null, null, ID);
        if(cursor.moveToFirst()){
            do {
                LinhaFatura auxLinha = new LinhaFatura(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getDouble(5), cursor.getDouble(6));
                linhas.add(auxLinha);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return linhas;
    }

    public Fatura getFaturaDB(int id){
        Fatura fatura = null;
        Cursor cursor = db.query(TABLE_FATURAS, new String[]{ID, NIF, MORADA, DATA, VALORTOTAL, VALORIVA, VALORDESCONTO, SUBTOTAL},
                ID+"=?", new String[]{String.valueOf(id)}, null, null, ID);
        if(cursor.moveToFirst()){
            fatura = new Fatura(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7));
            cursor.close();
        }
        return fatura;
    }

    public ArrayList<Categoria> getAllCategoriasDB(){
        ArrayList<Categoria> categorias = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CATEGORIAS, new String[]{ID, NOME},
                null, null, null, null, ID);
        if(cursor.moveToFirst()){
            do {
                Categoria auxCategoria = new Categoria(cursor.getInt(0), cursor.getString(1));
                categorias.add(auxCategoria);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return categorias;
    }

    public ArrayList<Marca> getAllMarcasDB(){
        ArrayList<Marca> marcas = new ArrayList<>();
        Cursor cursor = db.query(TABLE_MARCAS, new String[]{ID, NOME},
                null, null, null, null, ID);
        if(cursor.moveToFirst()){
            do {
                Marca auxMarca = new Marca(cursor.getInt(0), cursor.getString(1));
                marcas.add(auxMarca);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return marcas;
    }

    public void removeAllFilters(){
        db.delete(TABLE_CATEGORIAS, null, null);
        db.delete(TABLE_MARCAS, null, null);
    }

    public void removeAllFaturas(){
        db.delete(TABLE_FATURAS, null, null);
        db.delete(TABLE_LINHAS, null, null);
    }

    public void dropDB(){
        onUpgrade(db, 1, 1);
    }

}
