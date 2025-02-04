package com.lr.biyou.chatry.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.lr.biyou.R;
import com.lr.biyou.chatry.ui.fragment.PublicServiceFragment;
import com.lr.biyou.chatry.ui.interfaces.OnPublicServiceClickListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.PublicServiceProfile;

import static com.lr.biyou.chatry.ui.view.SealTitleBar.Type.NORMAL;

public class PublicServiceActivity extends TitleBaseActivity implements OnPublicServiceClickListener, View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setType(NORMAL);
        getTitleBar().setTitle(R.string.public_service);
        getTitleBar().getBtnRight().setImageDrawable(getResources().getDrawable(R.drawable.seal_ic_main_more));
        getTitleBar().getBtnRight().setOnClickListener(this);
        setContentView(R.layout.activity_public_service_content);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_fragment_container, new PublicServiceFragment(this))
                .commit();
    }

    @Override
    public void OnPublicServiceClicked(PublicServiceProfile publicServiceProfile) {
        RongIM.getInstance().startConversation(this, publicServiceProfile.getConversationType(), publicServiceProfile.getTargetId(), publicServiceProfile.getName());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PublicServiceSearchActivity.class);
        startActivity(intent);
    }
}
