package com.example.salesexpress.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.salesexpress.model.ItemModel;
import com.example.salesexpress.model.SalesModel;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    public static final String DATABASE_NAME = "ReprocessedSales.db";
    public SQLiteDatabase db;

    public MyDatabaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS reprocessed_sales ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "cpf TEXT,"
                + "email TEXT,"
                + "value DECIMAL(10,2),"
                + "moneyChange DECIMAL(10,2), "
                + "users_id INTEGER NOT NULL, "
                + "FOREIGN KEY(users_id) REFERENCES users(id)" + ")";;
        sqLiteDatabase.execSQL(query);

        String items = "CREATE TABLE IF NOT EXISTS items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "description TEXT,"
                + "sales_id INTEGER,"
                + "products_id INTEGER NOT NULL,"
                + "FOREIGN KEY (sales_id) REFERENCES reprocessed_sales(id),"
                + "FOREIGN KEY(products_id) REFERENCES products(id)"
                + ")";
        sqLiteDatabase.execSQL(items);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS reprocessed_sales");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS items");
        onCreate(sqLiteDatabase);
    }

    public boolean insertSale(SalesModel salesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put("name", salesModel.getName());
        cv.put("email", salesModel.getEmail());
        cv.put("cpf", salesModel.getCpf());
        cv.put("value", salesModel.getValue());
        cv.put("moneyChange", salesModel.getMoneyChange());
        cv.put("users_id", salesModel.getUsers_id());
        long result = db.insert("reprocessed_sales", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int getLastSalesId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(id) FROM reprocessed_sales";
        Cursor cursor = db.rawQuery(query, null);
        int lastSalesId = -1;

        if (cursor.moveToFirst()) {
            lastSalesId = cursor.getInt(0);
        }

        cursor.close();
        return lastSalesId;
    }

    public SalesModel getLastSale() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM reprocessed_sales WHERE id = (SELECT MAX(id) FROM reprocessed_sales)";
        Cursor cursor = db.rawQuery(query, null);

        SalesModel lastSale = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int cpfIndex = cursor.getColumnIndex("cpf");
            int emailIndex = cursor.getColumnIndex("email");
            int valueIndex = cursor.getColumnIndex("value");
            int moneyChangeIndex = cursor.getColumnIndex("moneyChange");
            int userIdIndex = cursor.getColumnIndex("users_id");

            String id = idIndex != -1 ? cursor.getString(idIndex) : "";
            String name = nameIndex != -1 ? cursor.getString(nameIndex) : "";
            String cpf = cpfIndex != -1 ? cursor.getString(cpfIndex) : "";
            String email = emailIndex != -1 ? cursor.getString(emailIndex) : "";
            double value = valueIndex != -1 ? cursor.getDouble(valueIndex) : 0.0;
            double moneyChange = moneyChangeIndex != -1 ? cursor.getDouble(moneyChangeIndex) : 0.0;
            String userId = moneyChangeIndex != -1 ? cursor.getString(userIdIndex) : "";

            lastSale = new SalesModel(id, name, cpf, email, value, moneyChange, userId);
        }

        cursor.close();
        return lastSale;
    }

    public ArrayList<ItemModel> getLastItems(String salesId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM items WHERE sales_id = ? ORDER BY id DESC";
        Cursor cursor = db.rawQuery(query, new String[]{salesId});

        ArrayList<ItemModel> lastItems = new ArrayList<>();
             while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex("id");
                int descriptionIndex = cursor.getColumnIndex("description");
                int salesIdIndex = cursor.getColumnIndex("sales_id");
                int productIdIndex = cursor.getColumnIndex("products_id");

                String id = cursor.getString(idIndex);
                String description = cursor.getString(descriptionIndex);
                String saleId = cursor.getString(salesIdIndex);
                String productId = cursor.getString(productIdIndex);

                ItemModel lastItem = new ItemModel(id, description, saleId, productId);
                lastItems.add(lastItem);
            }


        cursor.close();
        return lastItems;
    }

    public boolean insertItems(ArrayList<ItemModel> itemModels) {
        SQLiteDatabase db = this.getWritableDatabase();
        SalesModel salesModel = getLastSale();

        for (ItemModel itemModel : itemModels) {
            ContentValues cv = new ContentValues();

            cv.put("description", itemModel.getDescription());
            cv.put("sales_id", salesModel.getId());
            long result = db.insert("items", null, cv);

            if (result == -1) {
                return false;
            }
        }

        return true;
    }


    public boolean deleteLastSaleAndItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String selectLastSaleIdQuery = "SELECT MAX(id) FROM reprocessed_sales";
            Cursor cursor = db.rawQuery(selectLastSaleIdQuery, null);

            if (cursor.moveToFirst()) {
                int lastSaleId = cursor.getInt(0);

                String deleteItemsQuery = "DELETE FROM items WHERE sales_id = ?";
                db.execSQL(deleteItemsQuery, new String[]{String.valueOf(lastSaleId)});

                String deleteSaleQuery = "DELETE FROM reprocessed_sales WHERE id = ?";
                db.execSQL(deleteSaleQuery, new String[]{String.valueOf(lastSaleId)});
            }

            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return true;
    }

    public List<SalesModel> getSales() {
        List<SalesModel> salesList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM reprocessed_sales";
        Cursor cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int cpfIndex = cursor.getColumnIndex("cpf");
                    int emailIndex = cursor.getColumnIndex("email");
                    int valueIndex = cursor.getColumnIndex("value");
                    int moneyChangeIndex = cursor.getColumnIndex("moneyChange");
                    int userIdIndex = cursor.getColumnIndex("users_id");

                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    String cpf = cursor.getString(cpfIndex);
                    String email = cursor.getString(emailIndex);
                    String userId = cursor.getString(userIdIndex);
                    double value = Double.parseDouble(cursor.getString(valueIndex));
                    double moneyChange = Double.parseDouble(cursor.getString(moneyChangeIndex));

                    SalesModel salesModel = new SalesModel(id, name, cpf, email, value, moneyChange, userId);
                    salesList.add(salesModel);
                }
        } catch (Exception e){
            Toast.makeText(context, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return salesList;
    }

}
