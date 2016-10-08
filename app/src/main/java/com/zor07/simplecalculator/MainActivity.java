package com.zor07.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{



    /**
     * Кнопки калькулятора
     */
    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;
    Button btnEquals;
    Button btnComma;
    Button btnClear;
    Button btnOpenBr, btnCloseBr, btnBcsp;
    Button[] btnsNumber = new Button[10];

    /**
     * TextView для вводимого выражения и результата
     */
    TextView tvExpression;
    TextView tvResult;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //находим view эллементы по id
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
        if (ExpressionBuilding.setNewExpression(v.getId() != R.id.btnEq)){
            tvResult.setText("");
            tvExpression.setText("");
        }

        String expression = tvExpression.getText().toString();
        ExpressionBuilding.setExpression(expression);

            switch (v.getId()) {

            case R.id.btnAdd:
                ExpressionBuilding.addActionSymbol('+');
                break;
            case R.id.btnSub:
                ExpressionBuilding.addActionSymbol('-');
                break;
            case R.id.btnMult:
                ExpressionBuilding.addActionSymbol('*');
                break;
            case R.id.btnDiv:
                ExpressionBuilding.addActionSymbol('/');
                break;

            case R.id.btnOpenBr:
                ExpressionBuilding.addOpenBr();
                break;

            case R.id.btnCloseBr:
                ExpressionBuilding.addCloseBr();
                break;
            case R.id.btnClear:
                ExpressionBuilding.clear();
                tvResult.setText("");
                tvExpression.setText("");
                break;
            case R.id.btnComma:
                ExpressionBuilding.addComma();
                break;

            case R.id.btnEq:
                String result = String.valueOf(ExpressionBuilding.equal());
                tvResult.setText(result);
                break;

            case R.id.btnBcsp:
                ExpressionBuilding.backSpace();
                break;

            default:
                //Для цифр дописываем нажатую цифру в дисплей
                ExpressionBuilding.addNumber(v);
                break;
        }

        expression = ExpressionBuilding.getExpression();
        tvExpression.setText(expression);
    }
}
