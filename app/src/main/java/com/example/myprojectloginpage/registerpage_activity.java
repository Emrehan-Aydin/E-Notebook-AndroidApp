package com.example.myprojectloginpage;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myprojectloginpage.myprojectentity.UserRegisterModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class registerpage_activity extends AppCompatActivity {
    EditText editText_username, editText_Surname, editText_userEmail,editText_userPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    UserRegisterModel model;
    ProgressDialog load_dialog;
    public void Initialize()
    {
        mAuth = FirebaseAuth.getInstance();
        editText_username = findViewById(R.id.editTextPersonName);
        editText_Surname = findViewById(R.id.editTextPersonSurname);
        editText_userEmail = findViewById(R.id.editTextPersonEmail);
        editText_userPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btn_register);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Initialize();
    }
    public void registerOnClick(View V)
    {
        load_dialog = new ProgressDialog(registerpage_activity.this);
        load_dialog.setMessage("Bilgiler Kontrol Ediliyor.");
        load_dialog.setCancelable(false);
        load_dialog.show();

        editText_username.setError(editText_username.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
        editText_Surname.setError(editText_Surname.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
        editText_userEmail.setError(editText_userEmail.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
        editText_userPassword.setError(editText_userPassword.getText().toString().isEmpty()?getString(R.string.errormessage_isemptyinput):null);
        editText_userPassword.setError(editText_userPassword.getText().toString().length()<=5?getString(R.string.errormessage_isminlenghtpassword):null);

        if(editText_username.getError() == null && editText_Surname.getError() == null && editText_userEmail.getError() == null && editText_userPassword.getError() == null){

            setWidgetToObject();
            RegisterManager();
        }
        else{
            load_dialog.dismiss();
            Toast.makeText(registerpage_activity.this,"Eksik bilgi girdiniz.",Toast.LENGTH_SHORT).show();
        }
    }

    private void setWidgetToObject()
    {
        model = new UserRegisterModel(editText_username.getText().toString(),
                editText_Surname.getText().toString(),
                editText_userPassword.getText().toString(),
                editText_userEmail.getText().toString()
        );

    }
    private void RegisterManager() {
        db = FirebaseDatabase.getInstance();
        mAuth.createUserWithEmailAndPassword(model.getUserEmail(), model.getUserPassword())
                .addOnCompleteListener(registerpage_activity.this, task -> {
                    if (task.isSuccessful()) {
                        Save_User_RealTime(mAuth.getUid());
                        load_dialog.dismiss();
                        Toast.makeText(registerpage_activity.this, "Kaydınız Başarıyla Oluşturuldu.", Toast.LENGTH_LONG).show();


                        finish();
                    } else {
                        load_dialog.dismiss();
                        Toast.makeText(registerpage_activity.this, "Kayıt sırasında bir sorun oluştu. Daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                    }
                });
    }
        public void Save_User_RealTime(String uID)
        {
            db.getReference().child("Kullanıcılar").child(uID).setValue(model).addOnCompleteListener(task -> load_dialog.setMessage("Kullanıcı Kaydediliyor.")).addOnFailureListener(e -> {
                Toast.makeText(registerpage_activity.this, "Kayıt sırasında bir sorun oluştu. Daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                onBackPressed();
            });
        }
    }