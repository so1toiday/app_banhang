package com.quyet.banhang.app_banhang.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.LoginViewPagerAdapter;

public class LoginActivity extends AppCompatActivity {
    TabLayout tab;
    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findview();
        LoginViewPagerAdapter adapter=new LoginViewPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        tab.setSelectedTabIndicatorColor(Color.parseColor("#FFA725"));
        tab.setupWithViewPager(vp);
    }

    private void findview() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Đăng nhập / Đăng ký");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tab=findViewById(R.id.tlLogin);
        vp=findViewById(R.id.vpLogin);
    }
}
