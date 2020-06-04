package com.quyet.banhang.app_banhang.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.ProductAdapter;
import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    DatabaseReference reference;
    RecyclerView re;
    TextView mTitleCategory;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        Intent i=getIntent();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(i.hasExtra("category")){
            TheLoai tl= (TheLoai) i.getSerializableExtra("category");
            reference.orderByChild("category").equalTo(tl.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<SanPham> list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SanPham sp=snapshot.getValue(SanPham.class);
                        sp.setId(snapshot.getKey());
                        list.add(sp);
                    }
                    if (adapter != null) {
                        adapter.updateDatachange(list);
                    } else {
                        adapter=new ProductAdapter(CategoryActivity.this,list);
                        re.setAdapter(adapter);
                        re.setLayoutManager(new GridLayoutManager(CategoryActivity.this,2));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mTitleCategory.setText(tl.getTitle());
        }


    }

    private void initView() {
        reference = FirebaseDatabase.getInstance().getReference("products");
        re = findViewById(R.id.re);
        mTitleCategory=findViewById(R.id.tvTitleCategory);
    }
}
