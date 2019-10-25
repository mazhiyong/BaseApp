package cn.wildfire.chat.kit.contact.viewholder.header;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.lr.biyou.R;

import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.contact.UserListAdapter;
import cn.wildfire.chat.kit.contact.model.HeaderValue;


@SuppressWarnings("unused")
@LayoutRes(resId = R.layout.contact_header_channel)
public class ChannelViewHolder extends HeaderViewHolder<HeaderValue> {

    public ChannelViewHolder(Fragment fragment, UserListAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(HeaderValue headerValue) {

    }
}
