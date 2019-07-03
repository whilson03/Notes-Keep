package com.olabode.wilson.notekeep.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.olabode.wilson.notekeep.R;
import com.olabode.wilson.notekeep.models.Note;

/**
 * Created by OLABODE WILSON on 2019-06-26.
 */
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {


    private static DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getBody().equals(newItem.getBody()) &&
                    oldItem.getTimeStamp().equals(newItem.getTimeStamp());
        }
    };


    private OnItemClickListener listener;
    private ToggleListener Tlistener;
    private OnItemLongClickListener LongListener;


    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);

        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getBody());
        holder.dateTextView.setText(currentNote.getTimeStamp());

        if (currentNote.getIsFavourite() == 1) {
            holder.favouriteButton.setChecked(true);
        } else {
            holder.favouriteButton.setChecked(false);
        }
    }


    public Note getNoteAt(int position) {

        return getItem(position);
    }

    public void addNoteBack(int pos, Note note) {

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setTlistener(ToggleListener tlistener) {
        this.Tlistener = tlistener;
    }

    public void setLongListener(OnItemLongClickListener listener) {
        this.LongListener = listener;
    }

    /**
     * interfaces
     */
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public interface ToggleListener {
        void onItemToggle(Note note, boolean isChecked);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(Note note);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView dateTextView;
        private ToggleButton favouriteButton;


        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            dateTextView = itemView.findViewById(R.id.note_date);
            favouriteButton = itemView.findViewById(R.id.favButton);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });


            favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int position = getAdapterPosition();

                        Log.i("toggle", " " + position);

                        if (Tlistener != null && position != RecyclerView.NO_POSITION) {
                            Tlistener.onItemToggle(getItem(position), true);

                        }

                    } else {
                        int position = getAdapterPosition();

                        Log.i("toggle", " " + position);

                        if (Tlistener != null && position != RecyclerView.NO_POSITION) {
                            Tlistener.onItemToggle(getItem(position), false);
                        }
                    }
                }
            });


            // get the position that was long clicked
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (LongListener != null && position != RecyclerView.NO_POSITION) {
                        LongListener.OnItemLongClick(getItem(position));
                    }
                    return false;
                }
            });


        }

    }

}
