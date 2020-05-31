package com.quyet.banhang.app_banhang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.User;
import com.quyet.banhang.app_banhang.ui.activity.LoginActivity;
import com.quyet.banhang.app_banhang.ui.activity.SettingActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    DatabaseReference reference;
    TextView mEmail,mName;
    Button mButtonEdit,mLogin;
    CircleImageView mImageUser;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    User user;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference= FirebaseDatabase.getInstance().getReference("user");
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        findView(view);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });
        if(firebaseUser!=null){
            mEmail.setVisibility(View.VISIBLE);
            mName.setVisibility(View.VISIBLE);
            mButtonEdit.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.GONE);
            String uid=firebaseUser.getUid();
            mEmail.setText(firebaseUser.getEmail());
            reference.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user=dataSnapshot.getValue(User.class);
                    mName.setText(user.getName());
                    Picasso.with(getContext()).load(user.getImage()).into(mImageUser);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            mEmail.setVisibility(View.GONE);
            mName.setVisibility(View.GONE);
            mButtonEdit.setVisibility(View.GONE);
            mLogin.setVisibility(View.INVISIBLE);
        }


    }

    private void findView(View view) {
        mEmail=view.findViewById(R.id.tvEmailProfile);
        mName=view.findViewById(R.id.tvNameProfile);
        mButtonEdit=view.findViewById(R.id.btnEditProfile);
        mImageUser=view.findViewById(R.id.imUser);
        mLogin=view.findViewById(R.id.btnDangNhap);
    }
}

