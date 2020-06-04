package com.quyet.banhang.app_banhang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.quyet.banhang.app_banhang.ui.activity.LoginActivity;

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
    int count = 0;
    CartAdapter adapter;
    Button mLogin;
    ConstraintLayout container;
    LinearLayout linearLayout;


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
        if (user != null) {
            linearLayout.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            reference.child("cart").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();
                    int tong = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Cart c = snapshot.getValue(Cart.class);
                        c.setId(snapshot.getKey());
                        tong += c.getCount() * c.getSanPham().getPrice();
                        list.add(c);
                    }
                   if(adapter!=null){
                       adapter.setList(list);
                   }
                   else {
                       adapter = new CartAdapter(getContext(), list, user.getUid());
                       re.setAdapter(adapter);
                       re.setLayoutManager(new LinearLayoutManager(getContext()));
                   }
                    mTongTien.setText(FormatMany.getMany(tong));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            linearLayout.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        }
        mbtnDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) return;
                DatabaseReference orderreferen = reference.child("order").child(user.getUid());
                for (int i = 0; i < list.size(); i++) {
                    orderreferen.push().setValue(list.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            count++;
                            if (count >= list.size() - 1) {
                                reference.child("cart").child(user.getUid()).removeValue();
                                Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                count = 0;
                            }
                        }
                    });
                }
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }

    private void findView(View view) {
        mbtnDatMua = view.findViewById(R.id.btnDatMua);
        mTongTien = view.findViewById(R.id.tvTongTien);
        re = view.findViewById(R.id.recyclerView);
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        mLogin=view.findViewById(R.id.btnLogin);
        container=view.findViewById(R.id.container);
        linearLayout=view.findViewById(R.id.linearLogin);
    }
}
