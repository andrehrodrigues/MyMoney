package br.com.saturno.android.database.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.saturno.android.database.DatabaseHelper;
import br.com.saturno.android.database.entities.Category;

public class CategoryDataSource extends DatabaseHelper implements DataSource<Category> {
	public CategoryDataSource(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// Adding new category
	@Override
	public boolean add(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CATEGORY_KEY_NAME, category.getName()); // Category Name

        try {
            // Inserting Row
            db.insertOrThrow(TABLE_CATEGORY, null, values);
        }catch (SQLException e){
            Log.e("CategoryDataSource", "Insert Error - " + e);
			return false;
        }finally {
            db.close(); // Closing database connection
        }
		return true;
	}

	// Getting single category by id
    @Override
	public Category get(Category category) {
		SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(category.getId() != 0 ){
		cursor = db.query(TABLE_CATEGORY, new String[] {
				CATEGORY_KEY_ID, CATEGORY_KEY_NAME }, CATEGORY_KEY_ID + "=?",
				new String[] { String.valueOf(category.getId()) }, null, null, null, null);
        }else{
            if(category.getName() != null){
                cursor = db.query(TABLE_CATEGORY, new String[] {
                                CATEGORY_KEY_ID, CATEGORY_KEY_NAME }, CATEGORY_KEY_NAME + "=?",
                        new String[] { String.valueOf(category.getName()) }, null, null, null, null);
            }
        }
		if (cursor.moveToFirst()){
            category.setId(cursor.getInt(cursor.getColumnIndex(CATEGORY_KEY_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(CATEGORY_KEY_NAME)));
        }

		db.close();
		// return contact
		return category;
	}

	// Getting All categories
	@Override
    public List<Category> getAll() {
		List<Category> categoryList = new ArrayList<Category>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Category category = new Category();
				category.setId(cursor.getInt(cursor.getColumnIndex(CATEGORY_KEY_ID)));
				category.setName(cursor.getString(cursor.getColumnIndex(CATEGORY_KEY_NAME)));
				// Adding contact to list
				categoryList.add(category);
			} while (cursor.moveToNext());
		}

		db.close();
		// return category list
		return categoryList;
	}

	// Updating single category
    @Override
	public int update(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CATEGORY_KEY_NAME, category.getName());

		// updating row
		int affectedRows = db.update(TABLE_CATEGORY, values, CATEGORY_KEY_ID + " = ?",
				new String[] { String.valueOf(category.getId()) });
		db.close();
		return affectedRows;
	}

	// Deleting single category
    @Override
	public int delete(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();
		int affectedRows = db.delete(TABLE_CATEGORY, CATEGORY_KEY_ID + " = ?",
				new String[] { String.valueOf(category.getId()) });
		db.close();
		return affectedRows;
	}

	// Getting categories Count
    @Override
	public int getCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CATEGORY;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		// return count
		return cursor.getCount();
	}

}
