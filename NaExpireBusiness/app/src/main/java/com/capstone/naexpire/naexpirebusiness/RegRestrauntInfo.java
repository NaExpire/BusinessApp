package com.capstone.naexpire.naexpirebusiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegRestrauntInfo extends AppCompatActivity {

    EditText name, address, phone, description, username, email, pphone, password, cPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_restraunt_info);
        setTitle("Register"); //set activity title

        name = (EditText) findViewById(R.id.txtRestrauntName);
        address = (EditText) findViewById(R.id.txtAddress);
        phone = (EditText) findViewById(R.id.txtPhone);
        description = (EditText) findViewById(R.id.txtRestDescription);
        username = (EditText) findViewById(R.id.txtRegUsername);
        email = (EditText) findViewById(R.id.txtEmail);
        pphone = (EditText) findViewById(R.id.txtPersonalPhone);
        password = (EditText) findViewById(R.id.txtRegPassword);
        cPassword = (EditText) findViewById(R.id.txtRegConfirmPass);
    }

    public void clickNext(View view){
        String n = name.getText().toString();
        String a = address.getText().toString();
        String p = phone.getText().toString();
        String d = description.getText().toString();
        String u = username.getText().toString();
        String e = email.getText().toString();
        String pp = pphone.getText().toString();
        String pass = password.getText().toString();
        String cPass = cPassword.getText().toString();

        //makes sure all fields are filled
        Boolean ready = !n.isEmpty() && !a.isEmpty() && !p.isEmpty()
                && !d.isEmpty() && !u.isEmpty() && !e.isEmpty()
                && !pp.isEmpty() && !pass.isEmpty() && !cPass.isEmpty();
        Boolean same = pass.equals(cPass); //checks password is entered the same twice

        if(ready && same){ //if all fields are filled and passwords match
            //rInfo is Rest. Name, Address, Rest. Phone, Description, Username, Email,
            //Personal Phone, Password
            String[] rInfo = {n, a, p, d, u, e, pp, pass};
            Intent intent = new Intent(this, RegFoodTypes.class);
            intent.putExtra("restInfo", rInfo);
            startActivity(intent);
        }
        else if(!ready) Toast.makeText(this, "Fill All Fields", Toast.LENGTH_LONG).show();
        else if(!same) Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_LONG).show();
    }
}
