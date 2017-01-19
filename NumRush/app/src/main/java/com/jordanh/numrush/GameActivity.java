package com.jordanh.numrush;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Jordan H on 2017-01-17.
 */

public class GameActivity extends AppCompatActivity{

    TextView question1;
    TextView question2;
    TextView question3;
    TextView timeLeft;
    TextView score;
    CountDownTimer timer;
    Question[] questions = new Question[10];
    Button[] answers = new Button[5];
    Question currentQuestion;
    Set<Integer> uniqueAnswers = new HashSet<>();
    long timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Instantiating UI from activity_game
        question1 = (TextView) findViewById(R.id.Question1);
        question2 = (TextView) findViewById(R.id.Question2);
        question3 = (TextView) findViewById(R.id.Question3);

        answers[0] = (Button) findViewById(R.id.answer1);
        answers[1] = (Button) findViewById(R.id.answer2);
        answers[2] = (Button) findViewById(R.id.answer3);
        answers[3] = (Button) findViewById(R.id.answer4);
        answers[4] = (Button) findViewById(R.id.answer5);

        timeLeft = (TextView) findViewById(R.id.timer);

        score = (TextView) findViewById(R.id.score);

        for(int i = 0; i < questions.length; i++){
            questions[i] = generateQuestion();
            //check if exists in hashset
            while(uniqueAnswers.contains(questions[i].getAnswerInt())){
                questions[i] = generateQuestion();
            }
            uniqueAnswers.add(questions[i].getAnswerInt());
        }




        question1.setText(questions[0].getQuestion());
        question2.setText(questions[1].getQuestion());
        question3.setText(questions[2].getQuestion());

        currentQuestion = questions[0];

        Random rand = new Random();
        int i, j , k; //variables for random numbers for first 3 questions mapped to answer buttons
        i = rand.nextInt(5);
        answers[i].setText(questions[0].getAnswer());//answers question 1

        do{
            j = rand.nextInt(5);
        }while(i == j);
        answers[j].setText(questions[1].getAnswer());//answer for question 2

        do{
            k = rand.nextInt(5);
        }while(i == k || j == k);
        answers[k].setText(questions[2].getAnswer());

        //handle answers for next 2 possible questions
        questions[3] = generateQuestion();
        int a, b;
        do {
            a = rand.nextInt(5);
        }while(a == i || a == j || a == k);
        answers[a].setText(questions[3].getAnswer());

        questions[4] = generateQuestion();
        do{
            b = rand.nextInt(5);
        }while( b == i || b == j || b == k || b == a);
        answers[b].setText(questions[4].getAnswer());

        score.setText("0");

        timeRemaining = 30000;
        createCountDownTimer(timeRemaining);
    }

    public void createCountDownTimer(long period){
        timer = new CountDownTimer(period, 1000){
            public void onTick(long millisUnitilFinished){
                timeLeft.setText(String.valueOf(millisUnitilFinished/1000));
                timeRemaining = millisUnitilFinished;
            }
            public void onFinish(){
                endGame();
            }
        }.start();
    }

    // Generates a random question and returns it
    public Question generateQuestion(){
        Random rand = new Random();
        int decider = rand.nextInt(3);
        int first = 1, second = 1, answer = 1;
        String operand = "";
        Question question = null;
        switch (decider){
            case 0:
                operand = "+";
                first = rand.nextInt(100);
                second = rand.nextInt(100);
                answer = first + second;
                break;

            case 1:
                operand = "-";
                do {
                    first = rand.nextInt(100);
                    second = rand.nextInt(100);
                }while(first <= second);
                answer = first - second;
                break;

            case 2:
                operand = "*";
                first = rand.nextInt(16);
                second = rand.nextInt(16);
                answer = first * second;
                break;

            case 3:
                operand = "/";
                do {
                    first = rand.nextInt(226);
                    second = rand.nextInt(15) + 1;
                }while(first % second == 0);
                answer = first / second;
                break;
        }
        question = new Question(first, second, operand, answer);
        return question;
    }

    public void checkAnswer(View v){
        Button b = (Button) v;
        String newAnswer;
        if(b.getText().equals(currentQuestion.getAnswer())){
            incrementScore();
            newAnswer = refreshQuestionsandAnswers();
            b.setText(newAnswer);
        }else{
            Toast.makeText(this, "Incorrect", Toast.LENGTH_LONG).show();
            timer.cancel();
            if(timeRemaining <= 5000){
                timeLeft.setText("0");
                endGame();
            }
            timeRemaining -= 5000;
            createCountDownTimer(timeRemaining);
        }

    }

    public void incrementScore(){
        int cur = Integer.parseInt(score.getText().toString());
        cur++;
        score.setText(String.valueOf(cur));
    }

    public String refreshQuestionsandAnswers(){

        Random rand = new Random();
        int newQuestion;
        String newAnswer;
        boolean done = true, question1OK = false, question2OK = false, question3OK = false;

        uniqueAnswers.remove(currentQuestion.getAnswerInt());
        for(int i = 1; i < questions.length; i++){
            questions[i - 1] = questions[i];
        }
        questions[9] = generateQuestion();
        while(uniqueAnswers.contains(questions[9].getAnswerInt())){
            questions[9] = generateQuestion();
        }
        uniqueAnswers.add(questions[9].getAnswerInt());
        question1.setText(questions[0].getQuestion());
        question2.setText(questions[1].getQuestion());
        question3.setText(questions[2].getQuestion());
        currentQuestion = questions[0];

        while(true){
            done = true;
            newQuestion = rand.nextInt(6) + 4;
            newAnswer = questions[newQuestion].getAnswer();
            for(int k = 0; k < answers.length; k++){
                if(answers[k].getText().equals(newAnswer)){
                    done = false;
                }
            }
            if(done){
                break;
            }
        }

        for(int j = 0; j < answers.length; j++){
            if(questions[0].getAnswer().equals(answers[j].getText())){
                question1OK = true;
            }else if(questions[1].getAnswer().equals(answers[j].getText())){
                question2OK = true;
            }else if(questions[2].getAnswer().equals(answers[j].getText())){
                question3OK = true;
            }
        }
        if(!question1OK){
            return questions[0].getAnswer();
        }
        if(!question2OK){
            return questions[1].getAnswer();
        }
        if(!question3OK){
            return questions[2].getAnswer();
        }
        return newAnswer;
    }


    public void endGame(){

        SharedPreferences sp = getSharedPreferences("Scores", Activity.MODE_PRIVATE);
        int myHigh = sp.getInt("high_score", -1);
        int myScore = Integer.parseInt(score.getText().toString());

        if(myHigh < myScore) {
            myHigh = myScore;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("high_score", myScore);
            editor.commit();
        }

        AlertDialog.Builder endDialogue = new AlertDialog.Builder(this);
        endDialogue.setTitle("Game Over!");
        endDialogue.setMessage("Your final score was " + myScore + "\nYou're highest score is: " + myHigh);
        endDialogue.setCancelable(false);
        endDialogue.setPositiveButton("Play again", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int d){
                dialog.dismiss();
                recreate();
            }
        });
        endDialogue.setNegativeButton("Main menu", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int d){
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        endDialogue.show();
    }

}
