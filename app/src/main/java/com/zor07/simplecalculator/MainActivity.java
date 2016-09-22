package com.zor07.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;
    Button btnEquals;
    Button btnComma;
    Button btnClear;
    Button[] btnsNumber = new Button[10];

    TextView tvResult;
    TextView tvInfo;

    char selectedAction = ' '; // +, -, /, или *
    Operation operation;
    double currentResult = 0;
    double displayValue = 0;
    double eqN1 = 0, eqN2 = 0;
    boolean newNumber, eqInitialized, firstSubOperation = true;

    enum Operation{
        ADD,
        SUB,
        DIV,
        MULT,
        NULL
    }
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

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

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
        String displayText = tvResult.getText().toString();

        if (!"".equals(displayText)){
            displayValue = Double.parseDouble(displayText.replace(",", "."));
        }

        switch (v.getId()) {
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
            case R.id.btnClear:
                clear();
                break;
            case R.id.btnComma:
                addComma(displayText);
                break;
            case R.id.btnEq:
                equal(operation);
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
                    tvResult.setText(displayText);
                }
                break;
        }
    }
    public boolean replaceActionSymbol(String displayText, char symbol){
        if ("".equals(displayText)){
            String currentInfoText = tvInfo.getText().toString();
            int len = currentInfoText.length();
            String newInfoText = currentInfoText.replace(currentInfoText.charAt(len - 1), symbol);
            tvInfo.setText(newInfoText);
            return true;
        } else
            return false;
    }
    public void add(){
        firstSubOperation = true;
        selectedAction = '+';
        operation = operation.ADD;
        if (eqInitialized)
            currentResult = displayValue;
        else
            currentResult += displayValue;
        tvInfo.setText(currentResult + "+");
        tvResult.setText("");
        displayValue = 0;
        eqInitialized = false;
    }
    public void sub(){

        selectedAction = '-';
        operation = operation.SUB;
        if (!eqInitialized)
            if (!firstSubOperation) {
                currentResult -= displayValue;

            }
            else {
                firstSubOperation = false;
                currentResult = displayValue;
            }
        else
            currentResult = displayValue;
        tvInfo.setText(currentResult + "-");
        tvResult.setText("");
        displayValue = 0;
        eqInitialized = false;

    }
    public void div(){
        firstSubOperation = true;
        selectedAction = '/';
        operation = operation.DIV;
        if (eqInitialized)
            currentResult = displayValue;
        else
            currentResult /= displayValue;
        tvInfo.setText(currentResult + "/");
        tvResult.setText("");
        displayValue = 0;
        eqInitialized = false;
    }
    public void mult() {
        firstSubOperation = true;
        selectedAction = '*';
        operation = operation.MULT;
        if (eqInitialized)
            currentResult = displayValue;
        else
            currentResult *= displayValue;
        tvInfo.setText(currentResult + "*");
        tvResult.setText("");
        displayValue = 0;
        eqInitialized = false;

    }
    public void equal(Operation operation){
        if (!eqInitialized) {
            eqN1 = currentResult;
            eqN2 = displayValue;
            eqInitialized = true;
        }
        switch (operation.ordinal()){
            case (0): //ADD
                currentResult = eqN1 + eqN2;
                break;
            case (1): //SUB
                currentResult = eqN1 - eqN2;
                break;
            case (2): //DIV
                try {
                    if (displayValue == 0) throw new ArithmeticException();
                    currentResult = eqN1 / eqN2;
                } catch (ArithmeticException e){
                    clear();
                    Toast.makeText(this, "Деление на ноль!", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case (3): //MULT
                currentResult = eqN1 * eqN2;
                break;
        }
        tvInfo.setText(String.valueOf(eqN1) +  selectedAction + String.valueOf(eqN2));
        tvResult.setText(""+currentResult);
        newNumber = true;
    }
    public void clear(){
        firstSubOperation = true;
        eqN1 = 0;
        eqN2 = 0;
        displayValue = 0;
        currentResult = 0;
        operation = Operation.NULL;
        tvInfo.setText("");
        tvResult.setText("");
    }
    public void addComma(String displayText){
        if (!displayText.contains(".")){
            if (displayText.length() == 0){
                displayText += "0.";
            } else {
                displayText += ".";
            }
            tvResult.setText(displayText);
        }}
}
