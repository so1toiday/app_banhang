package com.quyet.banhang.app_banhang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    Context context;
    List<Cart> list;
    String UID;
    DatabaseReference referenceCount;
    public  CartAdapter(Context context,List<Cart> list,String UID){
        this.context=context;
        this.list=list;
        this.UID=UID;
        referenceCount= FirebaseDatabase.getInstance().getReference("cart").child(UID);
    }
    public void setList( List<Cart> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        final Cart cart=list.get(position);
        Picasso.with(context).load(cart.getImage()).into(holder.mImage);
        holder.mName.setText(cart.getName());
        int gia=cart.getSanPham().getPrice()*list.get(position).getCount();
        holder.mPrice.setText(FormatMany.getMany(gia));
        holder.mMau.setText("Loại: "+cart.getSanPham().getSize()+" - "+cart.getSanPham().getColor());
        holder.mCount.setText(String.valueOf(cart.getCount()));
        holder.mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceCount.child(cart.getId()).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       int count=dataSnapshot.getValue(Integer.class);
                       if(count>1){
                        referenceCount.child(cart.getId()).child("count").setValue(--count);}
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.mAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceCount.child(cart.getId()).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count=dataSnapshot.getValue(Integer.class);
                        referenceCount.child(cart.getId()).child("count").setValue(++count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                referenceCount.child(cart.getId()).removeValue();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        Button mAddition,mMinus;
        TextView mName,mPrice,mCount,mMau;
        CircleImageView mImage;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            mCount=itemView.findViewById(R.id.tvCount);
            mAddition=itemView.findViewById(R.id.btnAddition);
            mMinus=itemView.findViewById(R.id.btnMinus);
            mName=itemView.findViewById(R.id.tvName);
            mPrice=itemView.findViewById(R.id.tvPrice);
            mImage=itemView.findViewById(R.id.imCart);
            mMau=itemView.findViewById(R.id.tvMau);
        }
    }
}
