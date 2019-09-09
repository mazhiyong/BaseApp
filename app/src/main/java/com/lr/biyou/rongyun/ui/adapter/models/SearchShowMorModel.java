package com.lr.biyou.rongyun.ui.adapter.models;

public class SearchShowMorModel extends SearchModel<Integer> {

    public SearchShowMorModel(Integer bean, int type, int priority) {
        super(bean, type);
        this.priority = priority;
    }
}
