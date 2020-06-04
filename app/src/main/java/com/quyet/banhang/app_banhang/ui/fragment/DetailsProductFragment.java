package com.quyet.banhang.app_banhang.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.SanPham;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsProductFragment extends Fragment {
    TextView mName,mCategory,mDescreption;
    SanPham sp;

    public DetailsProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle b=getArguments();
       sp = (SanPham) b.getSerializable("sanpham");
        return inflater.inflate(R.layout.fragment_details_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mName.setText(sp.getName());
        mDescreption.setText(sp.getDescreption());
        mCategory.setText(sp.getCategory());
    }

    private void initView(View view) {
        mName=view.findViewById(R.id.tvName);
        mDescreption=view.findViewById(R.id.tvDescreption);
        mCategory=view.findViewById(R.id.tvCategory);
    }
}
