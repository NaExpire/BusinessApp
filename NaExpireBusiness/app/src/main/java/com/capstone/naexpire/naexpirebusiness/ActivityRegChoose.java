package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ActivityRegChoose extends AppCompatActivity {
    private SharedPreferences sharedPref;

    private int startTime, endTime;
    private TextView start, end;
    private EditText description;
    private ColorStateList oldColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_choose);

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        start = (TextView) findViewById(R.id.lblStartTime);
        end = (TextView) findViewById(R.id.lblEndTime);
        description = (EditText) findViewById(R.id.txtDescription);

        start.setText(sharedPref.getString("startTime", "7:00 PM"));
        end.setText(sharedPref.getString("endTime", "8:00 PM"));

        oldColors =  start.getTextColors();

        startTime = 700;
        endTime = 800;
    }

    //user taps to set pickup start time
    public void clickStartTime(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_set_time, null);
        final TimePicker time = (TimePicker) view.findViewById(R.id.tmpTime);
        Button timeOkay = (Button) view.findViewById(R.id.btnTimeOk);
        Button timeCancel = (Button) view.findViewById(R.id.btnTimeCancel);

        mBuilder.setView(view);
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
                int minutes = time.getCurrentMinute();
                String minute = String.format("%02d", minutes);
                startTime = Integer.parseInt(hour + minute);
                String ampm = " AM";

                //convert to AM/PM format from 24 hour
                if (hour == 0) {
                    hour += 12;
                    ampm = " AM";
                } else if (hour == 12) {
                    ampm = " PM";
                } else if (hour > 12) {
                    hour -= 12;
                    ampm = " PM";
                } else {
                    ampm = " AM";
                }

                start.setText(hour +":"+minute+ampm);
                if(startTime > endTime){ //if start time is after end time
                    start.setTextColor(Color.RED); // set text of selected times to red
                    end.setTextColor(Color.RED);
                    Toast.makeText(ActivityRegChoose.this, "start time must be earlier than end time", Toast.LENGTH_SHORT).show();
                }
                else{ //if start time is before end time
                    //set time text back to default color
                    start.setTextColor(oldColors);
                    end.setTextColor(oldColors);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("startTime", hour +":"+minutes+ampm);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
    }

    //user taps to select end time
    public void clickEndTime(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_set_time, null);
        final TimePicker time = (TimePicker) view.findViewById(R.id.tmpTime);
        Button timeOkay = (Button) view.findViewById(R.id.btnTimeOk);
        Button timeCancel = (Button) view.findViewById(R.id.btnTimeCancel);

        mBuilder.setView(view);
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
                int minutes = time.getCurrentMinute();
                String minute = String.format("%02d", minutes);
                endTime = Integer.parseInt(hour + minute);
                String ampm = " AM";

                //convert time to AM/PM format from 24 hour
                if (hour == 0) {
                    hour += 12;
                    ampm = " AM";
                } else if (hour == 12) {
                    ampm = " PM";
                } else if (hour > 12) {
                    hour -= 12;
                    ampm = " PM";
                } else {
                    ampm = " AM";
                }

                end.setText(hour +":"+minute+ampm);

                if(startTime > endTime){ //if start time is after end time
                    start.setTextColor(Color.RED); //set time text color to red
                    end.setTextColor(Color.RED);
                    Toast.makeText(ActivityRegChoose.this, "start time must be earlier than end time", Toast.LENGTH_SHORT).show();
                }
                else{ //if start time is before end time
                    start.setTextColor(oldColors); //set time text color to default color
                    end.setTextColor(oldColors);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("endTime", hour +":"+minute+ampm);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
    }

    public void clickGrabBag(View view){ //navigate to grab bag creation activity
        Intent intent = new Intent(this, ActivityRegGrabBag.class);
        startActivity(intent);
    }

    public void clickRegular(View view){ //navigate to regular item creation activity
        Intent intent = new Intent(this, ActivityRegCreateMenu.class);
        startActivity(intent);
    }

    public void clickFinish(View view){
        boolean madeGrabBag = (1 == sharedPref.getInt("madeGrabBag", 0));
        boolean madeMenu = (1 == sharedPref.getInt("madeMenu", 0));

        //enforcet that at least one regular menu item or a grab bag is created, start time < end time,
        //and that the restaurant has a description
        if((madeGrabBag || madeMenu) && (startTime < endTime) && !description.getText().toString().isEmpty()){ //if ready
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("restaurantDescription", description.getText().toString().trim());
            editor.commit();

            Toast.makeText(ActivityRegChoose.this, "Registration Complete", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NavDrawer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if(startTime > endTime){
            Toast.makeText(ActivityRegChoose.this, "start time must be earlier than end time", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ActivityRegChoose.this, "You must create either a Grab-Bag Meal or a regular menu item", Toast.LENGTH_SHORT).show();
        }
    }
}
