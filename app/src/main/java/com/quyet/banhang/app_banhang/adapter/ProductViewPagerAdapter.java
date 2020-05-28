package com.quyet.banhang.app_banhang.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.quyet.banhang.app_banhang.ui.fragment.DetailsProductFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ProductFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ReviewsProductFragment;

public class ProductViewPagerAdapter extends FragmentStatePagerAdapter {
    public ProductViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ProductFragment();
                break;
            case 1:
                fragment = new DetailsProductFragment();
                break;
            case 2:
                fragment = new ReviewsProductFragment();
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
