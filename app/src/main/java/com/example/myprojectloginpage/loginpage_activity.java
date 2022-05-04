package com.example.myprojectloginpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class loginpage_activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressDialog progressDialog_load;
    private EditText textedit_mail,textedit_password;
    public Button btn_login;
    private FirebaseUser mUser;
    final static String sharedPreferenc="enotebook.SharedPreferences ";
    final static String isAnyUserMail="enotebook.SharedPreferences.userMail";
    final static String isAnyPassword="enotebook.SharedPreferences.userPassword";
    public void Initialize()
    {
        mAuth = FirebaseAuth.getInstance();
        textedit_mail =  findViewById(R.id.editTextPersonName);
        textedit_password =  findViewById(R.id.editTextPassword);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> {
            ShowProgressDialog();

            textedit_mail.setError(textedit_mail.getText().toString().isEmpty() ? getString(R.string.errormessage_isemptyinput) : null);
            textedit_password.setError(textedit_password.getText().toString().isEmpty() ? getString(R.string.errormessage_isemptyinput) : null);
            textedit_password.setError(textedit_password.getText().toString().length()<=5? getString(R.string.errormessage_isminlenghtpassword) : null);
            if(textedit_mail.getError() == null && textedit_password.getError()==null)
            {
                loginManager(textedit_mail.getText().toString(),textedit_password.getText().toString());
            }
            else
            {
                progressDialog_load.dismiss();
                Toast.makeText(loginpage_activity.this,"Doldurmadığınız Alanlar var!",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void ShowProgressDialog() {
        progressDialog_load = new ProgressDialog(loginpage_activity.this);
        progressDialog_load.setMessage("Giriş Yapılıyor");
        progressDialog_load.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Initialize();
        HaveAnyAccount();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }
                    }
                });
    }

    private void HaveAnyAccount() {
        if(getSharedPreferences(sharedPreferenc,MODE_PRIVATE).getString(isAnyPassword,null)!=null
                && getSharedPreferences(sharedPreferenc,MODE_PRIVATE).getString(isAnyUserMail,null)!=null)
        {
            ShowProgressDialog();
            SharedPreferences anyUser = getSharedPreferences(sharedPreferenc,MODE_PRIVATE);
            loginManager(anyUser.getString(isAnyUserMail,"").trim(),anyUser.getString(isAnyPassword,"").trim());
        }
    }

    private void loginManager(String Email,String Password) {
        mAuth.signInWithEmailAndPassword(Email,Password).addOnSuccessListener(loginpage_activity.this, authResult -> {
            mUser = mAuth.getCurrentUser();
            Toast.makeText(loginpage_activity.this,"Giriş Başarılı",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(loginpage_activity.this, mainpage_activity.class);
            i.putExtra("uid",mUser.getUid());
            progressDialog_load.dismiss();
            startActivity(i);
            finish();
        }).addOnFailureListener(loginpage_activity.this, e -> {
            progressDialog_load.dismiss();
            Toast.makeText(loginpage_activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        });
    }
    public void registerOnClick(View V)
    {
        Intent i = new Intent(loginpage_activity.this, registerpage_activity.class);
        startActivity(i);
    }


}