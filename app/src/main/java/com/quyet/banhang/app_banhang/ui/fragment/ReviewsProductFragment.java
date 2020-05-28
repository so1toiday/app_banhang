package com.quyet.banhang.app_banhang.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quyet.banhang.app_banhang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsProductFragment extends Fragment {

    public ReviewsProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews_product, container, false);
    }
}
