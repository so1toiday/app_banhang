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
import android.widget.Toast;

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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DatHangAdapter extends RecyclerView.Adapter<DatHangAdapter.viewholder> {
    Context context;
    List<Cart> list;
    DatabaseReference reference;
    DatabaseReference cancelReference;
    String UID;
    public  DatHangAdapter(Context context,List<Cart> list,String UID){
        this.context=context;
        this.list=list;
        this.UID=UID;
        reference=FirebaseDatabase.getInstance().getReference("order").child(UID);
        cancelReference=FirebaseDatabase.getInstance().getReference("cancel").child(UID);

    }
    public void setList( List<Cart> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_dathang, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        final Cart c=list.get(position);
        holder.mCount.setText("Số lượng: "+c.getCount());
        holder.mColor.setText("Loại: "+c.getSanPham().getSize()+" - "+c.getSanPham().getColor());
        holder.mName.setText(c.getName());
        holder.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(context).create();
                dialog.setTitle("Hủy đơn hàng");
                dialog.setMessage("Bạn có chắc chắn hủy đơn hàng này");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child(c.getIdCart()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                c.setIdCart(null);
                                cancelReference.push().setValue(c);
                                Toast.makeText(context, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        holder.mPrice.setText(FormatMany.getMany(c.getSanPham().getPrice()));
        Picasso.with(context).load(c.getImage()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        Button mCancel;
        TextView mName,mPrice,mColor,mCount;
        CircleImageView mImage;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            mCancel=itemView.findViewById(R.id.btnCancel);
            mName=itemView.findViewById(R.id.tvName);
            mPrice=itemView.findViewById(R.id.tvPrice);
            mImage=itemView.findViewById(R.id.imCart);
            mColor=itemView.findViewById(R.id.tvColor);
            mCount=itemView.findViewById(R.id.tvCount);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
