package com.quyet.banhang.app_banhang.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.CartAdapter;
import com.quyet.banhang.app_banhang.function.FormatMany;
import com.quyet.banhang.app_banhang.model.Cart;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    RecyclerView re;
    TextView mTongTien, mbtnDatMua;
    DatabaseReference reference;
    FirebaseAuth auth;
    List<Cart> list;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        final FirebaseUser user = auth.getCurrentUser();
        if (user !=null){
            reference.child("cart").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list=new ArrayList<>();
                    int tong=0;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Cart c=snapshot.getValue(Cart.class);
                            c.setId(snapshot.getKey());
                            tong+=c.getCount()*c.getSanPham().getPrice();
                            list.add(c);
                        }
                    CartAdapter adapter = new CartAdapter(getContext(), list,user.getUid());
                    re.setAdapter(adapter);
                    re.setLayoutManager(new LinearLayoutManager(getContext()));
                    mTongTien.setText(String.valueOf(FormatMany.getMany(tong)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        mbtnDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user==null) return;
                reference.child("order").push().setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            reference.child("cart").child(user.getUid()).removeValue();
                            Toast.makeText(getContext() , "Đặt hàng thành cônng", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void findView(View view) {
        mbtnDatMua = view.findViewById(R.id.btnDatMua);
        mTongTien = view.findViewById(R.id.tvTongTien);
        re = view.findViewById(R.id.recyclerView);
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }
}
