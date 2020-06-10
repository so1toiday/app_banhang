package com.quyet.banhang.app_banhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.inter.ItemClickListenner;
import com.quyet.banhang.app_banhang.model.DetailsSanPham;
import com.quyet.banhang.app_banhang.ui.activity.ProductActivity;

import java.util.List;

public class ProductRadioAdapter extends RecyclerView.Adapter<ProductRadioAdapter.viewholder> {
    int mSelected=0;
    Context context;
    List<DetailsSanPham> list;
    boolean isFirst=true;
    public  ProductRadioAdapter(Context context,List<DetailsSanPham> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_radio, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        if(isFirst && position==0){
            holder.mProduct.setChecked(true);
            holder.mProduct.setText("Loại "+(position+1));
            isFirst=false;
        }else{
            holder.mProduct.setChecked(position==mSelected);
            holder.mProduct.setText("Loại "+(position+1));
        }


    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }



    public class viewholder extends RecyclerView.ViewHolder {
        RadioButton mProduct;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            mProduct=itemView.findViewById(R.id.rdoProduct);
            mProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelected=getAdapterPosition();
                    ProductActivity.setLoai(mSelected);
                    Intent i=new Intent("quyet.product.pro");
                    Bundle b=new Bundle();
                    DetailsSanPham sp=list.get(mSelected);
                    b.putSerializable("infosp",sp);
                    i.putExtras(b);
                    context.sendBroadcast(i);
                    notifyDataSetChanged();
                }
            });


        }
    }
}
