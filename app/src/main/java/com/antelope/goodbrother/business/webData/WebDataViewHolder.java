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
        EditText telEt = contentView.findViewById(R.id.tel_et);
        EditText passEt = contentView.findViewById(R.id.password_et);
        contentView.findViewById(R.id.login_btn).setOnClickListener(v -> {
            presenter.login(telEt.getText().toString(), passEt.getText().toString());
        });
        contentView.findViewById(R.id.main_btn).setOnClickListener(v -> {
            presenter.login("13260213625", "123456");
        });
        contentView.findViewById(R.id.person_btn).setOnClickListener(v -> {
            presenter.getProducts();
        });
    }
}
