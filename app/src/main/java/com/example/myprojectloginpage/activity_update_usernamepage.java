package com.example.myprojectloginpage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprojectloginpage.Dto.UserDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_update_usernamepage extends AppCompatActivity {
    UserDto currentUser;
    EditText editTextPassword,editTextUserName,editTextUserSurname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_usernamepage);
        Initialize();
    }

    private void Initialize() {
        Intent getIntent = getIntent();
        currentUser = (UserDto) getIntent.getSerializableExtra("currentUser");
        editTextPassword = findViewById(R.id.textEdit_CurrentPassword);
        Button btn_UpdateUserName = findViewById(R.id.btn_UpdateUserName);
        Button btn_Cancel = findViewById(R.id.btn_cancel);
        editTextUserName = findViewById(R.id.textEdit_userName);
        editTextUserName.setText(currentUser ==null?"":currentUser.getUserName());
        editTextUserSurname = findViewById(R.id.textEdit_userSurname);
        editTextUserSurname.setText(currentUser ==null?"":currentUser.getUserSurname());
        btn_UpdateUserName.setOnClickListener(view -> {
            editTextUserName.setError(editTextUserName.getText().toString().isEmpty()?"Bu Alan Boş Bırakılamaz!":null);
            editTextUserSurname.setError(editTextUserSurname.getText().toString().isEmpty()?"Bu Alan Boş Bırakılamaz!":null);

            editTextPassword.setError(editTextPassword.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
            editTextPassword.setError(editTextPassword.getText().toString().equals(currentUser.getUserPassword())?null:"Şifreniz Doğru Değil");
            ConfirmUpdateUserName();
        });
        btn_Cancel.setOnClickListener(view -> onBackPressed());
    }

    public void ConfirmUpdateUserName() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (editTextUserName.getError() == null && editTextUserSurname.getError() == null && mAuth.getCurrentUser() != null && editTextPassword.getError() == null) {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Kullanıcılar");
            currentUser.setUserSurname(editTextUserSurname.getText().toString());
            currentUser.setUserName(editTextUserName.getText().toString());
            dbref.child(currentUser.getuId()).child("userName").setValue(currentUser.getUserName()).addOnCompleteListener(task -> {
                dbref.child(currentUser.getuId()).child("userSurname").setValue(currentUser.getUserSurname()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity_update_usernamepage.this, "İsiminiz Başarıyla Güncellendi", Toast.LENGTH_SHORT).show();
                        Intent backPage = new Intent();
                        backPage.putExtra("UserInfo", currentUser);
                        setResult(200, backPage);
                        finish();
                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(activity_update_usernamepage.this, "Bir Hata Oluştu " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(activity_update_usernamepage.this, "Lütfen geçerli şifre giriniz.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        setResult(201);
        super.onBackPressed();
    }
}
