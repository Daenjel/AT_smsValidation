package com.daenjel.at_smsvalidation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.africastalking.AfricasTalking;
import com.africastalking.models.sms.Recipient;
import com.africastalking.services.SmsService;
import com.africastalking.utils.Logger;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //For the emulator, connecting to local host
    private final String host = "192.168.26.247";
    private final int port = 8088;

    Button mButton;
    public EditText mUserName, mNumber, mPassword;
    BottomSheet bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheet = new BottomSheet();
        mUserName = findViewById(R.id.editUserName);
        mNumber = findViewById(R.id.editNumber);
        mPassword = findViewById(R.id.editPass);
        mButton = findViewById(R.id.btnSignUp);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        connectToServer();
    }

    public void sendSMS(final String number, final String message){
        //Toast.makeText(this, "Sending Verification code...",Toast.LENGTH_LONG).show();
        // get our sms service and use it to send the message
            @SuppressLint("StaticFieldLeak")AsyncTask<Void, String, Void> smsTask = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    //get the sms service
                    SmsService smsService = AfricasTalking.getSmsService();

                    //Send the sms, get the response
                    List<Recipient> recipients = smsService.send(message, new String[] {number});

                    //Toast the response
                    Toast.makeText(MainActivity.this, recipients.get(0).messageId + " " + recipients.get(0).status,Toast.LENGTH_LONG).show();
                } catch (IOException e){
                    Toast.makeText(MainActivity.this, "Failed to Send Code",Toast.LENGTH_LONG).show();
                    Log.e("SMS FAILURE", e.toString());
                }/*finally {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }*/
                return null;
            }
        };

        smsTask.execute();
    }
    //  implementation of connectToServer()
    private void connectToServer(){

        //Initialize te sdk, and connect to our server. Do this in a try catch block
        try{
            AfricasTalking.initialize(host, port,true);

            //Use AT's Logger to get any message
            AfricasTalking.setLogger(new Logger() {
                @Override
                public void log(String message, Object... args) {

                    Log.e("FROM AT LOGGER",message + " " + args.toString());
                }
            });

            Log.e("SERVER SUCCESS","Managed to connect to server");
        } catch (IOException e){

            Log.e("SERVER ERROR", "Failed to connect to server");
        }
    }
    protected void registerUser() {

        String username = mUserName.getText().toString();
        String number = mNumber.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (username.isEmpty()) {
            mUserName.setError("Username is Required");
            mUserName.requestFocus();
            return;
        }

        if (number.isEmpty()) {
            mNumber.setError("Number is Required");
            mNumber.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(number).matches()) {
            mNumber.setError("Please Enter Valid Phone Number");
            mNumber.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mPassword.setError("Password is Required");
            mPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Minimum length should be 6 characters");
            mPassword.requestFocus();
            return;
        }

        Bundle bund = new Bundle();
        bund.putString("key", mNumber.getText().toString());  // Set the numbers you want to send to in international format
        bottomSheet.setArguments(bund);
        bottomSheet.show(getSupportFragmentManager(),"Africa's Talking");

    }
}
