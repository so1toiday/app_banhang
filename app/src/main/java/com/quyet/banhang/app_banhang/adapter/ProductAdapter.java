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

import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.ui.activity.ProductActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.viewholder> {
    List<SanPham> list;
    Context context;

    public ProductAdapter(Context context,List<SanPham> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.viewholder holder, int position) {
        SanPham s=list.get(position);
        holder.mProductPrice.setText(s.getSanPhams().get(0).getPrice()+"VNĐ");
        holder.mProductName.setText(s.getName());
        Picasso.with(context).load(s.getImage().get(0)).into(holder.mProductImage);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView mProductName,mProductPrice;
        ImageView mProductImage;

        public viewholder(@NonNull final View itemView) {
            super(itemView);
            mProductImage=itemView.findViewById(R.id.imProduct);
            mProductName=itemView.findViewById(R.id.tvProductName);
            mProductPrice=itemView.findViewById(R.id.tvPriceProduct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    Intent i=new Intent(context, ProductActivity.class);
                    i.putExtra("sanpham",list.get(position));
                    context.startActivity(i);
                }
            });
        }
    }
}
