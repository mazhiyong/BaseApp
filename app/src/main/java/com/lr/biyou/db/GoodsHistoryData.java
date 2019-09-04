package com.lr.biyou.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.lr.biyou.basic.MbsConstans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsHistoryData {

	private SQLiteDatabase db;

	private static GoodsHistoryData sqliteDBHelp;
	private String dbPath = MbsConstans.DATABASE_PATH+"/"+MbsConstans.DATABASE_NAME;

	private GoodsHistoryData(){
		
	}
	public static GoodsHistoryData getInstance(){
		if (sqliteDBHelp == null) {
			sqliteDBHelp = new GoodsHistoryData();
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
	
	public Map<String, Object> selectById(int goodsId){
		openDb();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_goods_cart where goods_id = "+goodsId+" and is_work = 0", null);
			int idIndex=cursor.getColumnIndex("id");
			int goodsIdIndex=cursor.getColumnIndex("goods_id");
			int goodsCountIndex=cursor.getColumnIndex("goods_count");
			int goodsAmountIndex=cursor.getColumnIndex("goods_price");
			int isWorkIndex=cursor.getColumnIndex("is_work");
			int isDeleteIndex=cursor.getColumnIndex("is_delete");
			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				int gid = cursor.getInt(goodsIdIndex);
				int goodsCount = cursor.getInt(goodsCountIndex);
				String goodsAmount = cursor.getString(goodsAmountIndex);
				int isWork = cursor.getInt(isWorkIndex);
				int isDelete = cursor.getInt(isDeleteIndex);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("gid", gid);
				map.put("gcount", goodsCount);
				map.put("goods_price", goodsAmount);
				map.put("is_work", isWork);
				map.put("is_delete", isDelete);
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
			Cursor cursor = db.rawQuery("select * from tb_goods_history", null);
			int idIndex=cursor.getColumnIndex("id");
			int goodsIdIndex=cursor.getColumnIndex("goods_id");
			/*int goodsCountIndex=cursor.getColumnIndex("goods_count");
			int goodsAmountIndex=cursor.getColumnIndex("goods_price");
			int isWorkIndex=cursor.getColumnIndex("is_work");
			int isDeleteIndex=cursor.getColumnIndex("is_delete");*/
			while (cursor.moveToNext()) {
				int id = cursor.getInt(idIndex);
				int goodsId = cursor.getInt(goodsIdIndex);
				/*int goodsCount = cursor.getInt(goodsCountIndex);
				String goodsAmount = cursor.getString(goodsAmountIndex);
				int isWork = cursor.getInt(isWorkIndex);
				int isDelete = cursor.getInt(isDeleteIndex);*/
				
				Map<String, Object> map = new HashMap<String, Object>();
				//map.put("id", id);
				map.put("goodsId", goodsId);
				//map.put("goodsCount", goodsCount);
				//map.put("goods_price", goodsAmount);
				//map.put("is_work", isWork);
				//map.put("is_delete", isDelete);
				list.add(map);
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
	
	public void insertDB(int goodsId){
		openDb();
		if (db.isOpen()) {
			ContentValues values=new ContentValues();
			values.put("goods_id", goodsId);
			db.insert("tb_goods_cart", "id", values);
			db.close();
		}
	}

	public void updateDB(int goodsCount,int goodsId){
		openDb();
		if (db.isOpen()) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("goods_count", goodsCount);
			db.update("tb_goods_cart", contentValues, "goods_id=? ", new String[]{goodsId+""});
			db.close();
		}
	}
	
	public void updateStatus(List<Map<String, Object>> list, int isWork){
		openDb();
		if (db.isOpen()) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				//ContentValues contentValues = new ContentValues();
				//contentValues.put("is_work", isWork);
				//db.update("tb_goods_cart", contentValues, "goods_id=?", new String[]{map.get("goodsId")+""});
				//删除数据
				db.delete("tb_goods_cart", "goods_id=?",new String[]{map.get("goodsId")+""});
			}
			db.close();
		}
	}
	

	public void closeDB(){
		if (db != null && db.isOpen() ) {
			db.close();
		}
	}

}
