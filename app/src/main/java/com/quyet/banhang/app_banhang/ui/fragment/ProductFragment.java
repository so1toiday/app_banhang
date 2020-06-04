package com.quyet.banhang.app_banhang.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.ProductRadioAdapter;
import com.quyet.banhang.app_banhang.model.DetailsSanPham;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends Fragment {
    RecyclerView re;
   List< DetailsSanPham> sanPham;
   IntentFilter filter;
   BroadcastReceiver receiver;
   TextView mSize,mColor,mCount;

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle b=getArguments();
        sanPham= (List<DetailsSanPham>) b.getSerializable("sanphams");

        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        ProductRadioAdapter adapter=new ProductRadioAdapter(getContext(),sanPham);
        re.setAdapter(adapter);
        re.setHasFixedSize(true);
        re.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        re.setLayoutManager(linearLayoutManager);
        createReceive();
        mSize.setText(sanPham.get(0).getSize()+"");
        mColor.setText(sanPham.get(0).getColor()+"");
        mCount.setText(sanPham.get(0).getCount()+"");
    }

    private void createReceive() {
        filter=new IntentFilter("quyet.product.pro");
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b=intent.getExtras();
                DetailsSanPham sp= (DetailsSanPham) b.getSerializable("infosp");
                mSize.setText(sp.getSize()+"");
                mColor.setText(sp.getColor()+"");
                mCount.setText(sp.getCount()+"");
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver,filter);

    }

    private void findView(View view) {
        re=view.findViewById(R.id.re);
        mColor=view.findViewById(R.id.tvColor);
        mSize=view.findViewById(R.id.tvSize);
        mCount=view.findViewById(R.id.tvCount);
    }
}
