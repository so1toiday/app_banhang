package com.quyet.banhang.app_banhang.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.MainActivity;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.CategoryAdapter;
import com.quyet.banhang.app_banhang.adapter.ProductAdapter;
import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView re,reProduct;
    DatabaseReference reference;
    CategoryAdapter adapter;
    SearchView sv;

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
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                      @Override
                                      public boolean onQueryTextSubmit(String query) {
                                          Fragment fragment=new SrearchFragment();
                                          Bundle b=new Bundle();
                                          b.putString("query",query);
                                          fragment.setArguments(b);
                                          ((MainActivity)getActivity()).replaceFragments(fragment);
                                          return false;
                                      }

                                      @Override
                                      public boolean onQueryTextChange(String newText) {
                                          return false;
                                      }
                                  }
        );
        reference.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TheLoai> list=new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    list.add(snapshot.getValue(TheLoai.class));
                }
                if(adapter!=null){
                    adapter.setOnDatachange(list);
                }else{
                    adapter=new CategoryAdapter(getContext(),list);
                    re.setAdapter(adapter);
                    LinearLayoutManager gi = new LinearLayoutManager(getContext());
                    gi.setOrientation(RecyclerView.HORIZONTAL);
                    re.setLayoutManager(gi);
                }

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
    }

    private void findView(View view) {
        re=view.findViewById(R.id.re);
        reference= FirebaseDatabase.getInstance().getReference();
        reProduct=view.findViewById(R.id.reProduct);
        sv=view.findViewById(R.id.svSearch);
    }
}

