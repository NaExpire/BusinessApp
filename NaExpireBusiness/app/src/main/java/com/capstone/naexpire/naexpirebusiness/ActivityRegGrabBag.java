package com.capstone.naexpire.naexpirebusiness;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityRegGrabBag extends AppCompatActivity {

    EditText ingredients, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_grab_bag);

        ingredients = (EditText) findViewById(R.id.txtGrabIngredients);
        price = (EditText) findViewById(R.id.txtGrabPrice);
    }

    public void clickWhatsThis(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_grab_bag_descrip, null);
        Button ok = (Button) mView.findViewById(R.id.btnGrabOk);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
    }

    public void clickGrabNext(View view){
        if(!ingredients.getText().toString().isEmpty() && !price.getText().toString().isEmpty()){
            Intent intent = new Intent(this, ActivityRegCreateMenu.class);
            startActivity(intent);
        }
        else Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
    }

    public void clickGrabSkip(View view){
        Intent intent = new Intent(this, ActivityRegCreateMenu.class);
        startActivity(intent);
    }
}
