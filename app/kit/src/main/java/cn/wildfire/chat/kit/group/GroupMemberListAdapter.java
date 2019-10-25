package cn.wildfire.chat.kit.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lr.biyou.R;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfirechat.model.UserInfo;

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.MemberViewHolder> {
    private List<UserInfo> members;
    private OnMemberClickListener onMemberClickListener;

    public void setMembers(List<UserInfo> members) {
        this.members = members;
    }


    public void addMembers(List<UserInfo> members) {
        int startIndex = this.members.size();
        this.members.addAll(members);
        notifyItemRangeInserted(startIndex, members.size());
    }

    public void updateMember(UserInfo userInfo) {
        if (this.members == null) {
            return;
        }
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).uid.equals(userInfo.uid)) {
                members.set(i, userInfo);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeMembers(List<String> memberIds) {
        Iterator<UserInfo> iterator = members.iterator();
        while (iterator.hasNext()) {
            UserInfo userInfo = iterator.next();
            if (memberIds.contains(userInfo.uid)) {
                iterator.remove();
                memberIds.remove(userInfo.uid);
            }

            if (memberIds.size() == 0) {
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setOnMemberClickListener(OnMemberClickListener onMemberClickListener) {
        this.onMemberClickListener = onMemberClickListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.conversation_item_member_info, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bindUserInfo(members.get(position));
    }

    @Override
    public int getItemCount() {
        if (members == null) {
            return 0;
        }
        return members.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.portraitImageView)
        ImageView portraitImageView;
        @BindView(R.id.nameTextView)
        TextView nameTextView;
        private UserInfo userInfo;

        @OnClick(R.id.portraitImageView)
        void onClick() {
            if (onMemberClickListener == null) {
                return;
            }
            if (userInfo != null) {
                onMemberClickListener.onUserMemberClick(userInfo);
            }
        }

        public MemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindUserInfo(UserInfo userInfo) {
            if (userInfo == null) {
                nameTextView.setText("");
                portraitImageView.setImageResource(R.mipmap.default_header);
                return;
            }
            this.userInfo = userInfo;
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(userInfo.displayName);
            Glide.with(portraitImageView).load(userInfo.portrait).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.default_header)).into(portraitImageView);
        }

    }

    public interface OnMemberClickListener {
        void onUserMemberClick(UserInfo userInfo);
    }
}
