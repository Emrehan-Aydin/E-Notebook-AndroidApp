package com.example.myprojectloginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class loginpage_activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ProgressDialog progressDialog_load;
    private EditText textedit_mail,textedit_password;
    public Button btn_login;
    private FirebaseUser mUser;
    public void Initialize()
    {
        mAuth = FirebaseAuth.getInstance();
        textedit_mail =  findViewById(R.id.editTextPersonName);
        textedit_password =  findViewById(R.id.editTextPassword);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> {
            progressDialog_load = new ProgressDialog(loginpage_activity.this);
            progressDialog_load.setCancelable(false);
            progressDialog_load.setMessage("Giriş Yapılıyor");
            progressDialog_load.show();

            textedit_mail.setError(textedit_mail.getText().toString().isEmpty() ? getString(R.string.errormessage_isemptyinput) : null);
            textedit_password.setError(textedit_password.getText().toString().isEmpty() ? getString(R.string.errormessage_isemptyinput) : null);
            textedit_password.setError(textedit_password.getText().toString().length()<=5? getString(R.string.errormessage_isminlenghtpassword) : null);
            if(textedit_mail.getError() == null && textedit_password.getError()==null)
            {
                loginManager();
            }
            else
            {
                progressDialog_load.dismiss();
                Toast.makeText(loginpage_activity.this,"Doldurmadığınız Alanlar var!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Initialize();
    }
    private void loginManager() {
        mAuth.signInWithEmailAndPassword(textedit_mail.getText().toString(),textedit_password.getText().toString()).addOnSuccessListener(loginpage_activity.this, authResult -> {
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