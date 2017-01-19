package com.jordanh.numrush;

/**
 * Created by Jordan H on 2017-01-17.
 */

public class Question {

    private int num1;
    private int num2;
    private String operand;
    private String question;
    private int answer;
    public boolean isAnswered;

    public Question(int num1, int num2, String operator, int i){

        this.num1 = num1;
        this.num2 = num2;
        operand = operator;
        answer = i;
        question = num1 + " " +  operand + " " + num2;
        this.isAnswered = false;
    }

    public String getQuestion(){
        return question;
    }

    public String getAnswer(){
        return String.valueOf(answer);
    }

    public int getAnswerInt(){
        return answer;
    }
}
