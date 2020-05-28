package com.quyet.banhang.app_banhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.quyet.banhang.app_banhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageProductAdapter extends PagerAdapter {
    Context context;
    List<String> list;


    public ImageProductAdapter(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_image_product, container, false);
        ImageView imageView=view.findViewById(R.id.imProduct);
        Picasso.with(context).load(list.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
