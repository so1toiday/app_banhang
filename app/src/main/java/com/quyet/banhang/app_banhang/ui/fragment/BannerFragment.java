package com.quyet.banhang.app_banhang.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.BannerAdapter;


import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class BannerFragment extends Fragment {
    DatabaseReference reference;
    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    int index = 0;
    Handler handler;
    Runnable runnable;
    ArrayList<String> banner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_banner, container, false);
        anhxa();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getdata();
    }


    private void anhxa() {
        viewPager = view.findViewById(R.id.viewpager);
        circleIndicator = view.findViewById(R.id.indicatordefault);
        reference = FirebaseDatabase.getInstance().getReference("banner");
    }


    private void getdata() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                banner=new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    banner.add(child.getValue(String.class));
                }


                BannerAdapter adapter = new BannerAdapter(getContext(), banner);
                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);
                handler = new Handler();
                runnable = new Runnable() {
                    public void run() {
                        if( index >= banner.size()){
                            index = 0;
                        }else{
                            index = index+1;
                        }
                        viewPager.setCurrentItem(index, true);
                        handler.postDelayed(runnable, 5000);
                    }
                };
                handler.postDelayed(runnable, 5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
