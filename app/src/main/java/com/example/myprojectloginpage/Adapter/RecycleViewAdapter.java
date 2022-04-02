package com.example.myprojectloginpage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectloginpage.Dto.NoteDto;
import com.example.myprojectloginpage.R;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    public RecycleViewAdapter(Context context, ArrayList<NoteDto> notes, ItemOnClickListener listener) {
        this.context = context;
        this.notes = notes;
        this.listener = listener;
    }
    private ItemOnClickListener listener;
    private Context context;
    private ArrayList<NoteDto> notes;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclecardview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.note_Title.setText(notes.get(holder.getAdapterPosition()).getNote_Title());
            holder.note_Description.setText(notes.get(holder.getAdapterPosition()).getNote_Context());
            holder.itemView.setOnClickListener(view -> listener.GetNoteDetail(notes.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return notes==null?0:notes.size();
    }
    public interface ItemOnClickListener{
        void GetNoteDetail(NoteDto note);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView note_Title,note_Description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note_Title = itemView.findViewById(R.id.Note_Title);
            note_Description = itemView.findViewById(R.id.Note_Description);
        }
    }
}
