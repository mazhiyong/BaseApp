package com.lr.biyou.chatry.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResult {
    @SerializedName(value = "id", alternate = {"groupId"})
    public String id;

    public List<AddMemberResult> userStatus;
}
