package com.lr.biyou.rongyun.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.rongyun.ui.adapter.PublicServiceAdapter;
import com.lr.biyou.rongyun.ui.adapter.models.ContactModel;
import com.lr.biyou.rongyun.ui.interfaces.OnPublicServiceClickListener;
import com.lr.biyou.rongyun.viewmodel.PublicServiceViewModel;

import java.util.List;

import com.lr.biyou.R;

public class PublicServiceFragment extends Fragment {
    private RecyclerView recyclerView;
    protected PublicServiceViewModel viewModel;
    private PublicServiceAdapter adapter;

    public PublicServiceFragment(OnPublicServiceClickListener listener) {
        adapter = new PublicServiceAdapter(listener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_searvice_content, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(PublicServiceViewModel.class);
        viewModel.getPublicService().observe(this, new Observer<List<ContactModel>>() {
            @Override
            public void onChanged(List<ContactModel> models) {
                adapter.updateData(models);
            }
        });
        onLoad();
        return view;
    }

    protected void onLoad() {
        viewModel.loadPublicServices();
    }
}
