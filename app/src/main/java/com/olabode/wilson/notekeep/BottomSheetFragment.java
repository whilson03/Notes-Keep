package com.olabode.wilson.notekeep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    public static final String TAG = BottomSheetFragment.class.getSimpleName();


    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet, container, false);

        TextView deleteButton = rootView.findViewById(R.id.bottom_sheet_delete);
        TextView copyButton = rootView.findViewById(R.id.bottom_sheet_copy);
        TextView shareButton = rootView.findViewById(R.id.bottom_sheet_share);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonClicked(view.getId());
                dismiss();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onButtonClicked(view.getId());
                    dismiss();
                }
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onButtonClicked(view.getId());
                    dismiss();
                }
            }
        });

        return rootView;
    }


    public void setmListener(BottomSheetListener listener) {
        this.mListener = listener;
    }


    public interface BottomSheetListener {
        void onButtonClicked(int id);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            mListener = (BottomSheetListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement BottomSheetListener");
//        }
//    }


}
