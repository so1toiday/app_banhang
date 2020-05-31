package com.quyet.banhang.app_banhang.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    }

    private void findView(View view) {
        re=view.findViewById(R.id.re);
    }
}
