package com.antelope.goodbrother.business.webData;

import android.view.View;
import android.widget.EditText;

import com.antelope.goodbrother.R;
import com.nirvana.zmkj.base.BaseViewHolder;

import androidx.appcompat.app.AppCompatActivity;

public class WebDataViewHolder extends BaseViewHolder<WebDataPresenter> {
    public WebDataViewHolder(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected WebDataPresenter createPresenter() {
        return new WebDataPresenter(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initContent(View contentView) {
        EditText telEt=contentView.findViewById(R.id.tel_et);
        EditText passEt=contentView.findViewById(R.id.password_et);
        contentView.findViewById(R.id.login_btn).setOnClickListener(v -> {
            presenter.postData(telEt.getText().toString(),passEt.getText().toString());
        });
    }
}
