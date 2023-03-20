package com.example.mylearning.notepad;

import static android.text.TextUtils.substring;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylearning.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Note> notes;

    Adapter(Context context, List<Note>notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull

    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_noteitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String title = notes.get(position).getTitle();
        String date = notes.get(position).getDate();
        String time = notes.get(position).getTime();
        time = time.substring(0,5);
        holder.noteTitle.setText(title);
        holder.noteDate.setText(date);
        holder.noteTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle,  noteDate, noteTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.textViewTitle);
            noteDate = itemView.findViewById(R.id.textViewDate);
            noteTime = itemView.findViewById(R.id.textViewTime);

            itemView.setOnClickListener((View v)-> {
                    Intent intent = new Intent(v.getContext(), NoteContentActivity.class);
                    intent.putExtra("ID",notes.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(intent);

            });
        }
    }
}
