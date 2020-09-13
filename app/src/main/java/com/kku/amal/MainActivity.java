package com.kku.amal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.WorkManager;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    WorkManager workManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workManager = WorkManager.getInstance();

        BottomNavigationView navView = findViewById(R.id.tabs);
        new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorites, R.id.nav_settings, R.id.nav_collections, R.id.nav_account)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.homefrag);
        NavigationUI.setupWithNavController(navView, navController);

    }

}

