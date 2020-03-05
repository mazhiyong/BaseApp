package com.lr.biyou.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.HashMap;
import java.util.Map;

/**
 * 红包数据
 */
public class RedHistoryData {

	private SQLiteDatabase db;

	private static RedHistoryData sqliteDBHelp;
	private String dbPath = MbsConstans.DATABASE_PATH+"/"+MbsConstans.DATABASE_NAME;

	private RedHistoryData(){
		
	}
	public static RedHistoryData getInstance(){
		if (sqliteDBHelp == null) {
			sqliteDBHelp = new RedHistoryData();
		}
		return sqliteDBHelp;
	}

	/**
	 * 打开数据库
	 */
	public void openDb(){


		db= SQLiteDatabase.openOrCreateDatabase(dbPath, null);
	}


	/**
	 *  执行SQL语句
	 * @param sql
	 */
	public void execSQL(String sql) {
		openDb();
		if (db.isOpen()) {
			db.execSQL(sql);
			closeDB();
		}
	}

	/**
	 *  条件查询(判断当前用户是否已经存在记录)
	 * @param account  用户账号
	 * @param rcid 对方id
	 * @return
	 */
	public Map<String, Object> queryRedData(String account,String rcid ){
		openDb();
		Map<String, Object> map = null;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_redpackage_data where account "+"='"+account+"'"
					+" and rcid "+"= '"+rcid+"'", null);
			int idIndex=cursor.getColumnIndex("id");
			int accountIndex=cursor.getColumnIndex("account");
			int rcidIndex=cursor.getColumnIndex("rcid");
			int redIndex=cursor.getColumnIndex("red");
			int transferIndex=cursor.getColumnIndex("transfer");
			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				String code = cursor.getString(accountIndex);
				String money= cursor.getString(redIndex);
				String number = cursor.getString(rcidIndex);
				String date = cursor.getString(transferIndex);
				if (account.equals(code) && rcid.equals(number)){
					map = new HashMap<String, Object>();
					map.put("id", id);
					map.put("account", code);
					map.put("rcid",number);
					map.put("red", money);
					map.put("transfer", date);
					cursor.close();
					closeDB();
				}
			}
			cursor.close();
			closeDB();
		}
		return map;
	}


	/**
	 *  更新数据
	 * @return
	 */
	public void updateRedData(String red,String id){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("red",red);
			db.update("tb_redpackage_data", values, "id=?", new String[]{id});
			closeDB();
		}
	}

	/**
	 *  更新数据
	 * @return
	 */
	public void updateTransferData(String transfer,String id){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("transfer",transfer);
			db.update("tb_redpackage_data", values, "id=?", new String[]{id});
			LogUtilDebug.i("show","更新成功");
			closeDB();
		}
	}


	/**
	 *  插入数据
	 * @param
	 */
	public void insertRedData(String account,String rcid,String red,String transfer){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("account",account);
			values.put("rcid",rcid);
			values.put("red",red);
			values.put("transfer",transfer);
			db.insert("tb_redpackage_data", "id", values);
			closeDB();
		}
	}






	/***
	 * 清空数据
	 */
	public void clearData() {
		openDb();
		if (db.isOpen()) {
			String sql = "DELETE FROM tb_redpackage_data;";
			db.execSQL(sql);
			LogUtilDebug.i("show","清空成功");
			closeDB();
		}
	}


	/**
	 * 关闭数据库
	 */
	public void closeDB(){
		if (db != null && db.isOpen() ) {
			db.close();
		}
	}


}
