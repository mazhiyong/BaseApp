package cn.wildfire.chat.kit.conversation.ext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import com.lr.biyou.R;

import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfire.chat.kit.location.data.LocationData;
import cn.wildfire.chat.kit.location.ui.activity.MyLocationActivity;
import cn.wildfirechat.model.Conversation;

import static android.app.Activity.RESULT_OK;

public class LocationExt extends ConversationExt {

    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "位置")
    public void pickLocation(View containerView, Conversation conversation) {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!((WfcBaseActivity) activity).checkPermission(permissions)) {
                activity.requestPermissions(permissions, 100);
                return;
            }
        }

        Intent intent = new Intent(activity, MyLocationActivity.class);
        startActivityForResult(intent, 100);
       /* TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_LOCATION);
        messageViewModel.sendMessage(conversation, content);*/



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            LocationData locationData = (LocationData) data.getSerializableExtra("location");
            messageViewModel.sendLocationMessage(conversation, locationData);
        }

    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap.rc_ic_location_normal;
    }

    @Override
    public String title(Context context) {
        return "位置";
    }
}
