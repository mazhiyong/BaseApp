package cn.wildfire.chat.kit.contact;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.wildfire.chat.kit.contact.model.FooterValue;
import cn.wildfire.chat.kit.contact.model.HeaderValue;
import cn.wildfire.chat.kit.contact.model.UIUserInfo;
import cn.wildfire.chat.kit.contact.viewholder.footer.FooterViewHolder;
import cn.wildfire.chat.kit.contact.viewholder.header.HeaderViewHolder;
import cn.wildfire.chat.kit.widget.ProgressFragment;
import cn.wildfire.chat.kit.widget.QuickIndexBar;


public abstract class BaseUserListFragment extends ProgressFragment implements QuickIndexBar.OnLetterUpdateListener, UserListAdapter.OnUserClickListener, UserListAdapter.OnHeaderClickListener, UserListAdapter.OnFooterClickListener {

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;
    @BindView(R.id.quickIndexBar)
    QuickIndexBar quickIndexBar;
    @BindView(R.id.indexLetterTextView)
    TextView indexLetterTextView;

    protected UserListAdapter userListAdapter;

    private LinearLayoutManager linearLayoutManager;
    protected ContactViewModel contactViewModel;
    private boolean showQuickIndexBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactViewModel = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);
    }

    @Override
    protected int contentLayout() {
        return getContentLayoutResId();
    }

    @Override
    protected void afterViews(View view) {
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {

        userListAdapter = onCreateUserListAdapter();
        userListAdapter.setOnUserClickListener(this);
        userListAdapter.setOnHeaderClickListener(this);
        userListAdapter.setOnFooterClickListener(this);

        initHeaderViewHolders();
        initFooterViewHolders();

        usersRecyclerView.setAdapter(userListAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        usersRecyclerView.setLayoutManager(linearLayoutManager);

        if (showQuickIndexBar) {
            quickIndexBar.setVisibility(View.VISIBLE);
            quickIndexBar.setOnLetterUpdateListener(this);
        } else {
            quickIndexBar.setVisibility(View.GONE);
        }
    }

    public void initHeaderViewHolders() {
        // no header
    }

    public void initFooterViewHolders() {
        // no footer
    }

    /**
     * 一定要包含一个id为contactRecyclerView的RecyclerView
     *
     * @return
     */
    protected int getContentLayoutResId() {
        return R.layout.contact_contacts_fragment;
    }

    /**
     * the data for this userListAdapter should be set here
     *
     * @return
     */
    protected UserListAdapter onCreateUserListAdapter() {
        userListAdapter = new UserListAdapter(this);
        return userListAdapter;
    }

    protected void addHeaderViewHolder(Class<? extends HeaderViewHolder> clazz, HeaderValue value) {
        userListAdapter.addHeaderViewHolder(clazz, value);
        // to do notify header changed
    }

    protected void addFooterViewHolder(Class<? extends FooterViewHolder> clazz, FooterValue value) {
        userListAdapter.addFooterViewHolder(clazz, value);
    }

    @Override
    public void onLetterUpdate(String letter) {
        indexLetterTextView.setVisibility(View.VISIBLE);
        indexLetterTextView.setText(letter);

        if ("↑".equalsIgnoreCase(letter)) {
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
        } else if ("☆".equalsIgnoreCase(letter)) {
            linearLayoutManager.scrollToPositionWithOffset(userListAdapter.headerCount(), 0);
        } else if ("#".equalsIgnoreCase(letter)) {
            List<UIUserInfo> data = userListAdapter.getUsers();
            for (int i = 0; i < data.size(); i++) {
                UIUserInfo friend = data.get(i);
                if (friend.getCategory().equals("#")) {
                    linearLayoutManager.scrollToPositionWithOffset(userListAdapter.headerCount() + i, 0);
                    break;
                }
            }
        } else {
            List<UIUserInfo> data = userListAdapter.getUsers();
            for (int i = 0; i < data.size(); i++) {
                UIUserInfo friend = data.get(i);
                if (friend.getCategory().compareTo(letter) >= 0) {
                    linearLayoutManager.scrollToPositionWithOffset(i + userListAdapter.headerCount(), 0);
                    break;
                }
            }
        }
    }

    @Override
    public void onLetterCancel() {
        indexLetterTextView.setVisibility(View.GONE);
    }

    /**
     * 是否显示快速导航条
     *
     * @param show
     */
    public void showQuickIndexBar(boolean show) {
        this.showQuickIndexBar = show;
        if (quickIndexBar != null) {
            quickIndexBar.setVisibility(show ? View.VISIBLE : View.GONE);
            quickIndexBar.setOnLetterUpdateListener(this);
            quickIndexBar.invalidate();
        }
    }

    @Override
    public void onUserClick(UIUserInfo userInfo) {

    }

    @Override
    public void onHeaderClick(int index) {

    }

    @Override
    public void onFooterClick(int index) {

    }
}
