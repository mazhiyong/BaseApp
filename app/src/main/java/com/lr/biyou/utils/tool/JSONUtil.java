package com.lr.biyou.utils.tool;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lr.biyou.di.component.DaggerNetComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class   JSONUtil {

    @Inject
    Gson gson;

    public static   JSONUtil   JSONUtil;

    public static   JSONUtil getInstance(){
        if(  JSONUtil == null){
              JSONUtil = new   JSONUtil();
        }
        DaggerNetComponent.create().injectTo(  JSONUtil);
        return   JSONUtil;
    }

    public String objectToJson( Object o){
        String ss =  gson.toJson(o);
        return ss;
    }

    public  List<Map<String,Object>> jsonToList(String json){
        List<Map<String,Object>> retList = null;
        try {
            // json转为带泛型的list
            retList = gson.fromJson(json,new TypeToken<List<Map<String,Object>>>() {}.getType());
        }catch (Exception e){
            return retList;
        }
        return retList;
    }

    public  Map<String,Object> jsonMap(String json){
        Map<String,Object> map = null;
        try {
            // json转为带泛型的list
            map = gson.fromJson(json,new TypeToken<Map<String,Object>>() {}.getType());
        }catch (Exception e){
            return map;
        }
        return map;
    }

    public  <T> T JsonToBean(String json, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(json, cls);
        } catch (Exception e) {

        }
        return t;
    }


    public  List<List<String>> jsonToListStr2(String json){
        List<List<String>> retList = null;
        try {
            // json转为带泛型的list
            retList = gson.fromJson(json,new TypeToken<List<List<String>>>() {}.getType());
        }catch (Exception e){
            return retList;
        }
        return retList;
    }

    public  List<String> jsonToListStr(String json){
        List<String> retList = null;
        try {
            // json转为带泛型的list
            retList = gson.fromJson(json,new TypeToken<List<String>>() {}.getType());
        }catch (Exception e){
            return retList;
        }
        return retList;
    }



    public <T>  List<T>  JsonToListBean(String json,  Class<T> cls) {
        List<T>  retList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement element:array){
            retList.add(gson.fromJson(element,cls));
        }
        return retList;
    }

}
