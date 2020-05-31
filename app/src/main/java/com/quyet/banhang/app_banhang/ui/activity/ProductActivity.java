package com.quyet.banhang.app_banhang.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.ImageProductAdapter;
import com.quyet.banhang.app_banhang.adapter.ProductViewPagerAdapter;
import com.quyet.banhang.app_banhang.model.Cart;
import com.quyet.banhang.app_banhang.model.DetailsSanPham;
import com.quyet.banhang.app_banhang.model.SanPham;

import me.relex.circleindicator.CircleIndicator;

public class ProductActivity extends AppCompatActivity {
    SanPham sanPham;
    ViewPager vpProduct, vpImage;
    TabLayout mTabProduct;
    Handler handler;
    Runnable runnable;
    TextView mAddToCart;
    int index = 0;
    CircleIndicator circleIndicator;
    FirebaseAuth auth;
    DatabaseReference reference;
    private static int IndexSP=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        findView();
        Intent i = getIntent();
        if (i != null && i.hasExtra("sanpham")) {
            sanPham = (SanPham) i.getSerializableExtra("sanpham");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        setTitle(sanPham.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageProductAdapter ImageAdapter = new ImageProductAdapter(this, sanPham.getImage());
        vpImage.setAdapter(ImageAdapter);
        circleIndicator.setViewPager(vpImage);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (index >= sanPham.getImage().size()) {
                    index = 0;
                } else {
                    index = index + 1;
                }
                vpImage.setCurrentItem(index, true);
                handler.postDelayed(runnable, 5000);
            }
        };
        ProductViewPagerAdapter adapterProduct = new ProductViewPagerAdapter(getSupportFragmentManager(),sanPham);
        vpProduct.setAdapter(adapterProduct);
        mTabProduct.setupWithViewPager(vpProduct);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public static void setLoai(int index){
        IndexSP=index;
    }
    private void findView() {
        vpProduct = findViewById(R.id.vpProduct);
        vpImage = findViewById(R.id.vpImage);
        mTabProduct = findViewById(R.id.tabProducts);
        circleIndicator = findViewById(R.id.circleIndicator);
        reference = FirebaseDatabase.getInstance().getReference("cart");
        auth = FirebaseAuth.getInstance();
        mAddToCart = findViewById(R.id.btnAddToCart);

    }

    public void clickAddToCart(View view) {
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            mAddToCart.setEnabled(false);
            Cart c=new Cart();
            c.setName(sanPham.getName());
            c.setDescreption(sanPham.getDescreption());
            c.setCategory(sanPham.getCategory());
            c.setImage(sanPham.getImage().get(0));
            c.setCount(1);
            DetailsSanPham sp=sanPham.getSanPhams().get(IndexSP);
            c.setSanPham(sp);
            reference.child(user.getUid()).child(sanPham.getId()).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                              @Override
                                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                                  mAddToCart.setEnabled(true);
                                                                                                                  if (task.isSuccessful()) {
                                                                                                                      Toast.makeText(ProductActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                                                                                  } else {
                                                                                                                      Toast.makeText(ProductActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                                                                                                  }
                                                                                                              }
                                                                                                          }
            );
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
        }

    }
}
