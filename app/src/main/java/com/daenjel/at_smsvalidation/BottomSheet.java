package com.daenjel.at_smsvalidation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BottomSheet extends BottomSheetDialogFragment {

    public TextView textView,mChange,mOkay,mbool;
    EditText mCode;
    MainActivity mMain;
    RelativeLayout mConfirm,mVerify;
    Button mValid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

        mMain = new MainActivity();
        Bundle bund = getArguments();
        final String myValue = bund.getString("key");

        /* Set your message */
        final String message = "Welcome to Africa's Talking";

        textView = view.findViewById(R.id.display);
        textView.setText(myValue);

        mConfirm = view.findViewById(R.id.confirmLay);
        mVerify = view.findViewById(R.id.verifyLay);
        mChange = view.findViewById(R.id.btnChange);
        mbool = view.findViewById(R.id.bool);
        mCode = view.findViewById(R.id.editCode);
        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Change Number",Toast.LENGTH_LONG).show();
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
            }
        });
        mValid = view.findViewById(R.id.btnVal);
        mValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Verify();
            }
        });
        return view;
    }
    public void Verify(){

        String code = mCode.getText().toString();

        if (code.isEmpty()){
            mCode.setError("Verification Code Required");
            mCode.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(code).matches()){
            mCode.setError("Enter Valid Code");
            mCode.requestFocus();
            return;
        }
        if (code.length() <4){
            mCode.setError("Incomplete Code");
            mCode.requestFocus();
            return;
        }

        if (code.equals("4321")){
            Toast.makeText(getActivity(),"Welcome to Africa's Talking",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(),Content.class));
            mCode.setText(null);
            dismiss();
        }else {
            mCode.setBackgroundResource(R.drawable.my_invalid_border);
            mbool.setText("Invalid Code");
            mbool.setTextColor(Color.RED);
        }
    }

}
