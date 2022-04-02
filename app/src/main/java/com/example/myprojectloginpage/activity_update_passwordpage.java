package com.example.myprojectloginpage;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myprojectloginpage.Dto.UserDto;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_update_passwordpage extends AppCompatActivity {
    private EditText editTextPassword,editTextPasswordConfirm;
    private UserDto currentUser;
    private DatabaseReference dbref;
    public void Initalize()
    {
        Intent getIntent = getIntent();
        currentUser = (UserDto) getIntent.getSerializableExtra("currentUser");
        editTextPassword = findViewById(R.id.textEdit_password);
        editTextPasswordConfirm = findViewById(R.id.textEdit_confirmPassword);
        Button btn_updatePassword = findViewById(R.id.btn_updatePassword);
        Button btn_Cancel = findViewById(R.id.btn_cancel);

        btn_updatePassword.setOnClickListener(view -> {
            editTextPassword.setError(editTextPassword.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
            editTextPassword.setError(editTextPassword.getText().toString().length()<5?getString(R.string.errormessage_isminlenghtpassword):null);

            editTextPasswordConfirm.setError(editTextPasswordConfirm.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
            editTextPassword.setError(editTextPassword.getText().toString().length()<5?getString(R.string.errormessage_isminlenghtpassword):null);

            editTextPasswordConfirm.setError(editTextPasswordConfirm.getText().toString().equals(currentUser.getUserPassword())?null:"Şifreniz Doğru Değil");
            ConfirmUpdatePasswordBtn_Onclick();
        });
        btn_Cancel.setOnClickListener(view -> onBackPressed());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_passwordpage);
        Initalize();
    }
    public void ConfirmUpdatePasswordBtn_Onclick()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(editTextPassword.getError() == null && editTextPasswordConfirm.getError()==null && mAuth.getCurrentUser()!=null)
        {
            mAuth.getCurrentUser().updatePassword(editTextPassword.getText().toString()).addOnCompleteListener(task -> {
                dbref = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
                currentUser.setUserPassword(editTextPassword.getText().toString());
                dbref.child(currentUser.getuId()).child("userPassword").setValue(currentUser.getUserPassword());
                Toast.makeText(activity_update_passwordpage.this,"Şifre Başarıyla Güncellendi",Toast.LENGTH_SHORT).show();
                Intent backPage = new Intent();
                backPage.putExtra("UserInfo",currentUser);
                setResult(200,backPage);
                finish();
            }).addOnFailureListener(e -> Toast.makeText(activity_update_passwordpage.this,"Bir Hata Oluştu " + e.getMessage(),Toast.LENGTH_SHORT).show());

        }
        else
        {
            Toast.makeText(activity_update_passwordpage.this,"Lütfen geçerli şifre giriniz.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(201);
        super.onBackPressed();

    }

    public static class Activity_noteEditor extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_passwordpage);
        }
    }
}