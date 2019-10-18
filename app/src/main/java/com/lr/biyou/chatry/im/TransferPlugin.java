package com.lr.biyou.chatry.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lr.biyou.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imlib.model.Conversation;

public class TransferPlugin implements IPluginModule, IPluginRequestPermissionResultCallback {

    Conversation.ConversationType conversationType;
    String targetId;

    public TransferPlugin() {
    }

    @Override
    public Drawable obtainDrawable(Context context) {

        return context.getResources().getDrawable(R.drawable.transfer_selector_account);
    }

    @Override
    public String obtainTitle(Context context) {
        return "转账";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        this.conversationType = rongExtension.getConversationType();
        this.targetId = rongExtension.getTargetId();

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

    @Override
    public boolean onRequestPermissionResult(Fragment fragment, RongExtension rongExtension, int i, @NonNull String[] strings, @NonNull int[] ints) {
        return false;
    }
}
