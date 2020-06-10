package com.quyet.banhang.app_banhang.adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.function.FormatMany;
import com.quyet.banhang.app_banhang.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DangGiaoAdapter extends RecyclerView.Adapter<DangGiaoAdapter.viewholder> {
    Context context;
    List<Cart> list;
    String uid;
    DatabaseReference shopThanhCong, userThanhCong, commingshop, comminguser;

    public DangGiaoAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.list = list;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        shopThanhCong = FirebaseDatabase.getInstance().getReference("shopthanhcong");
        userThanhCong = FirebaseDatabase.getInstance().getReference("userthanhcong").child(uid);
        comminguser = FirebaseDatabase.getInstance().getReference("comminguser").child(uid);
        commingshop = FirebaseDatabase.getInstance().getReference("commingshop");

    }

    public void setList(List<Cart> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danggiao, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        final Cart c = list.get(position);
        holder.mCount.setText("Số lượng: " + c.getCount());
        holder.mColor.setText("Loại: " + c.getSanPham().getSize() + " - " + c.getSanPham().getColor());
        holder.mName.setText(c.getName());
        holder.mPrice.setText(FormatMany.getMany(c.getSanPham().getPrice()));
        Picasso.with(context).load(c.getImage()).into(holder.mImage);
        holder.mDate.setText(c.getDate());
        holder.mDaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartId = c.getIdCart();

                //update số lượng sp đã bán
                final DatabaseReference daban = FirebaseDatabase.getInstance().getReference("products").child(c.getPid());
                daban.child("daban").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> ban = new HashMap<>();
                        int k = dataSnapshot.getValue(Integer.class);
                        k++;
                        ban.put("daban", k);
                        daban.updateChildren(ban);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update doanh thu
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                final DatabaseReference thongke = FirebaseDatabase.getInstance().getReference("thongke").child(month + "");
                thongke.child("doanhthu").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            int doanhthu = dataSnapshot.getValue(Integer.class);
                            doanhthu += c.getSanPham().getPrice() * c.getCount();
                            thongke.child("doanhthu").setValue(doanhthu);
                        } catch (NullPointerException e) {
                            thongke.child("doanhthu").setValue(c.getSanPham().getPrice() * c.getCount());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update top 10 sp bán chạy nhất
                thongke.child("daban").child(c.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            int count = dataSnapshot.getValue(Integer.class);
                            count++;
                            thongke.child("daban").child(c.getPid()).setValue(count);
                        } catch (NullPointerException e) {
                            thongke.child("daban").child(c.getPid()).setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final String key = shopThanhCong.push().getKey();
                comminguser.child(cartId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        c.setDate(new SimpleDateFormat("hh:mm dd/MM/yyy").format(new Date()));
                        c.setIdCart(null);
                        userThanhCong.child(key).setValue(c);
                    }
                });
                commingshop.child(cartId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        c.setDate(new SimpleDateFormat("hh:mm dd/MM/yyy").format(new Date()));
                        c.setIdCart(null);
                        c.setUid(uid);
                        shopThanhCong.child(key).setValue(c);
                    }
                });


            }

        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView mName, mPrice, mColor, mCount, mDate;
        CircleImageView mImage;
        Button mDaNhan;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tvName);
            mPrice = itemView.findViewById(R.id.tvPrice);
            mImage = itemView.findViewById(R.id.imCart);
            mColor = itemView.findViewById(R.id.tvColor);
            mCount = itemView.findViewById(R.id.tvCount);
            mDate = itemView.findViewById(R.id.tvDate);
            mDaNhan = itemView.findViewById(R.id.btnDaNhan);

        }
    }
}
