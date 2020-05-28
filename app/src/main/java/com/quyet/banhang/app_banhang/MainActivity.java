package com.quyet.banhang.app_banhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quyet.banhang.app_banhang.ui.fragment.CartFragment;
import com.quyet.banhang.app_banhang.ui.fragment.HomeFragment;
import com.quyet.banhang.app_banhang.ui.fragment.ProfileFragment;
import com.quyet.banhang.app_banhang.ui.fragment.SrearchFragment;

public class MainActivity extends AppCompatActivity {
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
}
