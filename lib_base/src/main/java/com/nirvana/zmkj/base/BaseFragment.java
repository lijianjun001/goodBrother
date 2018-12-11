package com.nirvana.zmkj.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nirvana.zmkj.widget.ShowMessageProxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseFragment extends Fragment {
    public ShowMessageProxy showMessageProxy;
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessageProxy = new ShowMessageProxy(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(rootView);
        initView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= LayoutInflater.from(getActivity()).inflate(getLayoutId(),null);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    public abstract int getLayoutId();
    protected abstract void findViews(View rootView);
    protected abstract void initView();

}
