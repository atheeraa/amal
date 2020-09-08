package com.kku.amal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.work.BackoffPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Home extends Fragment {
    SpinKitView progress;
    RelativeLayout area;
    CheckBox fav; // زر الاعجاب
    ImageView share, collection; // زر المشاركة
    String Select; // راح نحفظ فيها العبارة اللي بنختارها بشكل عشوائي من العبارات اللي بنجيبها من فايربيس
    List<String> list; // لستة العبارات اللي بنجيبها من فايربيس
    TextView sentencehome, empty; // بنحط العبارة في هالتكست فيو

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {

        super.onActivityCreated(savedInstanceType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // نربط هالملف بالواجهة ال xml
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences notification = getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);


        // عشان نشوف وش يبغى اليوزر لغة عربي أو انقلش؟
        SharedPreferences prefs = getActivity().getSharedPreferences("lang", Context.MODE_PRIVATE);
        int arpref = prefs.getInt("ar", 1);
        int enpref = prefs.getInt("en", 1);

        int npref = notification.getInt("on/off", 1);
        int freqpref = notification.getInt("freq", 1);

        //ننادي الlayouts اللي في الواجهة
        //واجهة تظهر لما مافيه انترنت
        RelativeLayout noconnection = view.findViewById(R.id.noconnection);
        // الواجهة اللي فيها العبارة
        RelativeLayout bg = view.findViewById(R.id.holder);
        progress = view.findViewById(R.id.progress);


        // عشان نؤخر عمل الانميشن شوي إلى أن يظهر اللوقو لليوزر لمدة ثانيتين
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                logo(bg);
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progress.setVisibility(View.VISIBLE);
            }
        }, 2000);

        // نشيك هل فيه انترنت أو لا؟
        new CheckConnection(internet -> {
            // إذا مافيه؟
            if (!internet) {
                // تظهر صفحة انه مافيه انترنت، حاول مرة أخرى
                // بس نبغى تتأخر شوي إلى أن يروح اللوقو عشان ما
                // يتعارضون فوق بعض
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        noconnection.setVisibility(View.VISIBLE);

                    }
                }, 1000);

            }
            //إذا فيه انترنت
            else {
                // بيبدأ البرنامج يشتغل عادي
                // بس نبغى تتأخر شوي إلى أن يروح اللوقو عشان ما
                // يتعارضون فوق بعض
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // صفحة انه مافيه انترنت نبغى تختفي
                        noconnection.setVisibility(View.INVISIBLE);

                        if(npref==1) {
                            Toast.makeText(getContext(), "on",
                                    Toast.LENGTH_SHORT).show();

                            startNotification();
                        }
                        else{
                            Toast.makeText(getContext(), "off",
                                    Toast.LENGTH_SHORT).show();

                        }


                        // العبارة اللي بتظهر للمستخدم
                        sentencehome = view.findViewById(R.id.sentence);
                         // المربع اللي يحمل العبارة والأزرار اللي تحتها
                        area = view.findViewById(R.id.area);
                        area.setVisibility(View.VISIBLE);
                        sentencehome.setMovementMethod(new ScrollingMovementMethod());


                        // إذا اليوزر يبغى عبارات عربي و انقلش
                        if (arpref == 1 && enpref == 1) {
                            /// Ref to access sentences
                            // هنا نسوي رفرنس، عشان ننادي الديتابيس الي في فايربيس
                            // الديتابيس تحتوي على عنصر رئيسي سميته sentences
                            // فقاعدين ننادي البيانات اللي تقع في هالمسار
                            // بنستدعي الجمل كلها من الديتابيس
                            DatabaseReference ref = database.getReference("sentences");

                            ref.addValueEventListener(new ValueEventListener() {
                                // هذي الفنكشن يتم ندائها لما يصير فيه تغير في البيانات
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // نسوي instantiations  لللست
                                    list = new ArrayList<>();
                                    // فور لوب على قد ال children  اللي في الديتابيس اللي يقعون في ال sentences
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        for (DataSnapshot dss : ds.getChildren()) {
                                            // جيب العبارات و خزنها في ال sentence
                                            String sentence = dss.getValue(String.class);

                                            // أضف العبارات للست
                                            list.add(sentence);
                                            if (!list.isEmpty()) {
                                                progress.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                    // هنا بنشوف اللست كاملة وراح نستعمل كلاس random  عشان نختار أحد العناصر اللي في
                                    // اللست بشكل عشوائي
                                    // وراح نعرضها في التكست فيو
                                    // عن طريق setText
                                    Random r = new Random();
                                    int n = list.size();
                                    if (!list.isEmpty()) {
                                        String Select = list.get((r.nextInt(n)));
                                        sentencehome.setText(Select);
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

                            // لو اليوزر يبغى انقلش بس ، بنسوي نفس الشي أعلاه

                        } else if (arpref == 0 && enpref == 1) {
                            // الفرق فقط في وش بننادي
                            // بدل الديتابيس كاملة، راح نخصص اننا نبغى الفرع اللي فيه الانقلش
                            DatabaseReference ref = database.getReference("sentences/En");
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
                                        if (!list.isEmpty()) {
                                            progress.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                    // هنا بنشوف اللست كاملة وراح نستعمل كلاس random  عشان نختار أحد العناصر اللي في
                                    // اللست بشكل عشوائي
                                    // وراح نعرضها في التكست فيو
                                    // عن طريق setText
                                    Random r = new Random();
                                    int n = list.size();
                                    if (!list.isEmpty()) {
                                        String Select = list.get((r.nextInt(n)));
                                        sentencehome.setText(Select);
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


                        }
                        // بنفس الطريقة لو يبغى عربي بس
                        else if (arpref == 1 && enpref == 0) {
                            // لاحظوا ال path
                            DatabaseReference ref = database.getReference("sentences/Ar");
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
                                        if (!list.isEmpty()) {
                                            progress.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    // هنا بنشوف اللست كاملة وراح نستعمل كلاس random  عشان نختار أحد العناصر اللي في
                                    // اللست بشكل عشوائي
                                    // وراح نعرضها في التكست فيو
                                    // عن طريق setText
                                    Random r = new Random();
                                    int n = list.size();
                                    if (!list.isEmpty()) {
                                        String Select = list.get((r.nextInt(n)));
                                        sentencehome.setText(Select);
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


                        }

                        // هنا بنحدد اش بيصير لما ينضغط زر الإعجاب

                        fav = view.findViewById(R.id.fav);
                        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                                // أولا بنتأكد لو فيه يوزر مسجل أو لا
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference();
                                // تذكروا ان الاعجاب كان تشيك بوكس مو زر، بالتالي له أحد حالتين ، تشيكد أو مو تشيكد
                                if (fav.isChecked()) {
                                    // هل اليوزر مسجل؟
                                    if (user != null) {

                                        if (!sentencehome.getText().equals("")) {
                                            // إذا ايه إذن اصنع له قائمة مفضلة، تندرج تحت اليوزر حقه
                                            ref.child("users").child(user.getUid()).child("favorites")
                                                    //بأشرح لكم في فيديو اش يعني السطرين التاليين
                                                    .child(user.getUid() + sentencehome.getText().subSequence(0, 3))
                                                    .setValue(sentencehome.getText().toString());


                                            // التوست هي التنبيهات الصغييرة اللي تطلع لليوزر تحت، هنا بس قلنا له أوكي أضفناها
                                            Toast.makeText(getContext(), "تمت إضافتها لمفضلتك",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    // هنا لو اليوزر مو مسجل! نقول له سجل أولاً عزيزي
                                    else {
                                        fav.setChecked(false);
                                        Toast.makeText(getContext(), "لإضافة العبارة للمفضلة يجب أن تسجل دخولك",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                                // هنا لو اليوزر مسجل، بس ضغط ضغطة ثانية على زر الاعجاب، بمعنى شال اللايك
                                else {
                                    if (user != null) {
                                        // راح نقول له احذف العبارة هذي من المفضلة
                                        // مرة أخرى، بأشرح لكم ليش سويت هالمسار
                                        ref.child("users").child(user.getUid()).child("favorites")
                                                .child(user.getUid() + sentencehome.getText()
                                                        .subSequence(0, 3)).removeValue();
                                    }
                                }
                            }
                        });


                        //اش يصير لما ينضغط على زر المشاركة
                        share = view.findViewById(R.id.share);
                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // تتذكرون الانتنتس؟ هذا استعمال ثاني لها، اعتبروها طريقة تنقل في الأبلكيشن أو من الابلكيشن لابلكيشنز ثانين
                                Intent sendIntent = new Intent();
                                // نوع الانتنت ارسال
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, sentencehome.getText().toString());
                                // نوع البيانات اللي بتنرسل نص
                                sendIntent.setType("text/plain");

                                // بنسوي لها شير
                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                startActivity(shareIntent);

                            }
                        });

                    }
                }, 2000);


            }
        });
        return view;

    }


    // فنكشن الانميشن

    void logo(View bg) {
        Animation bgfade = AnimationUtils.loadAnimation(getContext(), R.anim.bgfade);
        bg.startAnimation(bgfade);
        bg.setVisibility(View.INVISIBLE);
    }



    private void startNotification() {
        SharedPreferences notification = getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
        int freqpref = notification.getInt("freq", 1);
        int repeat =24;
        int flex = 15;


        switch (freqpref){
            case 2 :
                repeat=12;
                break;
            case 3:
                repeat=6;
                break;
            case 4 :
                repeat=3;
                break;
            }
        Toast.makeText(getContext(), "freq"+freqpref+"repeat"+repeat,
                Toast.LENGTH_LONG).show();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                NotificationWorker.class,
                repeat, //long repeatInterval,
                TimeUnit.HOURS, // TimeUnit repeatIntervalTimeUnit,
                flex,//   long flexInterval,
                TimeUnit.MINUTES)
                .setInitialDelay(5,TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.HOURS)
                .build();
        WorkManager workManager = WorkManager.getInstance();
        workManager.enqueue(periodicWorkRequest);


    }
}

