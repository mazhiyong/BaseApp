package com.lr.biyou.chatry.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.ui.adapter.CommonListAdapter;
import com.lr.biyou.chatry.ui.adapter.models.ListItemModel;
import com.lr.biyou.chatry.viewmodel.CommonListBaseViewModel;
import com.lr.biyou.chatry.viewmodel.ForwardSelectedDetailViewModel;

import java.util.ArrayList;

public class ForwardSelectedDetailFragment extends CommonListBaseFragment {

    private ArrayList<GroupEntity> selectedGroup;
    private ArrayList<FriendShipInfo>  selectedFriends;
    private ForwardSelectedDetailViewModel forwardSelectedDetailViewModel;
    private OnLeftSelectedListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedGroup = getActivity().getIntent().getParcelableArrayListExtra(IntentExtra.GROUP_LIST);
        selectedFriends = getActivity().getIntent().getParcelableArrayListExtra(IntentExtra.FRIEND_LIST);

        setOnItemClickListener(new CommonListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, ListItemModel data) {
                final ListItemModel.ItemView.Type type = data.getItemView().getType();
                switch (type) {
                    case GROUP:
                        final GroupEntity groupEntity = (GroupEntity) data.getData();
                        removeAndUpdate(groupEntity);
                        break;
                    case FRIEND:
                        final FriendShipInfo friendShipInfo = (FriendShipInfo) data.getData();
                        removeAndUpdate(friendShipInfo);
                        break;
                    default:
                        //DO Nothing
                        break;
                }


                forwardSelectedDetailViewModel.loadData(selectedFriends, selectedGroup);
                if (listener != null) {
                    listener.onLeftSelected(selectedFriends, selectedGroup);
                }
            }
        });
    }

    @Override
    protected CommonListBaseViewModel createViewModel() {
        forwardSelectedDetailViewModel = ViewModelProviders.of(this).get(ForwardSelectedDetailViewModel.class);
        return forwardSelectedDetailViewModel;
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
        forwardSelectedDetailViewModel.loadData(selectedFriends, selectedGroup);
    }


    private void removeAndUpdate(GroupEntity group) {
        if (selectedGroup != null && selectedGroup.size() > 0 && group != null) {
            selectedGroup.remove(group);
        }
    }

    private void removeAndUpdate(FriendShipInfo friendShipInfo) {
        if (selectedFriends != null && selectedFriends.size() > 0 && friendShipInfo != null) {
            selectedFriends.remove(friendShipInfo);
        }
    }

    public void setOnLeftSelectedListener(OnLeftSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnLeftSelectedListener {
        void onLeftSelected(ArrayList<FriendShipInfo> friendShipInfos, ArrayList<GroupEntity> groupEntities);
    }
}
