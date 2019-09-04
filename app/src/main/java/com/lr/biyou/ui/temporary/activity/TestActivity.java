package com.lr.biyou.ui.temporary.activity;

import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends BasicActivity implements RequestView {

    public static TestActivity mInstance;
    @Override
    public int getContentView() {
        return R.layout.activity_test;
    }
    @Override
    public void init() {
        mInstance = this;
      /*  Map<String, String> map = new HashMap<>();
        map.put("sort", "desc");
        map.put("page", 1 + "");
        map.put("pagesize", "10");
        map.put("time", new Date().getTime() / 1000 + "");
        map.put("key", "b319ddc604735aaad777d77d46d271e6");


        //weatherPresenterImp.requestPostToMap("c5bb749112664353af44bc99ed263857", "长沙");
        mRequestPresenterImp.requestPostToMap(MethodUrl.imgList, map);




        map.put("sort","desc");
        map.put("page",1+"");
        map.put("pagesize","10");
        map.put("time",new Date().getTime()/1000+"");
        map.put("key","b319ddc604735aaad777d77d46d271e6");*/

        //{
        /*"h2y_app_id": "string",
                "os_pd": {
            "client_version": "string",
                    "os_type": "string",
                    "os_version": "string"
        },
        "pd": {
            "account": "string",
                    "password": "string"
        },
        "token": "string"
    }*/

       /* Map<String,Object> mOsInfo = new HashMap<String,Object>();
        mOsInfo.put("client_version","1");
        mOsInfo.put("os_type","android");
        mOsInfo.put("os_version","7.0");*/

        Map<String,Object> mParamInfo = new HashMap<String,Object>();
        mParamInfo.put("account","17621690984");
        mParamInfo.put("password","123456");
        mParamInfo.put("os_type",MbsConstans.SYS_NAME);
        mParamInfo.put("push_code","");
        mParamInfo.put("push_id","");


       /* Map<String, Object> map = new HashMap<>();
        map.put("os_pd", mOsInfo);
        map.put("pd", mParamInfo);
        map.put("h2y_app_id", "app");
        map.put("token","");*/

       /* Gson gson = new Gson();
        String jsonStr = gson.toJson(map);

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonStr);*/

       // mRequestPresenterImp.requestPostToMap(new HashMap<String, String>(),MethodUrl.LOGIN_ACTION, mParamInfo);


      /*  map.put("sort","desc");
        map.put("page",1+"");
        map.put("pagesize","10");
        map.put("time",new Date().getTime()/1000+"");
        map.put("key","b319ddc604735aaad777d77d46d271e6");

        mRequestPresenterImp.requestPostToMap("http://japi.juhe.cn/joke/"+MethodUrl.contentList, map);*/
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> mData,String mType) {
        Toast.makeText(TestActivity.this,"madfasdf"+mData,Toast.LENGTH_SHORT).show();
        switch (mType){
            case MethodUrl.LOGIN_ACTION:

                break;
        }

    }


    @Override
    public void loadDataError(Map<String,Object> map,String mType) {
        String msg = map.get("msg")+"";
        showToastMsg(msg);
    }
}
