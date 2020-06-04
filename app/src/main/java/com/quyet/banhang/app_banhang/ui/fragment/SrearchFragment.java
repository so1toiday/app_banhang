package com.quyet.banhang.app_banhang.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.ProductAdapter;
import com.quyet.banhang.app_banhang.model.SanPham;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SrearchFragment extends Fragment {
    RecyclerView mRecycleview;
    SearchView mSearch;
    DatabaseReference reference;
    LinearLayout lEmpty;
    FirebaseUser user;

    public SrearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_srearch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        Bundle bundle=getArguments();
        mSearch.onActionViewExpanded();
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                reference.child("products").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<SanPham> list=new ArrayList<>();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            SanPham sp=snapshot.getValue(SanPham.class);
                          for(int i=0;i<sp.getKey().size();i++){
                              if(sp.getKey().get(i).contains(query)){
                                  list.add(sp);
                                  continue;
                              }
                          }

                        }
                        if(list.size()>0){
                            mRecycleview.setVisibility(View.VISIBLE);
                            lEmpty.setVisibility(View.GONE);
                        }else{
                            mRecycleview.setVisibility(View.GONE);
                            lEmpty.setVisibility(View.VISIBLE);
                        }
                        ProductAdapter adapter=new ProductAdapter(getContext(),list);
                        mRecycleview.setAdapter(adapter);
                        mRecycleview.setLayoutManager(new GridLayoutManager(getContext(),2));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        if(bundle!=null){
            String query=bundle.getString("query");
            mSearch.setQuery(query,true);
        }
    }

    private void findView(View view) {
        mRecycleview=view.findViewById(R.id.reSearchProduct);
        mSearch=view.findViewById(R.id.svSearch);
        reference= FirebaseDatabase.getInstance().getReference();
        lEmpty=view.findViewById(R.id.lEmpty);
    }

    @Override
    public void onResume() {
        super.onResume();
        user= FirebaseAuth.getInstance().getCurrentUser();
    }
}
