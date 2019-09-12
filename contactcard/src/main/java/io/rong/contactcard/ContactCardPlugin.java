package io.rong.contactcard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by Beyond on 2016/11/14.
 */

public class ContactCardPlugin implements IPluginModule {

    private static final int REQUEST_CONTACT = 55;
    private Context context;
    private Conversation.ConversationType conversationType;
    private String targetId;
    public static final String IS_FROM_CARD = "isFromCard";

    public ContactCardPlugin() {
    }

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.rc_contact_plugin_icon);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.rc_plugins_contact);
    }

    @Override
    public void onClick(Fragment currentFragment, RongExtension extension) {
        context = currentFragment.getActivity();
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();

        IContactCardSelectListProvider iContactCardSelectListProvider
                = ContactCardContext.getInstance().getContactCardSelectListProvider();
        IContactCardInfoProvider iContactInfoProvider
                = ContactCardContext.getInstance().getContactCardInfoProvider();
        if (iContactCardSelectListProvider != null) {
            iContactCardSelectListProvider.onContactPluginClick(REQUEST_CONTACT, currentFragment, extension, this);
            extension.collapseExtension();
        } else if (iContactInfoProvider != null) {
            //选择联系人
         /*   Intent intent = new Intent(context, ContactListActivity.class);
            extension.startActivityForPluginResult(intent, REQUEST_CONTACT, this);
            intent.putExtra(IS_FROM_CARD,true);
            extension.collapseExtension();*/


           /* Intent intent = new Intent(context, ContactListActivity.class);
            extension.startActivityForPluginResult(intent, REQUEST_CONTACT, this);
            intent.putExtra(IS_FROM_CARD,true);
            extension.collapseExtension();*/

        } else {
            Toast.makeText(context, "尚未实现\"名片模块\"相关接口", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

}
