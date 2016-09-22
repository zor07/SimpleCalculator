package com.zor07.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    char selectedAction = ' '; // +, -, /, или *
    double currentResult = 0;

    String expression;
    boolean eqInitialized, newNumber;

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
                break;

            case R.id.btnCloseBr:
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
                if (expression.length() > 0) {
                    expression = expression.substring(0, expression.length() - 1);
                    tvExpression.setText(expression);
                }
                break;

            default:
                //Для цифр дописываем нажатую цифру в дисплей
                if (expression.length() < 30) {
                    Button btn = (Button) v;
                    String btnText = btn.getText().toString();
                    expression += btnText;

                    tvExpression.setText(expression);
                }
                break;
        }
    }

    /**
     * Добавляет открывающуюся скобку в выражение
     * возможные случаи:
     * Скобка ставится после цифры, тогда функция ставит перед скобкой знак умножения;
     * Скобка ставится во всех остальных случаях;
     */
    public void addOpenBr(){

    }

    public void addActionSymbol(char action){
        switch (action) {
            case '-':
                if (atTheStart()){
                    changeAction(action);
                } else {
                    expression = "-";
                    tvExpression.setText(expression);
                }
                break;
            default:
                if (atTheStart()){
                    changeAction(action);
                }
                break;
        }
    }

    public void changeAction(char action){
        char lastExprCh = expression.charAt(expression.length()-1);
        if (lastExprCh == '+' || lastExprCh == '-' || lastExprCh == '*' || lastExprCh == '/')
            expression = expression.substring(0, expression.length() - 1);
        expression += action;

        tvExpression.setText(expression);
    }

    public void equal(){}

    public void clear(){
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

    public boolean atTheStart(){
        return expression.length() == 0;
    }
}
