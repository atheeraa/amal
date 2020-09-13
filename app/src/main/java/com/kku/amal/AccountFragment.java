package com.kku.amal;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.View.getDefaultSize;
import static android.view.View.inflate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    Button logout;
    TextView email;

    public Integer REQUEST_EXIT = 9;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    Button signUpButton, signInButton;
    TextView signupchoice, signinchoice, tosignin2;
    TextView forgot, tosignup, tosignin, verifytext, resend;
    EditText nameup, emailet, emailup, passup, namein, emailin, passin, pass2, emailresetpass;
    LinearLayout choice, signupscreen, signinscreen, details;
    RelativeLayout verify, forgotscreen;
    DatabaseReference ref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account,container,false);

        ref = FirebaseDatabase.getInstance().getReference();
        //Screens init
        choice = view.findViewById(R.id.choice);
        signinscreen = view.findViewById(R.id.signinscreen);
        signinscreen.setVisibility(INVISIBLE);
        details = view.findViewById(R.id.details);
        details.setVisibility(INVISIBLE);
        verify = view.findViewById(R.id.verify);
        verifytext = view.findViewById(R.id.verifyText);
        forgot = view.findViewById(R.id.forgot);
        forgotscreen = view.findViewById(R.id.forgotscreen);
        resend = view.findViewById(R.id.resend);
        emailresetpass = view.findViewById(R.id.emailresetpass);


        signupscreen = view.findViewById(R.id.signupscreen);
        signupscreen.setVisibility(INVISIBLE);

        signinchoice = view.findViewById(R.id.signinchoice);

        logout = view.findViewById(R.id.logout);
        emailet = view.findViewById(R.id.email);
        tosignup = view.findViewById(R.id.tosignup);
        tosignin = view.findViewById(R.id.tosignin);
        tosignin2 = view.findViewById(R.id.tosignin2);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();


        // Check auth on Activity start
        if (user != null && user.isEmailVerified()) {
            //  onAuthSuccess(mAuth.getCurrentUser());
            details.setVisibility(VISIBLE);
        }

        if (user == null || !user.isEmailVerified()) {

            choice.setVisibility(VISIBLE);
            signinchoice.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    emailup.setText("");
                                                    nameup.setText("");
                                                    passup.setText("");
                                                    pass2.setText("");
                                                    signinscreen.setVisibility(VISIBLE);
                                                    choice.setVisibility(INVISIBLE);
                                                }
                                            }
            );
            signupchoice = view.findViewById(R.id.signupchoice);
            signupchoice.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    emailin.setText("");
                                                    passin.setText("");
                                                    signupscreen.setVisibility(VISIBLE);
                                                    choice.setVisibility(INVISIBLE);
                                                }
                                            }
            );

            signUpButton = view.findViewById(R.id.signupbutton);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    signUp();
                                                }
                                            }
            );
            signInButton = view.findViewById(R.id.signinbutton);
            signInButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    signIn();
                                                }
                                            }
            );

            tosignup.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                emailin.setText("");
                                                passin.setText("");
                                                signinscreen.setVisibility(INVISIBLE);
                                                signupscreen.setVisibility(VISIBLE);
                                            }
                                        }
            );

            tosignin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                emailup.setText("");
                                                nameup.setText("");
                                                passup.setText("");
                                                pass2.setText("");
                                                signinscreen.setVisibility(VISIBLE);
                                                signupscreen.setVisibility(INVISIBLE);
                                            }
                                        }
            );
            tosignin2.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 emailup.setText("");
                                                 nameup.setText("");
                                                 passup.setText("");
                                                 pass2.setText("");
                                                 verify.setVisibility(INVISIBLE);
                                                 signinscreen.setVisibility(VISIBLE);
                                                 signupscreen.setVisibility(INVISIBLE);
                                             }
                                         }
            );
            emailin = view.findViewById(R.id.emailin);
            emailup = view.findViewById(R.id.emailup);


            nameup = view.findViewById(R.id.nameup);

            passup = view.findViewById(R.id.passwordup);
            passin = view.findViewById(R.id.passwordin);
            pass2 = view.findViewById(R.id.password2);

        } else {

            choice.setVisibility(INVISIBLE);
            signinscreen.setVisibility(INVISIBLE);
            signupscreen.setVisibility(INVISIBLE);
            //details.setVisibility(VISIBLE);
            // Name, email address, and profile photo Url
            String email = user.getEmail();

            emailet.setText(email);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();


        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "تم تسجيل الخروج",
                            Toast.LENGTH_SHORT).show();
                    details.setVisibility(View.GONE);
                    choice.setVisibility(VISIBLE);

                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinscreen.setVisibility(INVISIBLE);
                forgotscreen.setVisibility(VISIBLE);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailreset =emailresetpass.getText().toString().trim();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailreset)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    forgotscreen.setVisibility(INVISIBLE);
                                    verify.setVisibility(VISIBLE);
                                    verifytext.setText("تم إرسال إيميل لتغيير كلمة المرور");
                                } else {
                                    // ...
                                }
                            }
                        });
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);
       }

    /* @Override
     public void onStart() {
         super.onStart();

     }
*/
    private void signIn() {
        if (!validateFormSignIn()) {
            return;
        }

        String email = emailin.getText().toString().trim();
        String password = passin.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "حدث خطأ ما!",
                                    Toast.LENGTH_SHORT).show();

                        } else if (user.isEmailVerified()) {
                          //  onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(getContext(), "أهلاً بك!",
                                    Toast.LENGTH_SHORT).show();
                            details.setVisibility(VISIBLE);
                            emailet.setText(email);
                            signinscreen.setVisibility(INVISIBLE);

                        } else if (!user.isEmailVerified()) {
                            signinscreen.setVisibility(INVISIBLE);
                            verify.setVisibility(VISIBLE);
                            verifytext.setText("إيميلك غير مفعل، الرجاء تفعيله أولاً، " + "\n" + "إذا لم يصلك الإيميل الرجاء الضغط هنا لإعادة المحاولة");
                            verifytext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sendEmailVerification();

                                }

                            });


                            //


                        }
                    }
                });
    }

    private void signUp() {
        if (!validateFormSignUp()) {
            return;
        }
        String name = nameup.getText().toString();
        String email = emailup.getText().toString().trim();
        String password = passup.getText().toString();
        String password2 = pass2.getText().toString();
        if (password.length() < 6)
            Toast.makeText(getContext(), "كلمة المرور يجب أن تكون أكثر من 6 أحرف",
                    Toast.LENGTH_SHORT).show();

        else if (!password.equals(password2)) {
            Toast.makeText(getContext(), "كلمتي المرور غير متطابقة",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                onAuthSuccess(task.getResult().getUser());
                                sendEmailVerification();

                                Toast.makeText(getContext(), "أهلاً بك!",
                                        Toast.LENGTH_SHORT).show();
                                signupscreen.setVisibility(INVISIBLE);
                                emailet.setText(email);
                                verify.setVisibility(VISIBLE);
                                verifytext.setText("الرجاء تفعيل حسابك عبر الضغط على الرابط الذي سيصلك بد قليل، ثم سجل الدخول");

                            } else {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(getContext(), "كلمة المرور ضعيفة",
                                            Toast.LENGTH_SHORT).show();

                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(getContext(), "بيانات غير صحيحة",
                                            Toast.LENGTH_SHORT).show();

                                } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(getContext(), "الإيميل مستعمل مسبقًا",
                                            Toast.LENGTH_SHORT).show();

                                } catch(Exception e) {
                                    Toast.makeText(getContext(), "حدث خطأ ما!",
                                            Toast.LENGTH_SHORT).show();
     }

                            }
                        }
                    });
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        // Go to MainActivity

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateFormSignUp() {
        boolean result = true;
        if (TextUtils.isEmpty(emailup.getText().toString())) {
            emailup.setError("Required");
            result = false;
        } else {
            emailup.setError(null);
        }

        if (TextUtils.isEmpty(passup.getText().toString())) {
            passup.setError("Required");
            result = false;
        } else {
            passup.setError(null);
        }

        return result;
    }

    private boolean validateFormSignIn() {
        boolean result = true;
        if (TextUtils.isEmpty(emailin.getText().toString())) {
            emailin.setError("Required");
            result = false;
        } else {
            emailin.setError(null);
        }

        if (TextUtils.isEmpty(passin.getText().toString())) {
            passin.setError("Required");
            result = false;
        } else {
            passin.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email,  null);

        ref.child("users").child(userId).setValue(user);
    }

    public void sendEmailVerification() {
        // [START send_email_verification]

        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "تم إرسال إيميل التحقق",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END send_email_verification]
    }
}