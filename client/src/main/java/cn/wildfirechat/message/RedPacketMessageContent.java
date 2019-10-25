package cn.wildfirechat.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.wildfirechat.message.core.ContentTag;
import cn.wildfirechat.message.core.MessageContentType;
import cn.wildfirechat.message.core.MessagePayload;
import cn.wildfirechat.message.core.PersistFlag;

/**
 * Created by heavyrain lee on 2017/12/6.
 *
 */

@ContentTag(type = MessageContentType.getContentType_ImageRed, flag = PersistFlag.Persist_And_Count)
public class RedPacketMessageContent extends MessageContent {
    private String content;
    public String cid;
    public String redPackType;

    public RedPacketMessageContent() {

    }

    public RedPacketMessageContent(String content) {
        this.content = content;
       /* this.cid =cid;
        this.redPackType = type;*/
    }

    protected RedPacketMessageContent(Parcel in) {
        super(in);
        this.content = in.readString();
        this.redPackType = in.readString();
        this.cid = in.readString();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public MessagePayload encode() {
        MessagePayload payload = new MessagePayload();
        payload.searchableContent = content;
      /*  payload.redPackType = redPackType;
        payload.cid = cid;*/
        payload.mentionedType = mentionedType;
        payload.mentionedTargets = mentionedTargets;
        try {
            JSONObject objWrite = new JSONObject();
            objWrite.put("cid",cid);
            objWrite.put("redPackType", redPackType);
            payload.binaryContent = objWrite.toString().getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return payload;
    }


    @Override
    public void decode(MessagePayload payload) {
        content = payload.searchableContent;
       /* payload.redPackType = redPackType;
        payload.cid = cid;*/
        mentionedType = payload.mentionedType;
        mentionedTargets = payload.mentionedTargets;

        Log.i("show","binaryContent:"+new String(payload.binaryContent));
        try {
            if (payload.binaryContent != null) {
                JSONObject jsonObject = new JSONObject(new String(payload.binaryContent));
                cid = jsonObject.optString(("cid"));
                redPackType = jsonObject.optString("redPackType");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String digest(Message message) {
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
        dest.writeString(this.redPackType);
        dest.writeString(this.cid);

    }


    public static final Creator<RedPacketMessageContent> CREATOR = new Creator<RedPacketMessageContent>() {
        @Override
        public RedPacketMessageContent createFromParcel(Parcel source) {
            return new RedPacketMessageContent(source);
        }

        @Override
        public RedPacketMessageContent[] newArray(int size) {
            return new RedPacketMessageContent[size];
        }
    };


}
