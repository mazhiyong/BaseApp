package cn.wildfire.chat.kit.contact.newfriend;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lr.biyou.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.model.FriendRequest;
import cn.wildfirechat.model.UserInfo;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    private FriendRequestListFragment fragment;
    private FriendRequestListAdapter adapter;
    private FriendRequest friendRequest;
    private UserViewModel userViewModel;
    private ContactViewModel contactViewModel;

    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.introTextView)
    TextView introTextView;
    @BindView(R.id.acceptButton)
    Button acceptButton;
    @BindView(R.id.acceptStatusTextView)
    TextView acceptStatusTextView;

    public FriendRequestViewHolder(FriendRequestListFragment fragment, FriendRequestListAdapter adapter, View itemView) {
        super(itemView);
        this.fragment = fragment;
        this.adapter = adapter;
        ButterKnife.bind(this, itemView);
        userViewModel =ViewModelProviders.of(fragment).get(UserViewModel.class);
        contactViewModel = ViewModelProviders.of(fragment).get(ContactViewModel.class);
    }

    @OnClick(R.id.acceptButton)
    void accept() {
        contactViewModel.acceptFriendRequest(friendRequest.target).observe(fragment, aBoolean -> {
            if (aBoolean) {
                this.friendRequest.status = 1;
                acceptButton.setVisibility(View.GONE);
            } else {
                Toast.makeText(fragment.getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBind(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
        UserInfo userInfo = userViewModel.getUserInfo(friendRequest.target, false);

        if (userInfo != null && !TextUtils.isEmpty(userInfo.displayName)) {
            nameTextView.setText(userInfo.displayName);
        } else {
            nameTextView.setText("<" + friendRequest.target + ">");
        }
        if (!TextUtils.isEmpty(friendRequest.reason)) {
            introTextView.setText(friendRequest.reason);
        }
        // TODO status

        switch (friendRequest.status) {
            case 0:
                acceptButton.setVisibility(View.VISIBLE);
                acceptStatusTextView.setVisibility(View.GONE);
                break;
            case 1:
                acceptButton.setVisibility(View.GONE);
                acceptStatusTextView.setText("已添加");
                break;
            default:
                acceptButton.setVisibility(View.GONE);
                acceptStatusTextView.setText("已拒绝");
                break;
        }

        if (userInfo != null) {
            Glide.with(fragment).load(userInfo.portrait).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.avatar_def).transforms(new CenterCrop(), new RoundedCorners(10)).centerCrop()).into(portraitImageView);

        }
    }

}
