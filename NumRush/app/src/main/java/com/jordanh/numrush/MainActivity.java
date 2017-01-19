package com.jordanh.numrush;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highScore = (TextView) findViewById(R.id.highScore);

        SharedPreferences sp = getSharedPreferences("Scores", Activity.MODE_PRIVATE);
        int myHigh = sp.getInt("high_score", -1);

        highScore.setText("My Highscore: " + String.valueOf(myHigh));
    }

    public void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void showInstructions(View view){
        AlertDialog.Builder instructionsDialog = new AlertDialog.Builder(this);
        instructionsDialog.setTitle("Instructions");
        instructionsDialog.setMessage("Answer as many questions as you can before time runs out!\n" +
                "If you answer incorrectly, you will lose 5 seconds.");
        instructionsDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int d){
                dialog.dismiss();
            }
        });
        instructionsDialog.show();
    }
}
