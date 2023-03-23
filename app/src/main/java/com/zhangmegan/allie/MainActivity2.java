package com.zhangmegan.allie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_container);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogActivity()).commit();

        BottomNavigationView bottomNav = findViewById(R.id.navbar);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch(item.getItemId()) {
                    case R.id.nav_log: {
                        selected = new LogActivity();
                        break;
                    }
                    case R.id.nav_analysis: {
                        selected = new AnalysisFragment();
                        break;
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
                return true;
            }
        });
    }
}
