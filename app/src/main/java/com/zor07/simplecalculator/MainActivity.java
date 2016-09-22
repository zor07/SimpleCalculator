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
        String displayText = tvExpression.getText().toString();

        switch (v.getId()) {
            /*
            case R.id.btnAdd:
                //if (!replaceActionSymbol(displayText, '+'))
                add();
                break;
            case R.id.btnSub:
                //if (!replaceActionSymbol(displayText, '-'))
                sub();
                break;
            case R.id.btnMult:
                //if (!replaceActionSymbol(displayText, '*'))
                mult();
                break;
            case R.id.btnDiv:
                //if (!replaceActionSymbol(displayText, '/'))
                div();
                break;
            */

            case R.id.btnClear:
                clear();
                break;
            case R.id.btnComma:
                addComma();
                break;

            case R.id.btnEq:
                equal();
                break;

            default:
                //Для цифр дописываем нажатую цифру в дисплей
                if (eqInitialized) clear();
                eqInitialized = false;
                if (displayText.length() < 30) {
                    Button btn = (Button) v;
                    String btnText = btn.getText().toString();
                    if (newNumber){
                        displayText = btnText;
                        newNumber = false;
                    } else
                        displayText += btnText;
                    tvExpression.setText(displayText);
                }
                break;
        }
    }
    public boolean replaceActionSymbol(String displayText, char symbol){
        if ("".equals(displayText)){
            String currentInfoText = tvResult.getText().toString();
            int len = currentInfoText.length();
            String newInfoText = currentInfoText.replace(currentInfoText.charAt(len - 1), symbol);
            tvResult.setText(newInfoText);
            return true;
        } else
            return false;
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
        }}
}
