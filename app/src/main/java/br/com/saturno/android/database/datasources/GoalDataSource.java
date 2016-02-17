package br.com.saturno.android.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.saturno.android.database.DatabaseHelper;
import br.com.saturno.android.database.entities.Goal;

/**
 * Created by andre on 05/11/2015.
 */
public class GoalDataSource extends DatabaseHelper implements DataSource<Goal> {

    public GoalDataSource(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    // Adding new goal
    @Override
    public boolean add(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GOAL_KEY_START_DATE, goal.getStartDate());
        values.put(GOAL_KEY_END_DATE, goal.getEndDate());
        values.put(GOAL_KEY_VALUE, goal.getValue());
        values.put(GOAL_KEY_CARDID, goal.getCardId());

        try {
            // Inserting Row
            db.insertOrThrow(TABLE_GOAL, null, values);
        }catch (SQLException e){
            Log.e("GoalDataSource", "Insert Error - " + e);
            return false;
        }finally {
            db.close(); // Closing database connection
        }
        return true;
    }

    // Getting single goal
    @Override
    public Goal get(Goal goal) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GOAL, new String[]{GOAL_KEY_ID,
                        GOAL_KEY_START_DATE, GOAL_KEY_END_DATE, GOAL_KEY_VALUE,GOAL_KEY_CARDID}, GOAL_KEY_ID + "=?",
                new String[]{String.valueOf(goal.getId())}, null, null, null, null);
        if (cursor.moveToFirst()){
            goal.setId(cursor.getInt(cursor.getColumnIndex(GOAL_KEY_ID)));
            goal.setStartDate(cursor.getString(cursor.getColumnIndex(GOAL_KEY_START_DATE)));
            goal.setEndDate(cursor.getString(cursor.getColumnIndex(GOAL_KEY_END_DATE)));
            goal.setValue(cursor.getDouble(cursor.getColumnIndex(GOAL_KEY_VALUE)));
            goal.setCardId(cursor.getInt(cursor.getColumnIndex(GOAL_KEY_CARDID)));
        }
        db.close();
        // return goal
        return goal;
    }

    // Getting All goal
    @Override
    public List<Goal> getAll() {
        List<Goal> goalList = new ArrayList<Goal>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GOAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Goal goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndex(GOAL_KEY_ID)));
                goal.setStartDate(cursor.getString(cursor.getColumnIndex(GOAL_KEY_START_DATE)));
                goal.setEndDate(cursor.getString(cursor.getColumnIndex(GOAL_KEY_END_DATE)));
                goal.setValue(cursor.getDouble(cursor.getColumnIndex(GOAL_KEY_VALUE)));
                goal.setCardId(cursor.getInt(cursor.getColumnIndex(GOAL_KEY_CARDID)));
                // Adding contact to list
                goalList.add(goal);
            } while (cursor.moveToNext());
        }
        db.close();
        // return goal list
        return goalList;
    }

    // Updating single goal
    @Override
    public int update(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GOAL_KEY_START_DATE, goal.getStartDate());
        values.put(GOAL_KEY_END_DATE, goal.getEndDate());
        values.put(GOAL_KEY_VALUE, goal.getValue());
        values.put(GOAL_KEY_CARDID, goal.getCardId());

        // updating row
        int affectedRows = db.update(TABLE_GOAL, values, GOAL_KEY_ID + " = ?",
                new String[] { String.valueOf(goal.getId()) });
        db.close();
        return affectedRows;
    }

    // Deleting single goal
    @Override
    public int delete(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE_GOAL, GOAL_KEY_ID + " = ?",
                new String[] { String.valueOf(goal.getId()) });
        db.close();
        return affectedRows;
    }

    // Getting categories Count
    @Override
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GOAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
}
