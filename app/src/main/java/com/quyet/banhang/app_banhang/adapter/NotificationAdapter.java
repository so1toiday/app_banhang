package com.quyet.banhang.app_banhang.adapter;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.function.FormatMany;
import com.quyet.banhang.app_banhang.model.Cart;
import com.quyet.banhang.app_banhang.model.ThongBao;
import com.quyet.banhang.app_banhang.ui.activity.ThongBaoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewholder> {
    Context context;
    List<ThongBao> list;
    public  NotificationAdapter(Context context,List<ThongBao> list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_thongbao, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        final ThongBao tb=list.get(position);
        Picasso.with(context).load(tb.getImage()).into(holder.mImageThongBao);
        holder.mTextTitle.setText(tb.getTitle());
        holder.mTextDate.setText(tb.getDate());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView mImageThongBao;
        TextView mTextTitle,mTextDate;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            mImageThongBao=itemView.findViewById(R.id.imThongBao);
            mTextDate=itemView.findViewById(R.id.tvDateNotification);
            mTextTitle=itemView.findViewById(R.id.tvTitleNotification);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, ThongBaoActivity.class);
                    i.putExtra("thongbao",list.get(getAdapterPosition()));
                    context.startActivity(i);
                }
            });
        }
    }
}
