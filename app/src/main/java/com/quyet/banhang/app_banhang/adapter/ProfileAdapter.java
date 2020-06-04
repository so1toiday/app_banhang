package com.quyet.banhang.app_banhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.MenuProfile;
import com.quyet.banhang.app_banhang.ui.activity.ChangePasswordActivity;
import com.quyet.banhang.app_banhang.ui.activity.DonHangActivity;
import com.quyet.banhang.app_banhang.ui.activity.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.viewholder> {
    Context context;
    List<MenuProfile> list;
    FirebaseUser firebaseUser;


    public ProfileAdapter(Context context, List<MenuProfile> list) {
        this.context = context;
        this.list = list;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_menu, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        MenuProfile m=list.get(position);
        holder.mTitle.setText(m.getTitle());
        holder.mImage.setImageResource(m.getImage());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mTitle;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.imMenuProfile);
            mTitle=itemView.findViewById(R.id.tvTitleProfile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firebaseUser==null){
                        context.startActivity(new Intent(context, LoginActivity.class));
                        return;
                    }
                    if(getAdapterPosition()==1){
                        context.startActivity(new Intent(context, ChangePasswordActivity.class));
                    }else if(getAdapterPosition()==0){
                        context.startActivity(new Intent(context, DonHangActivity.class));
                    }
                }
            });
        }
    }
}
