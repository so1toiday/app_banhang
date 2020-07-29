package com.quyet.banhang.app_banhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.quyet.banhang.app_banhang.functions.FragmentChangeListenner;
import com.quyet.banhang.app_banhang.model.DetailsSanPham;
import com.quyet.banhang.app_banhang.model.SanPham;
import com.quyet.banhang.app_banhang.ui.fragment.CartFragment;
import com.quyet.banhang.app_banhang.ui.fragment.HomeFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ProfileFragment;
import com.quyet.banhang.app_banhang.ui.fragment.SrearchFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ThongBaoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentChangeListenner {
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        loadFragment(new HomeFragment());

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment=null;
                switch (menuItem.getItemId()){
                    case R.id.mnHome:
                        fragment=new HomeFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.mnSearch:
                        fragment=new SrearchFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.mnCart:
                        fragment=new CartFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.mnUser:
                        fragment=new ProfileFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.mnThongBao:
                        fragment=new ThongBaoFragment();
                        loadFragment(fragment);
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();
    }


    private void findView() {
        navigation=findViewById(R.id.navigation);
    }


    @Override
    public void ReplaceFragment(Fragment fragment) {
        navigation.setSelectedItemId(R.id.mnSearch);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof HomeFragment){
            ((HomeFragment) fragment).setFragmentChangeListenner(this);
        }
    }
}
