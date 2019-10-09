package com.lr.biyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.lr.biyou.utils.tool.LogUtilDebug;

/**
 * 
* @description: 数据库操作的类
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	

	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		LogUtilDebug.i("show","SQLLite  OnCreate");
		//创建购物车信息表
		db.execSQL("create table if not exists tb_goods_cart(id integer primary key,goods_id integer,goods_count integer,goods_price text,is_work integer,is_delete integer,if_activity text,is_gift text)");
		//创建搜索表
		db.execSQL("create table if not exists tb_goods_search(id integer primary key,search_name text,update_date text)");
		//创建商品浏览记录表
		db.execSQL("create table if not exists tb_goods_history(id integer primary key,goods_id integer)");
		//创建首页缓存表
		db.execSQL("create table if not exists tb_index_data(id integer primary key,advert_json text,content_json text)");
		//创建发票信息表
		db.execSQL("create table if not exists tb_fapiao_data(id integer primary key,fp_code text,fp_money text,fp_number text,fp_date text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {// 如果有字段变动
		for (int i = oldVersion + 1; i <= newVersion; i++) {// 迭代升级(跨版本升级)
			switch (i) {
			case 2:// 数据库版本号为2时
				//String sql1 = "ALTER TABLE tb_goods_cart ADD COLUMN if_activity varchar(20)";
				//db.execSQL(sql1);
				//String sql2 = "ALTER TABLE tb_goods_cart ADD COLUMN is_gift varchar(20)";
				//db.execSQL(sql2);
				break;
			case 3:// 数据库版本号为3时
				//String sql1 = "ALTER TABLE tb_goods_cart ADD COLUMN ss varchar(20)";
				//db.execSQL(sql1);
				break;
			case 4:// 数据库版本号为4时
				break;
			case 5:// 数据库版本号为5时
				break;
			case 6:
				break;
			}

		}
		onCreate(db);// 重新执行oncreate初始化数据库 注意版本号的变动 要大于oldversion
	}
}
