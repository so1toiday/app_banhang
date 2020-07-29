package com.quyet.banhang.app_banhang.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.quyet.banhang.app_banhang.function.FormatMany;
import com.quyet.banhang.app_banhang.model.Cart;
import com.quyet.banhang.app_banhang.model.DetailsSanPham;
import com.quyet.banhang.app_banhang.model.SanPham;

public class ProductActivity extends AppCompatActivity {
    ConstraintLayout containerBottom;
    SanPham sanPham;
    ViewPager vpProduct, vpImage;
    TabLayout mTabProduct;
    Handler handler;
    Runnable runnable;
    TextView mAddToCart, mGia;
    int index = 0;
    FirebaseAuth auth;
    DatabaseReference reference;
    private static int IndexSP = 0;
    BroadcastReceiver receiver;
    IntentFilter filter;

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
        mGia.setText(FormatMany.getMany(sanPham.getSanPhams().get(0).getPrice()));
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
        ProductViewPagerAdapter adapterProduct = new ProductViewPagerAdapter(getSupportFragmentManager(), sanPham);
        vpProduct.setAdapter(adapterProduct);
        vpProduct.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    containerBottom.setVisibility(View.GONE);
                } else {
                    containerBottom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabProduct.setupWithViewPager(vpProduct);
        filter = new IntentFilter("quyet.product.pro");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                DetailsSanPham s = (DetailsSanPham) b.getSerializable("infosp");
                mGia.setText(FormatMany.getMany(s.getPrice()));
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        unregisterReceiver(receiver);
    }

    public static void setLoai(int index) {
        IndexSP = index;
    }

    private void findView() {
        vpProduct = findViewById(R.id.vpProduct);
        vpImage = findViewById(R.id.vpImage);
        mTabProduct = findViewById(R.id.tabProducts);
        reference = FirebaseDatabase.getInstance().getReference("cart");
        auth = FirebaseAuth.getInstance();
        mAddToCart = findViewById(R.id.btnAddToCart);
        mGia = findViewById(R.id.tvGia);
        containerBottom = findViewById(R.id.containerBottom);
    }

    public void clickAddToCart(View view) {
        try {
            final FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                mAddToCart.setEnabled(false);
                Cart c = new Cart();
                c.setName(sanPham.getName());
                c.setDescreption(sanPham.getDescreption());
                c.setCategory(sanPham.getCategory());
                c.setImage(sanPham.getImage().get(0));
                c.setCount(1);
                DetailsSanPham sp = sanPham.getSanPhams().get(IndexSP);
                c.setSanPham(sp);
                c.setPid(sanPham.getId());
                reference.child(user.getUid()).push().setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        } catch (Exception e) {

        }

    }
}
