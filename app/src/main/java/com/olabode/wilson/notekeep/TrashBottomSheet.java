package com.olabode.wilson.notekeep;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrashBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener listener;


    public TrashBottomSheet() {

        // Required empty public constructor
    }

    public void setOnBottomSheetItemClickedListener(BottomSheetListener onBottomSheetItemClickedListener) {
        this.listener = onBottomSheetItemClickedListener;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_trash_bottom_sheet, container, false);

        TextView restoreTextView = rootView.findViewById(R.id.restore_note);
        TextView deleteNoteTextView = rootView.findViewById(R.id.delete_note);


        restoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onBottomSheetItemClicked(view.getId());
                }
                dismiss();
            }
        });


        deleteNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onBottomSheetItemClicked(view.getId());
                }
                dismiss();
            }
        });

        return rootView;
    }


    public interface BottomSheetListener {
        void onBottomSheetItemClicked(int id);
    }

}
