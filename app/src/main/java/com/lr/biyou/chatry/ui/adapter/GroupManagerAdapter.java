package com.lr.biyou.chatry.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.List;

import com.lr.biyou.chatry.model.GroupMember;
import com.lr.biyou.chatry.ui.adapter.item.GroupManagerItem;


/**
 * 群管理的 Adapter
 */
public class GroupManagerAdapter extends BaseAdapter implements SectionIndexer {

    private int MAX_SIZE = 0;
    private List<GroupMember> datas;
    private List<GroupMember> notSelected;
    private List<GroupMember> selected = new ArrayList<>();
    private OnGroupManagerListener listener;
    private boolean isUseCheck = false;
    public void setMaxSelectSize(int size) {
        MAX_SIZE = size;
        if (!isUseCheck && size > 1) {
            isUseCheck = true;
        }
    }

    /**
     * 是否使用多选，
     *
     * @param isUseCheck true 使用多选，并显示多选按钮 ； false 不使用
     */
    public void setUseCheck(boolean isUseCheck) {
        this.isUseCheck = isUseCheck;
    }

    public void updateList(List<GroupMember> list) {
        datas = list;
        notifyDataSetChanged();
    }

    public void setNotSelected(List<GroupMember> notSelected) {
        this.notSelected = notSelected;
        notifyDataSetChanged();
    }

    public void setOnGroupManagerListener (OnGroupManagerListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return datas == null? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null? null : datas.get(position);
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new GroupManagerItem(parent.getContext());
        }

        final GroupMember data = datas.get(position);
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        final GroupManagerItem itemView = (GroupManagerItem) convertView;
        itemView.setData(data, position == getPositionForSection(section));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUseCheck) {
                    if (selected.contains(data)) { // 已经选择过
                        // 移除掉
                        selected.remove(data);
                        itemView.setChecked(false);
                        if (listener != null) {
                            listener.onSelected(selected.size(), selected);
                        }
                    } else {
                        if (selected.size() < MAX_SIZE) {
                            selected.add(data);
                            itemView.setChecked(true);
                            if (listener != null) {
                                listener.onSelected(selected.size(), selected);
                            }
                        } else {
                            if (listener != null) {
                                listener.onAlreadyReachedMaxSize(selected, notSelected);
                            }
                        }
                    }
                } else {
                    if (listener != null) {
                        selected.clear();
                        selected.add(data);
                        listener.onSelected(selected.size(), selected);
                    }
                }
            }
        });

        // 选择状态
        if (isUseCheck) {
            itemView.setCheckVisibility(View.VISIBLE);
            if (notSelected != null && notSelected.contains(data)) {
                itemView.setEnabled(false);
            } else {
                itemView.setEnabled(true);
                itemView.setChecked(selected.contains(data));
            }
        } else {
            itemView.setCheckVisibility(View.GONE);
        }

        return convertView;
    }


    @Override
    public int getSectionForPosition(int position) {
        return datas.get(position).getNameSpelling().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = datas.get(i).getNameSpelling();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 获取选择的
     * @return
     */
    public List<GroupMember> getSelectedMember() {
        return selected;
    }


    public interface OnGroupManagerListener {
        void onSelected(int number, List<GroupMember> selected);

        void onAlreadyReachedMaxSize(List<GroupMember> selected, List<GroupMember> notSelected);
    }

}
