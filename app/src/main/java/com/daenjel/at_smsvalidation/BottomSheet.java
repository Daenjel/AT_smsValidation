package com.daenjel.at_smsvalidation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BottomSheet extends BottomSheetDialogFragment {

    public TextView textView;
    Button mChange,mOkay;
    MainActivity mMain;
    RelativeLayout mConfirm,mVerify;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

        mMain = new MainActivity();
        Bundle mArgs = getArguments();
        final String myValue = mArgs.getString("key");

        /* Set your message */
        final String message = "Welcome to Africa's Talking";

        textView = view.findViewById(R.id.display);
        textView.setText(myValue);

        mConfirm = view.findViewById(R.id.confirmLay);
        mVerify = view.findViewById(R.id.verifyLay);
        mChange = view.findViewById(R.id.btnChange);
        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"CHANGE NUMBER",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        mOkay = view.findViewById(R.id.btnOkay);
        mOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Sending Verification code...",Toast.LENGTH_LONG).show();
                mMain.sendSMS(myValue,message);
                mConfirm.setVisibility(View.GONE);
                mVerify.setVisibility(View.VISIBLE);
                //dismiss();
            }
        });
        return view;
    }

}
