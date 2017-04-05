package com.capstone.naexpire.naexpirebusiness;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivityRegFirstLogin extends AppCompatActivity {

    EditText phone, addressOne, addressTwo, city, zip, description;
    TextView start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_first_login);

        phone = (EditText) findViewById(R.id.txtRegPhone);
        addressOne = (EditText) findViewById(R.id.txtRegAddressOne);
        addressTwo = (EditText) findViewById(R.id.txtRegAddressTwo);
        city = (EditText) findViewById(R.id.txtRegCity);
        zip = (EditText) findViewById(R.id.txtRegZip);
        description = (EditText) findViewById(R.id.txtRegDescription);
        start = (TextView) findViewById(R.id.lblStartTime);
        end = (TextView) findViewById(R.id.lblEndTime);

        Spinner spinner = (Spinner) findViewById(R.id.spnState);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                this, R.array.states_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

    }

    public void clickRegisterNext(View view){

        //checks to make sure all text fields are filled
        Boolean ready = !phone.getText().toString().isEmpty() && !addressOne.getText().toString().isEmpty()
                && !addressTwo.getText().toString().isEmpty() && !city.getText().toString().isEmpty()
                && !zip.getText().toString().isEmpty() && !description.getText().toString().isEmpty();

        if(ready){
            Intent intent = new Intent(this, ActivityRegFoodTypes.class);
            startActivity(intent);
        }
        else Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
    }

    public void clickStartTime(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_set_time, null);
        final TimePicker time = (TimePicker) mView.findViewById(R.id.tmpTime);
        Button timeOkay = (Button) mView.findViewById(R.id.btnTimeOk);
        Button timeCancel = (Button) mView.findViewById(R.id.btnTimeCancel);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        timeCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        timeOkay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int hour = time.getCurrentHour();
                int mins = time.getCurrentMinute();
                String hours = "12";
                String minutes = "";
                String ampm = "";

                if(hour < 12){
                    ampm = " AM";
                    if(hour != 0) hours = ""+hour;
                }
                else{
                    if(hour != 12) hours = ""+(hour - 12);
                    ampm = " PM";
                }
                if(mins < 10) minutes = "0"+mins;
                else minutes = ""+mins;
                start.setText(hours +":"+minutes+ampm);
                dialog.dismiss();
            }
        });
    }

    public void clickEndTime(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_set_time, null);
        final TimePicker time = (TimePicker) mView.findViewById(R.id.tmpTime);
        Button timeOkay = (Button) mView.findViewById(R.id.btnTimeOk);
        Button timeCancel = (Button) mView.findViewById(R.id.btnTimeCancel);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        timeCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        timeOkay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int hour = time.getCurrentHour();
                int mins = time.getCurrentMinute();
                String hours = "12";
                String minutes = "";
                String ampm = "";

                if(hour < 12){
                    ampm = " AM";
                    if(hour != 0) hours = ""+hour;
                }
                else{
                    if(hour != 12) hours = ""+(hour - 12);
                    ampm = " PM";
                }
                if(mins < 10) minutes = "0"+mins;
                else minutes = ""+mins;
                end.setText(hour +":"+minutes+ampm);
                dialog.dismiss();
            }
        });
    }
}
