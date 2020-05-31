package com.quyet.banhang.app_banhang.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.ThongBao;
import com.squareup.picasso.Picasso;

public class ThongBaoActivity extends AppCompatActivity {
    ImageView mImage;
    TextView mTitle,mContent,mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);
        initView();
        Intent i=getIntent();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi tiết thông báo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(i.hasExtra("thongbao")){
            ThongBao t= (ThongBao) i.getSerializableExtra("thongbao");
            Picasso.with(ThongBaoActivity.this).load(t.getImage()).into(mImage);
            mTitle.setText(t.getTitle());
            mDate.setText(t.getDate());
            mContent.setText(t.getContent());
        }
    }

    private void initView() {
        mImage=findViewById(R.id.imThongBao);
        mTitle=findViewById(R.id.tvTitle);
        mContent=findViewById(R.id.tvContent);
        mDate=findViewById(R.id.tvDate);
    }
}
