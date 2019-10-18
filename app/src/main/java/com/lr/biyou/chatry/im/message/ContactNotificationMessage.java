package com.lr.biyou.chatry.im.message;

import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

@MessageTag(
        value = "RC:ContactNtf",
        flag = 1
)
public class ContactNotificationMessage extends MessageContent {
    private static final String TAG = "ContactNotificationMessage";
    public static final String CONTACT_OPERATION_REQUEST = "Request";
    public static final String CONTACT_OPERATION_ACCEPT_RESPONSE = "AcceptResponse";
    public static final String CONTACT_OPERATION_REJECT_RESPONSE = "RejectResponse";
    private String operation;
    private String sourceUserId;
    private String targetUserId;
    private String message;
    private String extra;
    public static final Creator<ContactNotificationMessage> CREATOR = new Creator<ContactNotificationMessage>() {
        public ContactNotificationMessage createFromParcel(Parcel source) {
            return new ContactNotificationMessage(source);
        }

        public ContactNotificationMessage[] newArray(int size) {
            return new ContactNotificationMessage[size];
        }
    };

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSourceUserId() {
        return this.sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getTargetUserId() {
        return this.targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ContactNotificationMessage(Parcel in) {
        this.operation = ParcelUtils.readFromParcel(in);
        this.sourceUserId = ParcelUtils.readFromParcel(in);
        this.targetUserId = ParcelUtils.readFromParcel(in);
        this.message = ParcelUtils.readFromParcel(in);
        this.extra = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static ContactNotificationMessage obtain(String operation, String sourceUserId, String targetUserId, String message) {
        ContactNotificationMessage obj = new ContactNotificationMessage();
        obj.operation = operation;
        obj.sourceUserId = sourceUserId;
        obj.targetUserId = targetUserId;
        obj.message = message;
        return obj;
    }

    private ContactNotificationMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.putOpt("operation", this.operation);
            jsonObj.putOpt("sourceUserId", this.sourceUserId);
            jsonObj.putOpt("targetUserId", this.targetUserId);
            if (!TextUtils.isEmpty(this.message)) {
                jsonObj.putOpt("message", this.message);
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.putOpt("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("ContactNotificationMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("ContactNotificationMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public ContactNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("ContactNotificationMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setOperation(jsonObj.optString("operation"));
            this.setSourceUserId(jsonObj.optString("sourceUserId"));
            this.setTargetUserId(jsonObj.optString("targetUserId"));
            this.setMessage(jsonObj.optString("message"));
            this.setExtra(jsonObj.optString("extra"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("ContactNotificationMessage", "JSONException " + var4.getMessage());
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.operation);
        ParcelUtils.writeToParcel(dest, this.sourceUserId);
        ParcelUtils.writeToParcel(dest, this.targetUserId);
        ParcelUtils.writeToParcel(dest, this.message);
        ParcelUtils.writeToParcel(dest, this.extra);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public int describeContents() {
        return 0;
    }
}
