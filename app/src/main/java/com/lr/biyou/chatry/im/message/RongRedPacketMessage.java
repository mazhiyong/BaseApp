package com.lr.biyou.chatry.im.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * 自定义融云IM消息类
 *
 * @author lsy
 * @date 2015-11-19
 *
 * MessageTag 中 flag 中参数的含义：
 * 1.NONE，空值，不表示任何意义.在会话列表不会显示出来。
 * 2.ISPERSISTED，消息需要被存储到消息历史记录。
 * 3.ISCOUNTED，消息需要被记入未读消息数。
 *
 * value：消息对象名称。
 * 请不要以 "RC:" 开头， "RC:" 为官方保留前缀。
 */

@MessageTag(value = "LR:redPackMsg", flag = MessageTag.ISPERSISTED)
public class RongRedPacketMessage extends MessageContent {

    //红包id 类似于messageId
    private String id;
    //融云id(用户id或是群组id)
    //private String userid;
    //备注提示信息 :红包(恭喜发财)  转账(转账 1000)
    private String message;
    //状态红包(待领取   已领取)  转账(待确认   已收账)
    //private String status;
    //类型 1 红包  2转账
    private String type;



    public RongRedPacketMessage() {

    }

    public static RongRedPacketMessage obtain(String type,String id, String message) {
        RongRedPacketMessage rongRedPacketMessage = new RongRedPacketMessage();
        rongRedPacketMessage.type = type;
        rongRedPacketMessage.id = id;
       // rongRedPacketMessage.userid = userid;
        rongRedPacketMessage.message = message;
        //rongRedPacketMessage.status = status;
        return rongRedPacketMessage;
    }

    // 给消息赋值
    public RongRedPacketMessage(byte[] data) {

        try {
            String jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setType(jsonObj.getString("type"));
            setId(jsonObj.getString("id"));
            //setUserid(jsonObj.getString("userid"));
            setMessage(jsonObj.getString("message"));
            //setStatus(jsonObj.getString("status"));
            /*if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }*/
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (UnsupportedEncodingException e1) {

        }
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public RongRedPacketMessage(Parcel in) {
        setType(ParcelUtils.readFromParcel(in));
        setId(ParcelUtils.readFromParcel(in));
        //setStatus(ParcelUtils.readFromParcel(in));
        //setUserid(ParcelUtils.readFromParcel(in));
        setMessage(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<RongRedPacketMessage> CREATOR = new Creator<RongRedPacketMessage>() {

        @Override
        public RongRedPacketMessage createFromParcel(Parcel source) {
            return new RongRedPacketMessage(source);
        }

        @Override
        public RongRedPacketMessage[] newArray(int size) {
            return new RongRedPacketMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 这里可继续增加你消息的属性
        ParcelUtils.writeToParcel(dest, type);
        ParcelUtils.writeToParcel(dest, id);
        ParcelUtils.writeToParcel(dest, message);
        //ParcelUtils.writeToParcel(dest, userid);
        //ParcelUtils.writeToParcel(dest, status);
        ParcelUtils.writeToParcel(dest, getUserInfo());

    }

    /**
     * 将消息属性封装成 json 串，再将 json 串转成 byte 数组，该方法会在发消息时调用
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type",type);
            jsonObj.put("id", id);
            //jsonObj.put("userid", userid);
            jsonObj.put("message", message);
            //jsonObj.put("status",status);

           /* if (getJSONUserInfo() != null)
                jsonObj.putOpt("user", getJSONUserInfo());*/

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }


        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

   /* public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }*/

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

 /*   public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }*/

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
