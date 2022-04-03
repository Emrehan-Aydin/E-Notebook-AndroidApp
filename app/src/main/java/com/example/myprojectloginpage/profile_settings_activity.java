package com.example.myprojectloginpage;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprojectloginpage.Dto.UserDto;


public class profile_settings_activity extends AppCompatActivity {
    private TextView textViewUserEmail,textViewUserPassword,textViewUserFullName;
    private UserDto currentUser;
    private ActivityResultLauncher<Intent> activityResultLaunch;
    private ImageButton btn_EditEmail,btn_EditPassword,btn_EditUserName;

    public void Initialize()
    {
        Intent getIntent = getIntent();
        currentUser = (UserDto) getIntent.getSerializableExtra("UserInfo");
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserPassword = findViewById(R.id.textViewUserPassword);
        textViewUserFullName = findViewById(R.id.textViewUserFullName);
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(view -> onBackPressed());
        ActivityResultLauncher();

        btn_EditEmail = findViewById(R.id.imgbtn_UpdateEmail);
        btn_EditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEmailEvent_OnClick();
            }
        });

        btn_EditPassword = findViewById(R.id.imgbtn_UpdatePassword);
        btn_EditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPasswordEvent_OnClick();
            }
        });

        btn_EditUserName = findViewById(R.id.imgbtn_UpdateUserName);
        btn_EditUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNameEvent_OnClick();
            }
        });

    }

    private void ActivityResultLauncher() {
        activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == 200) {
                        assert result.getData() != null;
                        currentUser = (UserDto) result.getData().getSerializableExtra("UserInfo");
                        InitializeWigetValues();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Initialize();
        InitializeWigetValues();
    }

    private void InitializeWigetValues() {
        textViewUserFullName.setText(String.format("%s %s", currentUser.getUserName(), currentUser.getUserSurname()));
        textViewUserEmail.setText(currentUser.getUserEmail());
        textViewUserPassword.setText(currentUser.getUserPassword());

    }
    public void editEmailEvent_OnClick()
    {
        Intent emailPage = new Intent(profile_settings_activity.this,activity_update_emailpage.class);
        emailPage.putExtra("currentUser",currentUser);
        activityResultLaunch.launch(emailPage);

    }
    public void editPasswordEvent_OnClick()
    {
        Intent passwordPage = new Intent(profile_settings_activity.this,activity_update_passwordpage.class);
        passwordPage.putExtra("currentUser",currentUser);
        activityResultLaunch.launch(passwordPage);
    }
    public void editNameEvent_OnClick()
    {
        Intent userNamePage = new Intent(profile_settings_activity.this,activity_update_usernamepage.class);
        userNamePage.putExtra("currentUser",currentUser);
        activityResultLaunch.launch(userNamePage);
    }
}