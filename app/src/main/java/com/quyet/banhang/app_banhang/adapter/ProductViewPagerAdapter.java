package com.quyet.banhang.app_banhang.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.ui.fragment.DetailsProductFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ProductFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ReviewsProductFragment;

import java.io.Serializable;

public class ProductViewPagerAdapter extends FragmentStatePagerAdapter {
    SanPham sanPham;
    public ProductViewPagerAdapter(@NonNull FragmentManager fm, SanPham sanPham) {
        super(fm);
        this.sanPham=sanPham;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ProductFragment();
                Bundle b=new Bundle();
                b.putSerializable("sanphams", (Serializable) sanPham.getSanPhams());
                fragment.setArguments(b);
                break;
            case 1:
                fragment = new DetailsProductFragment();
                Bundle b1=new Bundle();
                b1.putSerializable("sanpham", sanPham);
                fragment.setArguments(b1);
                break;
            case 2:
                fragment = new ReviewsProductFragment();
                Bundle b2=new Bundle();
                b2.putString("id",sanPham.getId());
                fragment.setArguments(b2);
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Sản phẩm";
                break;
            case 1:
                title = "Chi Tiết";
                break;
            case 2:
                title = "Đánh giá";
                break;
        }
        return title;
    }
}
