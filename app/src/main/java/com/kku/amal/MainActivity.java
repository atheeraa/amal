package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {


    LinearLayout area; //تحتوي على العبارة و الازرار الصغار اللي تحت
    RelativeLayout holder;// علامة الرسالة اللي أول ما نفتح الأبلكيشن
    CheckBox fav; // زر الاعجاب
    ImageView share; // زر المشاركة
    String Select; // راح نحفظ فيها العبارة اللي بنختارها بشكل عشوائي من العبارات اللي بنجيبها من فايربيس
    List<String> list; // لستة العبارات اللي بنجيبها من فايربيس
    TextView textView; // بنحط العبارة في هالتكست فيو
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //واجهة الصفحة هذي راح تكون من ملف activity_main
        setContentView(R.layout.activity_main);
        // التكستفيو اللي راح نحط فيه العبارة
        textView = findViewById(R.id.sentence);


        // the drawer الدرور اللي نسحبه من جنب
        // قاعدين نربطه بالدرور اللي في ملف ال xml
        // وبعدها نستعمل فنكشن onNavigationItemSelected عشان نحدد وش يصير لما ينضغط على كل منها
        // بس قبل ذلك راح ننادي فايربيس ونتأكد هل اليوزر مسجل أو لا؟ عشان لو مو مسجل ما له بيانات في المفضلة أو الكولكشن
        // فلازم أولاً يسجل دخول
        final  DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // الحين بنبدأ نحدد وش يصير لما ينضغط كل شيء في المنيو، عناصر المنيو هذي بتلقونها في مجلد المنيو، ملف drawer_menu
                 switch (id) {
                     // زر الكولكشنز
                    case R.id.collectionsnav:
                        // إذا اليوزر مسجل دخول، أبغاك تنتقل عادي إلى صفحة الكولكشنز
                        if (user != null) {
                            // User is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة الكولكشنز

                            intent = new Intent(MainActivity.this, CollectionsActivity.class);
                            startActivity(intent);


                        }
                        // إذا اليوزر مو مسجل دخول، أبغاك تنتقل أولاً إلى صفحة تسجيل الدخول، خله يسجل أول
                        else {
                            // No user is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة التسجيل
                            intent = new Intent(MainActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;

                    case R.id.account:
                        // إذا اليوزر مسجل دخول، أبغاك تنتقل عادي إلى صفحة المستخدم
                        if (user != null) {
                            // User is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة المستخدم

                            intent = new Intent(MainActivity.this, MyAccount.class);
                            startActivity(intent);

                        }
                        // إذا اليوزر مو مسجل دخول، أبغاك تنتقل أولاً إلى صفحة تسجيل الدخول، خله يسجل أول

                        else {
                            // No user is signed in
                            // الانتنت هذي للتنقل مابين المين اكتفتي اللي احنا فيها الان إلى صفحة التسجيل

                            intent = new Intent(MainActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;

                        // بنفس الطريقة صفحة المفضلة
                    case R.id.favnav:
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(MainActivity.this, Favorites.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(MainActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;

                }

                // هنا عشان نقفل الدرور لما خلاص ينضغط ويسوي مهمته
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // أول ما يفتح البرنامج أبغى المساحة اللي فيها العبارة و الازرار تكون مخفية، أنا هنا مسميتها area
        area = findViewById(R.id.area);
        area.setVisibility(View.INVISIBLE);

        // الهولدر هو علامة الرسالة الصغيرة الي نضغطها و تورينا العبارة
        holder = findViewById(R.id.holder);
        // هنا نحدد لما ينضغط وش نسوي؟
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //لما ينضغط أبغى خلاص تختفي علامة الرسالة
                holder.setVisibility(View.INVISIBLE);
                // وتظهر المساحة اللي تحتوي على العبارة و الازرار الصغار اللي تحت
                area.setVisibility(View.VISIBLE);

            }
        });


        /// Ref to access sentences
        // هنا نسوي رفرنس، عشان ننادي الديتابيس الي في فايربيس
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // الديتابيس تحتوي على عنصر رئيسي سميته sentences  فقاعدين ننادي البيانات اللي تقع في هالمسار
        DatabaseReference ref = database.getReference("sentences");

        ref.addValueEventListener(new ValueEventListener() {
            // هذي الفنكشن يتم ندائها لما يصير فيه تغير في البيانات
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // نسوي instantiations  لللست
                list = new ArrayList<>();
                // فور لوب على قد ال children  اللي في الديتابيس اللي يقعون في ال sentences
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // جيب العبارات و خزنها في ال sentence
                    String sentence = ds.getValue(String.class);
                    // أضف العبارات للست
                    list.add(sentence);
                }
                // هنا بنشوف اللست كاملة وراح نستعمل كلاس random  عشان نختار أحد العناصر اللي في
                // اللست بشكل عشوائي
                // وراح نعرضها في التكست فيو
                // عن طريق setText
                Random r = new Random();
                int n = list.size();
                if (!list.isEmpty()) {
                    String Select = list.get((r.nextInt(n)));
                    textView.setText(Select);
                }

            }

            @Override
            // لو صار فيه أي خطأ في القراءة من الديتابيس نقدر نحدد اش يصير هنا
            // حالياً ما سوينا شيء فعلي، ما راح يظهر شيء للمستخدم
            // بس سوينا جملة طباعة عشان نعرف احنا كمبرمجين اش الخطأ اللي صار
            // بس ممكن نظهر للمستخدم عبارة نقول له فيه خطا؟ ليش لا
            // لتس دو ات !
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });

        // هنا بنحدد اش بيصير لما ينضغط زر الإعجاب

        fav = findViewById(R.id.fav);
        fav.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // أولا بنتأكد لو فيه يوزر مسجل أو لا
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                // تذكروا ان الاعجاب كان تشيك بوكس مو زر، بالتالي له أحد حالتين ، تشيكد أو مو تشيكد
                if(fav.isChecked()){
                  // هل اليوزر مسجل؟
                    if (user != null) {
                    // إذا ايه إذن اصنع له قائمة مفضلة، تندرج تحت اليوزر حقه
                    ref.child("users").child(user.getUid()).child("favorites")
                            //بأشرح لكم في فيديو اش يعني السطرين التاليين
                            .child(user.getUid()+textView.getText().subSequence(0,3))
                            .setValue(textView.getText());


                    // التوست هي التنبيهات الصغييرة اللي تطلع لليوزر تحت، هنا بس قلنا له أوكي أضفناها
                    Toast.makeText(MainActivity.this, "added"+textView.getText(),
                            Toast.LENGTH_SHORT).show();
                }
                    // هنا لو اليوزر مو مسجل! نقول له سجل أولاً عزيزي
                    else {

                    Toast.makeText(MainActivity.this, "You're not signed in",
                            Toast.LENGTH_SHORT).show();
                }

            }
                // هنا لو اليوزر مسجل، بس ضغط ضغطة ثانية على زر الاعجاب، بمعنى شال اللايك
            else{
                    if (user != null) {
                        // راح نقول له احذف العبارة هذي من المفضلة
                        // مرة أخرى، بأشرح لكم ليش سويت هالمسار
                    ref.child("users").child(user.getUid()).child("favorites")
                            .child(user.getUid()+textView.getText()
                                    .subSequence(0,3)).removeValue();
            }}
            }
        } );

        //اش يصير لما ينضغط على زر المشاركة
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // تتذكرون الانتنتس؟ هذا استعمال ثاني لها، اعتبروها طريقة تنقل في الأبلكيشن أو من الابلكيشن لابلكيشنز ثانين
                Intent sendIntent = new Intent();
                // نوع الانتنت ارسال
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Select);
                // نوع البيانات اللي بتنرسل نص
                sendIntent.setType("text/plain");

                // بنسوي لها شير
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });


        // الكولكشينز للآن ما سويتها
        // let's do it together


        /*
        collection = findViewById(R.id.fav);
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


            }
        });*/


    }

}