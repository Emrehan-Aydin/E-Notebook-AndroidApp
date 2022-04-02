package com.example.myprojectloginpage.Dto;

import java.io.Serializable;

public class NoteDto implements Serializable {

        public NoteDto() {

        }
    public NoteDto(long Id,String noteName) {
            this.note_id=Id;
            this.note_Title=noteName;

    }

        public long getNote_id() {
            return note_id;
        }

        public void setNote_id(long note_id) {
            this.note_id = note_id;
        }

        public String getNote_Title() {
            return note_Title;
        }

        public void setNote_Title(String note_Title) {
            this.note_Title = note_Title;
        }

        public String getNote_Context() {
            return note_Context;
        }

        public void setNote_Context(String note_Context) {
            this.note_Context = note_Context;
        }

        private long note_id;
        private String note_Title;
        private  String note_Context;

        public NoteDto(long note_id, String note_title, String note_context) {
            this.note_id = note_id;
            note_Title = note_title;
            note_Context = note_context;
        }
    }

