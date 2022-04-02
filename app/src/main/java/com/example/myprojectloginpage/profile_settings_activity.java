package com.example.myprojectloginpage;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprojectloginpage.Dto.UserDto;


public class profile_settings_activity extends AppCompatActivity {
    private TextView textViewUserEmail,textViewUserPassword,textViewUserFullName;
    private UserDto currentUser;
    private ActivityResultLauncher<Intent> activityResultLaunch;
    

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
    public void editEmailEvent_OnClick(View view)
    {
        Intent emailPage = new Intent(profile_settings_activity.this,activity_update_emailpage.class);
        emailPage.putExtra("currentUser",currentUser);
        activityResultLaunch.launch(emailPage);

    }
    public void editPasswordEvent_OnClick(View view)
    {
        Intent passwordPage = new Intent(profile_settings_activity.this,activity_update_passwordpage.class);
        passwordPage.putExtra("currentUser",currentUser);
        activityResultLaunch.launch(passwordPage);
    }
}