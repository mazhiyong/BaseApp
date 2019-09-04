package com.lr.biyou.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.lr.biyou.basic.MbsConstans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchData {

	private SQLiteDatabase db;

	private static SearchData sqliteDBHelp;
	private String dbPath = MbsConstans.DATABASE_PATH+"/"+MbsConstans.DATABASE_NAME;

	private SearchData(){
		
	}
	public static SearchData getInstance(){
		if (sqliteDBHelp == null) {
			sqliteDBHelp = new SearchData();
		}
		return sqliteDBHelp;
	}

	public void openDb(){
		db= SQLiteDatabase.openOrCreateDatabase(dbPath, null);
	}


	public void execSQL(String sql) {
		openDb();
		if (db.isOpen()) {
			db.execSQL(sql);
			db.close();
		}
	}
	
	public Map<String, Object> selectByName(String searchName){
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_goods_search where search_name = '"+searchName+"'", null);
			int idIndex=cursor.getColumnIndex("id");
			int searchNameIndex=cursor.getColumnIndex("search_name");
			int updateDateIndex=cursor.getColumnIndex("update_date");
			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				String searchName1 = cursor.getString(searchNameIndex);
				String updateDate = cursor.getString(updateDateIndex);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("searchName", searchName1);
				map.put("updateDate", updateDate);
				return map;
			}
			cursor.close();
			db.close();
		}
		return null;
	}

	public List<Map<String, Object>> selectDB(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_goods_search order by update_date desc", null);
			int idIndex=cursor.getColumnIndex("id");
			int searchNameIndex=cursor.getColumnIndex("search_name");
			int updateDateIndex=cursor.getColumnIndex("update_date");
			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				String searchName1 = cursor.getString(searchNameIndex);
				String updateDate = cursor.getString(updateDateIndex);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("value", searchName1);
				map.put("updateDate", updateDate);
				list.add(map);
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
	
	public void insertDB(String searchName){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("search_name", searchName);
			values.put("update_date", new Date()+"");
			db.insert("tb_goods_search", "id", values);
			db.close();
		}
	}
	
	public void updateDB(String searchName){
		openDb();
		if (db.isOpen()) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("update_date", new Date()+"");
			db.update("tb_goods_search", contentValues, "search_name=? ", new String[]{searchName+""});
			db.close();
		}
	}

	//清空数据
	public void clearData(){
		openDb();
		if (db.isOpen()) {
			String sql = "DELETE FROM tb_goods_search;";
		    db.execSQL(sql);
			db.close();
		}
	}


	public void closeDB(){
		if (db != null && db.isOpen() ) {
			db.close();
		}
	}

}
