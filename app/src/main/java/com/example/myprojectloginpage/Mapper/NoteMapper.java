package com.example.myprojectloginpage.Mapper;

import com.example.myprojectloginpage.Dto.NoteDto;
import com.example.myprojectloginpage.myprojectentity.Note;

public class NoteMapper {
    private Note note;
    private NoteDto noteDto;

    public NoteMapper(Note note) {
        this.note = note;
    }
    public NoteMapper(NoteDto noteDto) {this.noteDto = noteDto;}
    public NoteDto MapToNoteDto()
    {
        NoteDto noteDto = new NoteDto();
        noteDto.setNote_id(note.getNote_id());
        noteDto.setNote_Title(note.getNote_Title());
        noteDto.setNote_Context(note.getNote_Context());
        return noteDto;
    }
    public Note MapToNote()
    {
        Note note = new Note();
        note.setNote_id(noteDto.getNote_id());
        note.setNote_Title(noteDto.getNote_Title());
        note.setNote_Context(noteDto.getNote_Context());
        return note;
    }
}
