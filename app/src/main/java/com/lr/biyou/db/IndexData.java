package com.lr.biyou.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;

import java.util.Map;

/**
 * 
 * @Description:首页缓存本地数据库信息
 *
 */
public class IndexData {

	private SQLiteDatabase db;

	private static IndexData sqliteDBHelp;
	private String dbPath = MbsConstans.DATABASE_PATH+"/"+MbsConstans.DATABASE_NAME;

	private IndexData(){
		
	}
	public static IndexData getInstance(){
		if (sqliteDBHelp == null) {
			sqliteDBHelp = new IndexData();
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
	
	/**
	 * 得到某个字段的值
	 * @param key
	 * @return
	 */
	public String selectIndex(String key){
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select "+key+" from tb_index_data where id = (select MAX(id) from tb_index_data) ", null);
			int valueIndex=cursor.getColumnIndex(key);
			while (cursor.moveToNext()) {
				String value = cursor.getString(valueIndex);
				return value;
			}
			cursor.close();
			db.close();
		}
		return null;
	}

	/**
	 * 插入本地数据库
	 * @param map
	 */
	public void insertDB(Map<String, Object> map){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("advert_json", map.get("advertJson")+"");
			values.put("content_json", map.get("nameCodeJson")+"");
			db.insert("tb_index_data", "id", values);
			db.close();
		}
	}
	
	/**
	 * 更新本地数据库首页信息，如果没有老数据的话，插入一条新的数据
	 * 如果有老数据的话，更新老数据，理想状态下，有且只有一条数据
	 * @param key
	 * @param value
	 */
	public void updateDB(String key, String value){
		openDb();

		LogUtilDebug.i("打印log日志","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		if (db.isOpen()) {
			Cursor cursor=db.rawQuery("select * from tb_index_data",null);
			if(cursor.getCount()==0){
				ContentValues values=new ContentValues();
				values.put(key, value);
				db.insert("tb_index_data", "id", values);
				db.close();
			}else {
				ContentValues contentValues = new ContentValues();
				contentValues.put(key, value);
				db.update("tb_index_data", contentValues,null, null);
				db.close();
			}
		}
	}

	//清空数据
	public void clearData(){
		openDb();
		if (db.isOpen()) {
			String sql = "DELETE FROM tb_index_data;";
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
