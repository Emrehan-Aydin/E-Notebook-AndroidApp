package com.example.myprojectloginpage;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprojectloginpage.Dto.NoteDto;
import com.example.myprojectloginpage.Dto.UserDto;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;


public class activity_note_readandwritepage extends AppCompatActivity {
    UserDto current_user;
    TextView note_Title;
    EditText note_Context;
    NoteDto note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_readandwritepage);
        Initalize();
        setWigetValues();
    }

    private void Initalize() {
        Button btn_deleteNote = findViewById(R.id.btn_deletenote);
        btn_deleteNote.setOnClickListener(this::deleteNoteOnClick);
         note_Title = findViewById(R.id.textview_note_title);
         note_Context = findViewById(R.id.MultiLineText_Context);
        Intent getIntent = getIntent();
        current_user = (UserDto) getIntent.getSerializableExtra("User_Info");
        note = (NoteDto) getIntent.getSerializableExtra("Note");
        if(note.getNote_Context()==null)
        {
            note.setNote_Context("");
        }
        Button btn_SaveAndQuit = findViewById(R.id.btn_saveAndQuick);
        btn_SaveAndQuit.setOnClickListener(view -> saveChanges());
    }
    private void setWigetValues() {
        note_Title.setText(note.getNote_Title());
        note_Context.setText(note.getNote_Context());
    }
    public void saveChanges()
    {
        note_Title.setError(note_Title.getText().toString().isEmpty()?"Bir mesaj başlığı giriniz!":null);
        if (note_Title.getError()==null){
            note.setNote_Context(note_Context.getText().toString());
            note.setNote_Title(note_Title.getText().toString());
            FirebaseDatabase.getInstance()
                    .getReference("KullanıcıNotları")
                    .child(current_user.getuId())
                    .child(String.valueOf(note.getNote_id()))
                    .setValue(note).addOnCompleteListener(this::SaveChangesOnComplete);
        }
        else if (note_Title.getError()!=null)
        {
            Toast.makeText(activity_note_readandwritepage.this,"Mesaj Başlığı Geçersiz!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            saveAlert();
        }
    }

    private void saveAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity_note_readandwritepage.this);
        alert.setTitle("Değişiklikler Kaydedilmedi");
        alert.setMessage("Sayfadan Ayrılmak İstiyor Musunuz ?");
        alert.setPositiveButton("Kaydetmeden Çık", (dialogInterface, i) -> finish());
        alert.setNegativeButton("Düzenlemeye devam et ", (dialogInterface, i) -> {

        });
        alert.setNeutralButton("Kaydet ve Çık", (dialogInterface, i) -> saveChanges());
        alert.show();
    }

    @Override
    public void onBackPressed() {
        saveAlert();
    }

    private void DeleteNoteOnCompleted(Task<Void> task) {
        Toast.makeText(activity_note_readandwritepage.this, note.getNote_Title() + " Başlıklı Not Silindi", Toast.LENGTH_LONG).show();
        finish();
    }

    private void DeleteNoteonFailure(Exception e) {
        Toast.makeText(activity_note_readandwritepage.this, note.getNote_Title() + " Başlıklı Not Silinirken Hata Oluştu!", Toast.LENGTH_LONG).show();
    }

    private void deleteNoteOnClick(View view) {
        FirebaseDatabase.getInstance().getReference("KullanıcıNotları")
                .child(current_user.getuId())
                .child(String.valueOf(note.getNote_id())).removeValue()
                .addOnCompleteListener(this::DeleteNoteOnCompleted)
                .addOnFailureListener(this::DeleteNoteonFailure);
    }

    private void SaveChangesOnComplete(Task<Void> task) {
        if (task.isSuccessful()) {
            Toast.makeText(activity_note_readandwritepage.this, "Not Kaydedildi!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            saveAlert();
        }
    }
}