package com.example.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper 
{
	private static final String DATABASE = "generic.db";
	private SQLiteDatabase databaseConn = null;

	public DatabaseHelper(Context context) 
	{
		super(context, DATABASE, null, 0);
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Insert MapData into the table
	 * 
	 * @param tableName
	 * @param mapData
	 * @return insert rows count
	 */
	public int insert(String tableName, HashMap<String, String> mapData) 
	{
		databaseConn.insertOrThrow(tableName, null, createContentValues(mapData));
		return 1;
	}

	/**
	 * Delete Data From Table According to WhereCondition
	 * 
	 * @param tableName
	 * @param mapData
	 * @return deleted rows count
	 */
	public int delete(String tableName, String whereConditionString, String[] whereArgs) 
	{
		return databaseConn.delete(tableName, whereConditionString, whereArgs);
	}

	/**
	 * Get Data From from Database in List of HashMap according to condition
	 * 
	 * @param tableName
	 * @param fields
	 * @param mapData
	 * @return List of HashMap Containing Data
	 */
	public ArrayList<HashMap<String, String>> select(boolean isDistinct,
			String tableName, String[] fields, String whereConditionPart,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {

		Cursor cursor = null;
		ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();

		try {
			cursor = databaseConn.query(isDistinct, tableName, fields,
					whereConditionPart, selectionArgs, groupBy, having,
					orderBy, limit);
			cursor.moveToFirst();

			for (int i = 0; i < cursor.getCount(); i++) 
			{
				cursor.moveToPosition(i);
				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < fields.length; j++) 
				{
					map.put(fields[j], cursor.getString(j));
				}
				mapList.add(map);
			}
		} 
		finally 
		{
			cursor.close();
		}

		return mapList;
	}

	/**
	 * Used to get the number of records inside any table.Mainly the query makes
	 * it easy to get the values.
	 * 
	 * @param selectionArgs
	 * @param query
	 * @return {@link Integer}
	 */
	public int rowCount(String[] selectionArgs, String query) {
		int rowCount = (int) DatabaseUtils.longForQuery(databaseConn, query, selectionArgs);
		return rowCount;
	}

	/**
	 * Make Connection with Database
	 * 
	 * @throws IOException
	 */
	public void connect(Context context, String database) throws SQLiteException, IOException 
	{
		databaseConn = context.openOrCreateDatabase(database, Context.MODE_PRIVATE, null);
	}

	/**
	 * DisConnect the Connection With Database
	 * 
	 * @return status
	 */
	public boolean disConnect() throws SQLException 
	{

		if (databaseConn != null) 
		{
			databaseConn.close();
			databaseConn = null;
		}
		return true;
	}

	/**
	 * It Begins the New Transaction
	 */
	public void beginTransaction() 
	{
		databaseConn.beginTransaction();
	}

	/**
	 * It Commits All Database Related Changes
	 * 
	 * @return status
	 */
	public boolean commit() throws IllegalStateException 
	{
		databaseConn.setTransactionSuccessful();
		return true;
	}

	/**
	 * Ends The Transaction
	 * 
	 * @return status
	 */
	public boolean endTransaction() 
	{
		databaseConn.endTransaction();
		return true;
	}

	/**
	 * To Update the Data in Table From Where Condition
	 * 
	 * @param table
	 * @param mapData
	 * @param whereConditionPart
	 * @param whereArgs
	 * @return count of updated rows
	 */
	public int update(String table, HashMap<String, String> mapData, String whereConditionPart, String[] whereArgs) 
	{
		return databaseConn.update(table, createContentValues(mapData), whereConditionPart, whereArgs);
	}

	/**
	 * Execute any Raw Query and return Result in Cursor
	 * 
	 * @param query
	 * @param selectionArgs
	 * @return Cursor of respective data
	 */
	public Cursor executeRawQuery(String query, String[] selectionArgs) 
	{

		return databaseConn.rawQuery(query, selectionArgs);
	}

	/**
	 * Convert HashMap to ContentValues
	 * 
	 * @param column
	 *            HashMap
	 * @return ContentValues
	 */
	protected ContentValues createContentValues(HashMap<String, String> columns) 
	{
		ContentValues values = new ContentValues();
		Iterator<Map.Entry<String, String>> it = columns.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
					.next();
			values.put(pairs.getKey().toString(), pairs.getValue().toString());
		}
		return values;
	}

}
