package cn.wildfire.chat.kit.contact.newfriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.model.FriendRequest;

public class FriendRequestListFragment extends Fragment {
    @BindView(R.id.noNewFriendLinearLayout)
    LinearLayout noNewFriendLinearLayout;
    @BindView(R.id.newFriendListLinearLayout)
    LinearLayout newFriendLinearLayout;
    @BindView(R.id.friendRequestListRecyclerView)
    RecyclerView recyclerView;

    private ContactViewModel contactViewModel;
    private FriendRequestListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_new_friend_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.userInfoLiveData().observe(this, userInfos -> {
            if (adapter != null) {
                adapter.onUserInfosUpdate(userInfos);
            }
        });

        List<FriendRequest> requests = contactViewModel.getFriendRequest();
        if (requests != null && requests.size() > 0) {
            noNewFriendLinearLayout.setVisibility(View.GONE);
            newFriendLinearLayout.setVisibility(View.VISIBLE);

            adapter = new FriendRequestListAdapter(FriendRequestListFragment.this);
            adapter.setFriendRequests(requests);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
        } else {
            noNewFriendLinearLayout.setVisibility(View.VISIBLE);
            newFriendLinearLayout.setVisibility(View.GONE);
        }
    }
}
