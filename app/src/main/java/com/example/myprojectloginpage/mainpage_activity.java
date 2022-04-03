package com.example.myprojectloginpage;

import static com.example.myprojectloginpage.loginpage_activity.isAnyPassword;
import static com.example.myprojectloginpage.loginpage_activity.isAnyUserMail;
import static com.example.myprojectloginpage.loginpage_activity.sharedPreferenc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myprojectloginpage.Adapter.RecycleViewAdapter;
import com.example.myprojectloginpage.Dto.NoteDto;
import com.example.myprojectloginpage.Dto.UserDto;
import com.example.myprojectloginpage.Mapper.UserMapper;
import com.example.myprojectloginpage.myprojectentity.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class mainpage_activity extends AppCompatActivity {
    private String uid;
    private TextView view_username;
    private UserDto current_user;
    private ArrayList<NoteDto> current_user_notes;
    ProgressDialog loadingMessage;
    RecyclerView RecycleView_Notes;
    public void Initialize() {
        RecycleView_Notes = findViewById(R.id.recycleview_notes);
        loadingMessage = new ProgressDialog(mainpage_activity.this);
        loadingMessage.setMessage("Yükleniyor");
        loadingMessage.setCancelable(false);
        loadingMessage.show();

        view_username = findViewById(R.id.viewusername);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");


        ImageView imageView_profilesettingsicon = findViewById(R.id.imageView_SettingsIntenticon);
        imageView_profilesettingsicon.setOnClickListener(view -> {
            Intent profileSettings = new Intent(mainpage_activity.this, profile_settings_activity.class);
            profileSettings.putExtra("UserInfo", current_user);
            startActivity(profileSettings);
        });

        ImageButton btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view -> onBackPressed());

        ImageButton addNewNote = findViewById(R.id.btn_addNewNote);
        addNewNote.setOnClickListener(view -> {
            ProgressDialog dialog = new ProgressDialog(mainpage_activity.this);
            dialog.setCancelable(false);
            dialog.setMessage("Not oluşturuluyor.");
            dialog.show();

            Task<DataSnapshot> data = FirebaseDatabase.getInstance().getReference("KullanıcıNotları").child(current_user.getuId()).get();
            data.addOnCompleteListener(task -> {
                DataSnapshot snapshot = data.getResult();
                long note_Id = 1;
                long getChildId;
                if (snapshot.getChildrenCount() != 0) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        getChildId = Long.parseLong(Objects.requireNonNull(child.getKey()));
                        note_Id = Math.max(note_Id, getChildId);
                    }
                    note_Id += 1;
                }

                NoteDto newNote = new NoteDto();
                newNote.setNote_id(note_Id);
                newNote.setNote_Title("Not " + note_Id);
                FirebaseDatabase.getInstance().getReference("KullanıcıNotları").child(current_user.getuId()).child(String.valueOf(newNote.getNote_id())).setValue(newNote);
                Intent newNoteActivity = new Intent(mainpage_activity.this, activity_note_readandwritepage.class);
                newNoteActivity.putExtra("User_Info", current_user);
                newNoteActivity.putExtra("Note", newNote);
                dialog.dismiss();
                startActivity(newNoteActivity);
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(mainpage_activity.this, "Not oluşturulurken bir sorun meydana geldi", Toast.LENGTH_SHORT).show();
            });

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Initialize();
        if(!uid.isEmpty())
        {
            GetUserInfo();
        }
        else
        {
            Toast.makeText(mainpage_activity.this,"Giriş Yaparken Bir Hata Meydana geldi!",Toast.LENGTH_LONG).show();
            finish();
        }

    }
    private void GetUserInfo() {
        DatabaseReference refdb = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(uid);
        refdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user  = snapshot.getValue(User.class);
                UserMapper mapper = new UserMapper(user);
                current_user = mapper.MapToUserDto();
                current_user.setuId(uid);
                getNotes();
                setWigetValues();
                AddLoginCache();
                loadingMessage.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainpage_activity.this,"Bir Sorun Oluştu!",Toast.LENGTH_LONG).show();
                BackToLoginPage();
            }
        });
    }
    public void setWigetValues()
    {
        view_username.setText(MessageFormat.format("{0} {1}", current_user.getUserName(), current_user.getUserSurname()));
    }

    @Override
    public void onBackPressed()
    {
        LogoutEvent_Message();
    }

    private void BackToLoginPage() {
        ClearLoginCache();
        Intent backIntent = new Intent(mainpage_activity.this, loginpage_activity.class);
        startActivity(backIntent);
        finish();
    }

    public void getNotes()
    {
        DatabaseReference refdb = FirebaseDatabase.getInstance().getReference("KullanıcıNotları").child(current_user.getuId());
        refdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null)
                {
                    current_user_notes = new ArrayList<>();
                    for (DataSnapshot note:snapshot.getChildren()) {
                        current_user_notes.add(note.getValue(NoteDto.class));
                    }
                    InitializeRecycleAdapter();
                }
                else
                {
                    current_user_notes=null;
                    InitializeRecycleAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainpage_activity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void InitializeRecycleAdapter() {
        RecycleViewAdapter adapter = new RecycleViewAdapter(mainpage_activity.this, current_user_notes, note -> {
            Intent noteEditor = new Intent(mainpage_activity.this,activity_note_readandwritepage.class);
            noteEditor.putExtra("Note",note);
            noteEditor.putExtra("User_Info",current_user);
            startActivity(noteEditor);
        });
        RecycleView_Notes.setAdapter(adapter);
        RecycleView_Notes.setLayoutManager(new LinearLayoutManager(mainpage_activity.this));
    }
    private void LogoutEvent_Message()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mainpage_activity.this);
        alert.setTitle("Çıkış Yap");
        alert.setMessage("Çıkış yapmak istiyor musunuz ?");
        alert.setPositiveButton("Evet", (dialogInterface, i) -> BackToLoginPage());
        alert.setNegativeButton("Hayır", (dialogInterface, i) -> Toast.makeText(mainpage_activity.this,"Çıkış İptal Edildi",Toast.LENGTH_SHORT).show());
        alert.show();
    }
    private void ClearLoginCache()
    {
        SharedPreferences pref = getSharedPreferences(sharedPreferenc,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(isAnyUserMail,null);
        editor.putString(isAnyPassword,null);
        editor.apply();
    }
    private void AddLoginCache()
    {
        SharedPreferences pref = getSharedPreferences(sharedPreferenc,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(isAnyUserMail,current_user.getUserEmail());
        editor.putString(isAnyPassword,current_user.getUserPassword());
        editor.apply();
    }
}