package com.quyet.banhang.app_banhang.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.sax.EndElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.MainActivity;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.CategoryAdapter;
import com.quyet.banhang.app_banhang.adapter.ProductAdapter;
import com.quyet.banhang.app_banhang.functions.FragmentChangeListenner;
import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class HomeFragment extends Fragment {
    private RecyclerView re, reProduct;
    private DatabaseReference reference;
    private CategoryAdapter adapter;
    private SearchView sv;
    private NestedScrollView scrollView;
    private int currentPosition = 0;
    private boolean isLoading = true;
    private String TAG="HomeFragment";
    private FragmentChangeListenner listenner;
    private ProgressBar progressBar;

    public void setFragmentChangeListenner(FragmentChangeListenner listenner) {
        this.listenner = listenner;
    }
    private  List<SanPham> list ;
    private ProductAdapter adapterProduct;

    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        list= new ArrayList<>();
        adapterProduct= new ProductAdapter(getContext(), list);
        reProduct.setAdapter(adapterProduct);
        final GridLayoutManager g = new GridLayoutManager(getContext(), 2);
        reProduct.setLayoutManager(g);

        getChildFragmentManager().beginTransaction().replace(R.id.banner, new BannerFragment()).commit();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                      @Override
                                      public boolean onQueryTextSubmit(String query) {
                                          Fragment fragment = new SrearchFragment();
                                          Bundle b = new Bundle();
                                          b.putString("query", query);
                                          fragment.setArguments(b);
                                          listenner.ReplaceFragment(fragment);
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
                List<TheLoai> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue(TheLoai.class));
                }
                if (adapter != null) {
                    adapter.setOnDatachange(list);
                } else {
                    adapter = new CategoryAdapter(getContext(), list);
                    re.setAdapter(adapter);
                    LinearLayoutManager gi = new LinearLayoutManager(getContext());
                    gi.setOrientation(RecyclerView.HORIZONTAL);
                    re.setLayoutManager(gi);

                }
                isLoading=false;
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading=false;
                progressBar.setVisibility(GONE);
            }
        });

        reference.child("products").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("HomeFragment","helo: "+dataSnapshot.getChildrenCount());
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    sanPham.setId(snapshot.getKey());
                    list.add(sanPham);
                }
                adapterProduct.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("products").keepSynced(true);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if(!isLoading){
                        loadMoreData();
                    }
                }
            }
        });
    }

    private void loadMoreData() {
        isLoading=true;
        progressBar.setVisibility(View.VISIBLE);
        String nodeId=list.get(list.size()-1).getId();
        reference.child("products").limitToFirst(10).startAt(nodeId).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"ADD"+dataSnapshot.getChildrenCount());
                int k=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    k++;
                    if(k==1){
                        continue;
                    }
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    sanPham.setId(snapshot.getKey());
                    list.add(sanPham);
                }
                adapterProduct.notifyDataSetChanged();
                isLoading=false;
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isLoading=false;
                progressBar.setVisibility(GONE);
            }
        });
    }

    private void findView(View view) {
        re = view.findViewById(R.id.re);
        reference = FirebaseDatabase.getInstance().getReference();
        reProduct = view.findViewById(R.id.reProduct);
        sv = view.findViewById(R.id.svSearch);
        scrollView = view.findViewById(R.id.scrollView);
        progressBar=view.findViewById(R.id.progressbar);
    }
}

