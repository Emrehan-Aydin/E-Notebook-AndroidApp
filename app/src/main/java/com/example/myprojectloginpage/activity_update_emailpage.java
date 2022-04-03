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

public class activity_update_emailpage extends AppCompatActivity {

    private EditText textEdit_userEmail,editTextPasswordConfirm;
    private UserDto currentUser;
    private DatabaseReference dbref;
    public void Initalize()
    {
        Intent getIntent = getIntent();
        currentUser = (UserDto) getIntent.getSerializableExtra("currentUser");
        textEdit_userEmail = findViewById(R.id.textEdit_userEmail);
        editTextPasswordConfirm = findViewById(R.id.textEdit_confirmPassword);
        Button btn_UpdateEmail = findViewById(R.id.btn_UpdateUserName);

        Button btn_Cancel = findViewById(R.id.btn_cancel);
        btn_Cancel.setOnClickListener(view -> onBackPressed());
        btn_UpdateEmail.setOnClickListener(view -> {
            textEdit_userEmail.setError(textEdit_userEmail.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
            textEdit_userEmail.setError(isEmailValid(textEdit_userEmail.getText().toString())?null:"Girilen posta adresi geçersiz");
            editTextPasswordConfirm.setError(editTextPasswordConfirm.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
            editTextPasswordConfirm.setError(editTextPasswordConfirm.getText().toString().equals(currentUser.getUserPassword())?null:"Şifreniz Doğru Değil");
            ConfirmUpdatePasswordBtn_Onclick();
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_emailpage);
        Initalize();
    }
    public void ConfirmUpdatePasswordBtn_Onclick()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(textEdit_userEmail.getError() == null && editTextPasswordConfirm.getError()==null && mAuth.getCurrentUser()!=null)
        {
            mAuth.getCurrentUser().updateEmail(textEdit_userEmail.getText().toString()).addOnCompleteListener(task -> {
                dbref = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
                currentUser.setUserEmail(textEdit_userEmail.getText().toString());
                dbref.child(currentUser.getuId()).child("userEmail").setValue(currentUser.getUserEmail());
                Toast.makeText(activity_update_emailpage.this,"Mail Başarıyla Güncellendi",Toast.LENGTH_SHORT).show();
                Intent backPage = new Intent();
                backPage.putExtra("UserInfo",currentUser);
                setResult(200,backPage);
                finish();
            }).addOnFailureListener(e -> Toast.makeText(activity_update_emailpage.this,"Bir Hata Oluştu " + e.getMessage(),Toast.LENGTH_SHORT).show());

        }
        else
        {
            Toast.makeText(activity_update_emailpage.this,"Lütfen geçerli bilgiler giriniz.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(201);
       super.onBackPressed();
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}