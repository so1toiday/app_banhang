package com.quyet.banhang.app_banhang.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.CategoryAdapter;
import com.quyet.banhang.app_banhang.adapter.ProductAdapter;
import com.quyet.banhang.app_banhang.model.DetailsSanPham;
import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView re,reProduct;
    DatabaseReference reference;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        getChildFragmentManager().beginTransaction().replace(R.id.banner, new BannerFragment()).commit();
        reference.child("category").limitToFirst(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TheLoai> list=new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(TheLoai.class));
                }
                list.add(new TheLoai("Tất Cả","https://thoitrangngaynay.com/upload/images/khuyen-mai-doi-bep-cu-lay-bep-moi.png"));
                CategoryAdapter adapter=new CategoryAdapter(getContext(),list);
                re.setAdapter(adapter);
                re.setLayoutManager(new GridLayoutManager(getContext(),4));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SanPham> list=new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    SanPham sanPham=snapshot.getValue(SanPham.class);
                    sanPham.setId(snapshot.getKey());
                    list.add(sanPham);
                }
                ProductAdapter adapter=new ProductAdapter(getContext(),list);
                reProduct.setAdapter(adapter);
                reProduct.setLayoutManager(new GridLayoutManager(getContext(),2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("products").keepSynced(true);
//        List<String> image=new ArrayList<>();
//        List<DetailsSanPham> sanPhams=new ArrayList<>();
//        sanPhams.add(new DetailsSanPham("Xanh","29",299000,1000));
//        sanPhams.add(new DetailsSanPham("Đỏ","30",291000,1000));
//        sanPhams.add(new DetailsSanPham("Tím","29",270000,1000));
//
//        image.add("https://gamek.mediacdn.vn/zoom/700_438/2016/3-1473916716742-crop-1473916810927.jpg");
//        image.add("https://i.pinimg.com/originals/89/06/1a/89061ab197489510aef0867939274388.jpg");
//        image.add("https://i.pinimg.com/564x/c7/20/fd/c720fd4a318846a79fe30930eaf04188.jpg");
//        SanPham sanPham=new SanPham();
//        sanPham.setName("Áo Da Calic");
//        sanPham.setDescreption("Áo da calic rất pro");
//        sanPham.setCategory("Quần Áo");
//        sanPham.setStar(5);
//        sanPham.setImage(image);
//        sanPham.setSanPhams(sanPhams);
//        reference.child("products").push().setValue(sanPham);

    }

    private void findView(View view) {
        re=view.findViewById(R.id.re);
        reference= FirebaseDatabase.getInstance().getReference();
        reProduct=view.findViewById(R.id.reProduct);
    }
}

