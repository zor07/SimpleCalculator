package com.zor07.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;
    Button btnEquals;
    Button btnComma;
    Button btnClear;
    Button btnOpenBr, btnCloseBr, btnBcsp;
    Button[] btnsNumber = new Button[10];

    TextView tvExpression;
    TextView tvResult;

    String expression;
    int openedBrCount, closedBrCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnComma = (Button) findViewById(R.id.btnComma);
        btnEquals = (Button) findViewById(R.id.btnEq);
        btnOpenBr = (Button) findViewById(R.id.btnOpenBr);
        btnCloseBr = (Button) findViewById(R.id.btnCloseBr);
        btnBcsp = (Button) findViewById(R.id.btnBcsp);

        tvExpression = (TextView) findViewById(R.id.tvResult);
        tvResult = (TextView) findViewById(R.id.tvInfo);

        btnsNumber[0] = (Button) findViewById(R.id.btn0);
        btnsNumber[1] = (Button) findViewById(R.id.btn1);
        btnsNumber[2] = (Button) findViewById(R.id.btn2);
        btnsNumber[3] = (Button) findViewById(R.id.btn3);
        btnsNumber[4] = (Button) findViewById(R.id.btn4);
        btnsNumber[5] = (Button) findViewById(R.id.btn5);
        btnsNumber[6] = (Button) findViewById(R.id.btn6);
        btnsNumber[7] = (Button) findViewById(R.id.btn7);
        btnsNumber[8] = (Button) findViewById(R.id.btn8);
        btnsNumber[9] = (Button) findViewById(R.id.btn9);

        // прописываем обработчик
        btnClear.setOnClickListener(this);
        btnComma.setOnClickListener(this);
        btnEquals.setOnClickListener(this);


        btnOpenBr.setOnClickListener(this);
        btnCloseBr.setOnClickListener(this);
        btnBcsp.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);

        for (int i = 0; i <= 9; i++) {
            btnsNumber[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        expression = tvExpression.getText().toString();

        switch (v.getId()) {

            case R.id.btnAdd:
                addActionSymbol('+');
                break;
            case R.id.btnSub:
                addActionSymbol('-');
                break;
            case R.id.btnMult:
                addActionSymbol('*');
                break;
            case R.id.btnDiv:
                addActionSymbol('/');
                break;

            case R.id.btnOpenBr:
                addOpenBr();
                break;

            case R.id.btnCloseBr:
                addCloseBr();
                break;
            case R.id.btnClear:
                clear();
                break;
            case R.id.btnComma:
                addComma();
                break;

            case R.id.btnEq:
                equal();
                break;

            case R.id.btnBcsp:
                if (!atTheStart()) {
                    deleteLastCharFromExpression();
                }
                break;

            default:
                //Для цифр дописываем нажатую цифру в дисплей
                if (expression.length() < 30) {
                    Button btn = (Button) v;
                    String btnText = btn.getText().toString();
                    expression += btnText;

                }
                break;
        }

        tvExpression.setText(expression);
    }

    /**
     * Добавляет открывающую скобку в expression
     * возможные случаи:
     * Скобка ставится после цифры, тогда функция ставит перед скобкой знак умножения;
     * Скобка ставится во всех остальных случаях;
     *
     * Увеличивает счетчик открытых скобок на 1
     */
    public void addOpenBr(){
        if (Pattern.matches(".*[\\d\\(]", expression)){
            expression += "*(";
        } else
            expression += "(";

        openedBrCount ++;
    }


    /**
     * Добавляет закрывающую скобку в expression
     * в случае если закрывающих скобок меньше чем открывающих
     */
    public void addCloseBr(){
        if (openedBrCount > closedBrCount){
            if (Pattern.matches(".*[\\d.]", expression)){
                //закрывающая скобка после числа или точки
                expression += ")";
                closedBrCount ++;
            }
        }
    }

    /**
     * Обработка нажатия кнопки Минус
     */
    private void addMinus(){
        if (atTheStart()) {
            expression = "-";
        } else if (Pattern.matches(".*[*/]", expression)){
            //минус после знака умножения или деления
            expression += "(-";
            openedBrCount ++;
        } else if (Pattern.matches(".*[+.]", expression)){
            //минус после знака плюса или точки
            deleteLastCharFromExpression();
            expression += "-";
        } else if (Pattern.matches(".*[\\d\\(]", expression)){
            //минус после цифры или открывающей скобки
            expression += "-";
        }
    }

    /**
     * Добавляет знак действия в строковое выражение expression
     * @param action знак действия, '+' | '-' | '*' | '/'
     */

    public void addActionSymbol(char action){
        switch (action) {
            case '-':
                //обработка -
                addMinus();
                break;
            default:
                //обработка +,*,/
                if (Pattern.matches(".*[\\d.\\)]", expression)){
                    //знак действия после числа или точки или закрывающей скобки
                    expression += action;

                } else if (Pattern.matches(".*[\\(]", expression)){
                    //знак действия после открывающей скобки
                    //ничего не делаем

                } else if (Pattern.matches(".*\\(-", expression)){
                    //знак действия после "(-"
                    deleteLastCharFromExpression();

                } else if (Pattern.matches("-", expression)){
                    //знак действия после минуса, при условии что expression == "-"
                    deleteLastCharFromExpression();

                } else if (Pattern.matches(".*(\\d|.)[+\\-*/]", expression)) {
                    //знак действия после другого знака, следующего за числом или точкой
                    deleteLastCharFromExpression();
                    expression += action;
                }
                break;
        }
    }

    public void equal(){}


    public void clear(){
        openedBrCount = 0;
        closedBrCount = 0;
        expression = "";
        tvResult.setText("");
        tvExpression.setText("");
    }
    public void addComma(){
        String displayText = "";

        if (!displayText.contains(".")){
            if (displayText.length() == 0){
                displayText += "0.";
            } else {
                displayText += ".";
            }
            tvExpression.setText(displayText);
        }
    }

    /**
     * Функция проверяет находится ли курсор в начале строки
     * @return true если expression = "" иначе false
     */
    private boolean atTheStart(){
        return expression.length() == 0;
    }

    /**
     * Удаляем последний символ expression
     */
    private void deleteLastCharFromExpression(){
        if (expression == null || expression.length() == 0) {
            return;
        } else
            expression =  expression.substring(0, expression.length()-1);
    }
}
