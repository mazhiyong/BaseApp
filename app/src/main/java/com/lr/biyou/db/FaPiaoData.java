package com.lr.biyou.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaPiaoData {

	private SQLiteDatabase db;

	private static FaPiaoData sqliteDBHelp;
	private String dbPath = MbsConstans.DATABASE_PATH+"/"+MbsConstans.DATABASE_NAME;

	private FaPiaoData(){
		
	}
	public static FaPiaoData getInstance(){
		if (sqliteDBHelp == null) {
			sqliteDBHelp = new FaPiaoData();
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
	 *  条件查询(判断当前发票数据是否已存在)
	 * @param fp_code  发票代码
	 * @param fp_number 发票号码
	 * @return
	 */
	public boolean dataExist(String fp_code,String fp_number){
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_fapiao_data where fp_code = "+fp_code
					+" and fp_number ="+fp_number, null);
			int idIndex=cursor.getColumnIndex("id");
			int fp_codeIndex=cursor.getColumnIndex("fp_code");
			int fp_moneyIndex=cursor.getColumnIndex("fp_money");
			int fp_numberIndex=cursor.getColumnIndex("fp_number");
			int fp_dateIndex=cursor.getColumnIndex("fp_date");
			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				String code = cursor.getString(fp_codeIndex);
				String money= cursor.getString(fp_moneyIndex);
				String number = cursor.getString(fp_numberIndex);
				String date = cursor.getString(fp_dateIndex);
				if (fp_code.equals(code) && fp_number.equals(number)){

				/*	Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", id);
					map.put("fp_code", code);
					map.put("fp_number",number);
					map.put("fp_money", money);
					map.put("fp_date", date);*/
					cursor.close();
					closeDB();
					return true;
				}
			}
			cursor.close();
			closeDB();
		}
		return false;
	}

	/**
	 * 查询数据库列表
	 * @return
	 */
	public List<Map<String, Object>> selectDB(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_fapiao_data", null);
			int idIndex=cursor.getColumnIndex("id");
			int fp_codeIndex=cursor.getColumnIndex("fp_code");
			int fp_moneyIndex=cursor.getColumnIndex("fp_money");
			int fp_numberIndex=cursor.getColumnIndex("fp_number");
			int fp_dateIndex=cursor.getColumnIndex("fp_date");

			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				String fp_code = cursor.getString(fp_codeIndex);
				String fp_money= cursor.getString(fp_moneyIndex);
				String fp_number = cursor.getString(fp_numberIndex);
				String fp_date = cursor.getString(fp_dateIndex);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("fp_code", fp_code);
				map.put("fp_number",fp_number);
				map.put("fp_money", fp_money);
				map.put("fp_date", fp_date);
				list.add(map);
			}
			cursor.close();
			closeDB();
		}
		return list;
	}
	/**
	 * 批量查询数据库
	 * @return
	 */
	public List<Map<String, Object>> selectDBByListKey(List<Map<String, Object>> keyList){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_fapiao_data", null);
			int idIndex=cursor.getColumnIndex("id");
			int fp_codeIndex=cursor.getColumnIndex("fp_code");
			int fp_moneyIndex=cursor.getColumnIndex("fp_money");
			int fp_numberIndex=cursor.getColumnIndex("fp_number");
			int fp_dateIndex=cursor.getColumnIndex("fp_date");

			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				String fp_code = cursor.getString(fp_codeIndex);
				String fp_money= cursor.getString(fp_moneyIndex);
				String fp_number = cursor.getString(fp_numberIndex);
				String fp_date = cursor.getString(fp_dateIndex);

				for (Map<String,Object> key : keyList){
					String keyCode = key.get("fp_code")+"";
					if (key.equals(fp_code)){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", id);
						map.put("fp_code", fp_code);
						map.put("fp_number",fp_number);
						map.put("fp_money", fp_money);
						map.put("fp_date", fp_date);
						list.add(map);
					}
				}
			}
			cursor.close();
			closeDB();
		}
		return list;
	}


	/**
	 *  插入数据
	 * @param
	 */
	public void insertDB(String fp_code,String fp_number,String fp_money,String fp_date){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("fp_code",fp_code);
			values.put("fp_money",fp_money);
			values.put("fp_number",fp_number);
			values.put("fp_date",fp_date);
			db.insert("tb_fapiao_data", "id", values);
			closeDB();
		}
	}



	/***
	 * 清空数据
	 */
	public void clearData() {
		openDb();
		if (db.isOpen()) {
			String sql = "DELETE FROM tb_fapiao_data;";
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
