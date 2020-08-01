package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.flask.colorpicker.slider.LightnessSlider;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xw.repo.BubbleSeekBar;

import org.w3c.dom.Text;

public class SettingsActivity extends Activity {
    int color;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_settings);
        final TextView t1 = findViewById(R.id.txt_color);
        final TextView t2 = findViewById(R.id.txt_size);
        final TextView bg = findViewById(R.id.Bg);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int colorprefs = prefs.getInt("color", 1);
        int txtprefs = prefs.getInt("txt", 1);
        t1.setTextColor(txtprefs);
        t2.setTextColor(txtprefs);
        bg.setTextColor(txtprefs);

        drawer.setBackgroundColor(colorprefs);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //قاعدين نقول لشاشة التنقل هذي تكون الأولى على الواجهة
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //  نبغى نحدد وش الزر اللي ضغطه المستخدم من المنيو
                int id = menuItem.getItemId();
                // الانتنت بنستعمله للتنقل ما بين الاكتفتيز
                Intent intent;
                // قاعدين نستعمل خدمة ال authentication  من فايربيس، عشان نعرف مين اليوزر المسجل حالياً

                // الحين بنبدأ نحدد وش يصير لما ينضغط كل شيء في المنيو، عناصر المنيو هذي بتلقونها في مجلد المنيو، ملف drawer_menu
                switch (id) {
                    // زر الكولكشنز
                    case R.id.collectionsnav:
                        // إذا اليوزر مسجل دخول، أبغاك تنتقل عادي إلى صفحة الكولكشنز
                        if (user != null) {
                            // User is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة الكولكشنز

                            intent = new Intent(SettingsActivity.this, CollectionsActivity.class);
                            startActivity(intent);


                        }
                        // إذا اليوزر مو مسجل دخول، أبغاك تنتقل أولاً إلى صفحة تسجيل الدخول، خله يسجل أول
                        else {
                            // No user is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة التسجيل
                            intent = new Intent(SettingsActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;

                    case R.id.account:
                        // إذا اليوزر مسجل دخول، أبغاك تنتقل عادي إلى صفحة المستخدم
                        if (user != null) {
                            // User is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة المستخدم

                            intent = new Intent(SettingsActivity.this, MyAccount.class);
                            startActivity(intent);

                        }
                        // إذا اليوزر مو مسجل دخول، أبغاك تنتقل أولاً إلى صفحة تسجيل الدخول، خله يسجل أول

                        else {
                            // No user is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة التسجيل

                            intent = new Intent(SettingsActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;

                    // بنفس الطريقة صفحة المفضلة
                    case R.id.favnav:
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(SettingsActivity.this, Favorites.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(SettingsActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.settings: {
                        intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                    }
                    startActivity(intent);
                    break;
                    case R.id.home:
                        intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent);

                        break;
                }

                // هنا عشان نقفل الدرور لما خلاص ينضغط ويسوي مهمته
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        final ColorPickerView bgpicker = findViewById(R.id.color_picker_view);
        final ColorPickerView txtpicker = findViewById(R.id.txt_color_picker);
     //   final Slider txtsizer = findViewById(R.id.txt_size_slider);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.setTextSize(28);
                t1.setTextSize(20);
                t2.setTextSize(20);
                bgpicker.setVisibility(View.VISIBLE);
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.setTextSize(20);
                t1.setTextSize(28);
                t2.setTextSize(20);
                txtpicker.setVisibility(View.VISIBLE);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg.setTextSize(20);
                t1.setTextSize(20);
                t2.setTextSize(28);
          //      txtsizer.setVisibility(View.VISIBLE);
            }
        });

        bgpicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {

                Log.v("1", "onclick" + selectedColor);
                SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt("color", selectedColor);
                editor.apply();
                int colorprefs = prefs.getInt("color", selectedColor);
                drawer.setBackgroundColor(colorprefs);

            }
        });

       txtpicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {

                Log.v("1", "onclick"+selectedColor);
                SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt("txt", selectedColor);
                editor.apply();
                int colorprefs = prefs.getInt("txt", selectedColor);
                t1.setTextColor(colorprefs);
                t2.setTextColor(colorprefs);
                bg.setTextColor(colorprefs);
            }
        });
        /*txtsizer.addOnSliderTouchListener( new Slider.OnSliderTouchListener() {
                    @Override
                    public void onStartTrackingTouch(Slider slider) {

                    }

                    @Override
                    public void onStopTrackingTouch(Slider slider) {

                    }});

         */


    }


}