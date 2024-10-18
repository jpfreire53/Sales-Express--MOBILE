package com.example.salesexpress.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.salesexpress.model.ItemModel;
import com.example.salesexpress.model.ProductModel;
import com.example.salesexpress.model.SalesModel;
import com.example.salesexpress.model.UserModelResponse;

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
                + "date TEXT, "
                + "users_id INTEGER NOT NULL, "
                + "FOREIGN KEY(users_id) REFERENCES users(id)" + ")";;
        sqLiteDatabase.execSQL(query);

        String products = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "description TEXT,"
                + "sku TEXT UNIQUE"
                + ")";
        sqLiteDatabase.execSQL(products);

        String items = "CREATE TABLE IF NOT EXISTS items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "description TEXT,"
                + "sales_id INTEGER,"
                + "products_id INTEGER NOT NULL,"
                + "FOREIGN KEY (sales_id) REFERENCES reprocessed_sales(id),"
                + "FOREIGN KEY(products_id) REFERENCES products(id)"
                + ")";
        sqLiteDatabase.execSQL(items);

        String user = "CREATE TABLE IF NOT EXISTS user   ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user TEXT,"
                + "name TEXT,"
                + "company TEXT,"
                + "cnpj TEXT,"
                + "password TEXT,"
                + "userType TEXT,"
                + "role TEXT"
                + ")";
        sqLiteDatabase.execSQL(user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS reprocessed_sales");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS items");
        onCreate(sqLiteDatabase);
    }

    @SuppressLint("Range")
    public UserModelResponse getUserByCredentials(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM user WHERE user = ? AND password = ?";

        Cursor cursor = db.rawQuery(query, new String[] {username, password});

        if (cursor != null && cursor.moveToFirst()) {

            UserModelResponse user = new UserModelResponse();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setUser(cursor.getString(cursor.getColumnIndex("user")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setCompany(cursor.getString(cursor.getColumnIndex("company")));
            user.setCnpj(cursor.getString(cursor.getColumnIndex("cnpj")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setUserType(cursor.getString(cursor.getColumnIndex("userType")));
            user.setRole(cursor.getString(cursor.getColumnIndex("role")));

            cursor.close();
            return user;
        }

        return null;
    }

    public UserModelResponse getUserById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM user WHERE id = ?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {

            UserModelResponse user = new UserModelResponse();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUser(cursor.getString(cursor.getColumnIndexOrThrow("user")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            user.setCompany(cursor.getString(cursor.getColumnIndexOrThrow("company")));
            user.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow("userType")));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
            cursor.close();
            return user;
        }

        return null;
    }

    public long createUser(String username, String name, String company, String cnpj, String password, String userType, String role) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("user", username);
        values.put("name", name);
        values.put("company", company);
        values.put("cnpj", cnpj);
        values.put("password", password);
        values.put("userType", userType);
        values.put("role", role);

        long result = db.insert("user", null, values);

        return result;
    }

    public List<UserModelResponse> getAllUsers() {
        List<UserModelResponse> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM user";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                UserModelResponse user = new UserModelResponse();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setUser(cursor.getString(cursor.getColumnIndexOrThrow("user")));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                user.setCompany(cursor.getString(cursor.getColumnIndexOrThrow("company")));
                user.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow("userType")));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }


    public boolean insertSale(SalesModel salesModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", salesModel.getName());
        cv.put("email", salesModel.getEmail());
        cv.put("cpf", salesModel.getCpf());
        cv.put("value", salesModel.getValue());
        cv.put("moneyChange", salesModel.getMoneyChange());
        cv.put("date", salesModel.getDate());
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
            int dateIndex = cursor.getColumnIndex("date");
            int userIdIndex = cursor.getColumnIndex("users_id");

            String id = idIndex != -1 ? cursor.getString(idIndex) : "";
            String name = nameIndex != -1 ? cursor.getString(nameIndex) : "";
            String cpf = cpfIndex != -1 ? cursor.getString(cpfIndex) : "";
            String email = emailIndex != -1 ? cursor.getString(emailIndex) : "";
            double value = valueIndex != -1 ? cursor.getDouble(valueIndex) : 0.0;
            double moneyChange = moneyChangeIndex != -1 ? cursor.getDouble(moneyChangeIndex) : 0.0;
            String date = dateIndex != -1 ? cursor.getString(dateIndex) : "";
            String userId = userIdIndex != -1 ? cursor.getString(userIdIndex) : "";

            lastSale = new SalesModel(id, name, cpf, email, value, moneyChange, date, userId);
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
                    int dateIndex = cursor.getColumnIndex("date");
                    int userIdIndex = cursor.getColumnIndex("users_id");

                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    String cpf = cursor.getString(cpfIndex);
                    String email = cursor.getString(emailIndex);
                    String userId = cursor.getString(userIdIndex);
                    String date = cursor.getString(dateIndex);
                    double value = Double.parseDouble(cursor.getString(valueIndex));
                    double moneyChange = Double.parseDouble(cursor.getString(moneyChangeIndex));

                    SalesModel salesModel = new SalesModel(id, name, cpf, email, value, moneyChange, date, userId);
                    salesList.add(salesModel);
                }

        } catch (Exception e){
            Toast.makeText(context, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return salesList;
    }

    public long insertProduct(String name, String description, String sku) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("sku", sku);

        return db.insert("products", null, values);
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String sku = cursor.getString(cursor.getColumnIndexOrThrow("sku"));

                    ProductModel product = new ProductModel(id, name, description, sku);
                    productList.add(product);
                }
            } finally {
                cursor.close();
            }
        }

        return productList;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<ProductModel> getProductByName(String name) {
        List<ProductModel> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM products WHERE name LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + name + "%"});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String sku = cursor.getString(cursor.getColumnIndexOrThrow("sku"));

                ProductModel product = new ProductModel(id, productName, description, sku);
                productList.add(product);
            }
            cursor.close();
        }

        return productList;
    }

    public ProductModel getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM products WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            String productId = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String sku = cursor.getString(cursor.getColumnIndexOrThrow("sku"));

            cursor.close();
            return new ProductModel(productId, name, description, sku);
        }

        return null;
    }

    public boolean updateProductById(int id, String name, String description, String sku) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("sku", sku);

        int rowsAffected = db.update("products", values, "id = ?", new String[]{String.valueOf(id)});

        return rowsAffected > 0;
    }

    public boolean updateProduct(ProductModel product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("sku", product.getSku());

        // Atualiza o produto com base no ID
        int rowsAffected = db.update("products", values, "id = ?", new String[]{String.valueOf(product.getId())});

        return rowsAffected > 0; // Retorna true se o produto foi atualizado com sucesso
    }

}