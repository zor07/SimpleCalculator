package com.zor07.simplecalculator;

import android.view.View;
import android.widget.Button;

import java.math.BigDecimal;
import java.util.regex.Pattern;


public class ExpressionBuilding {
    /**
     * Определяет создание нового выражения,
     * если isNewExpression == true, то создается новое выражение,
     * иначе продолжается старое
     */
    private static boolean isNewExpression;

    /**
     * expression, текущее выражение
     */
    private static String expression;

    /**
     *Счетчики открывающих и закрывающих скобок
     */
    private static int openedBrCount, closedBrCount;

    /**
     * Результат вычислений
     */
    private static BigDecimal result;

    /**
     * Закрытый конструктор класса.
     */
    private ExpressionBuilding() {
    }

    /**
     * Обрабатывает нажатие цифры.
     * @param v View, нажатая кнопка
     */
    public static void addNumber(View v){
        if (expression.length() < 30) {
            Button btn = (Button) v;
            String btnText = btn.getText().toString();
            if (Pattern.matches(".*\\)", expression))
                expression += "*" + btnText;
            else
                expression += btnText;
        }
    }

    /**
     * Добавляет открывающую скобку в expression
     * возможные случаи:
     * Скобка ставится после цифры или закрывающей скобки
     * тогда функция ставит перед скобкой знак умножения;
     * Скобка ставится во всех остальных случаях;
     *
     * Увеличивает счетчик открытых скобок на 1
     */
    public static void addOpenBr(){
        if (Pattern.matches(".*[\\d\\)]", expression)){
            expression += "*(";
        } else
            expression += "(";

        openedBrCount ++;
    }

    /**
     * Добавляет закрывающую скобку в expression
     * Только если закрывающих скобок меньше чем открывающих
     */
    public static void addCloseBr(){
        if (openedBrCount > closedBrCount){
            if (Pattern.matches(".*[\\d.)]", expression)){
                //закрывающая скобка после числа или точки
                expression += ")";
                closedBrCount ++;
            }
        }
    }

    /**
     * Обрабатывает нажатие кнопки "C"
     * Сбрасывает значения всех TextView, expression, счетчиков открытых и закрытых скобок
     */
    public static void clear(){
        openedBrCount = 0;
        closedBrCount = 0;
        isNewExpression = false;
        expression = "";
        result = null;
    }

    /**
     * Обрабатывает нажатие кнопки BCSP.
     * Стирает 1 символ
     */
    public static void backSpace(){
        if (!atTheStart()) {
            deleteLastCharFromExpression();
        }
    }

    /**
     * Добавляет дробный разделитель в число
     */
    public static void addComma(){
        if (Pattern.matches(".*[\\d]", expression)) {
            String[] nums = expression.split("[()+\\-*/]");
            String lastNum = nums[nums.length - 1];

            if (!lastNum.contains(".")) {
                expression += ".";
            }
        }
    }

    /**
     * Обрабатывает нажатие кнопки "="
     */
    public static String equal(){
        try {
            result = ExpressionCalculation.calculate(expression);
            isNewExpression = true;
            if (result == null) return "Ошибка";
        } catch (Exception e){
            return "Ошибка";
        }
        return String.valueOf(result);
    }

    /**
     * Добавляет знак действия в строковое выражение expression
     * @param action знак действия, '+' | '-' | '*' | '/'
     */
    public static void addActionSymbol(char action){
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

    /**
     * Каждое нажатие кнопки, после равно, приводит к обновлению выражения,
     * и его новому построению
     * @param lastPressedNotEqual последняя нажатая кнопка не равно
     * @return
     */
    public static boolean setNewExpression(boolean lastPressedNotEqual) {
        if (isNewExpression && lastPressedNotEqual){
            clear();
            return true;
        }
        return false;
    }

    /**
     * Геттер для expression
     * @return expression
     */
    public static String getExpression() {
        return expression;
    }

    /**
     * Сеттер для expression
     * @param expr значение для expression
     */
    public static void setExpression(String expr) {
        expression = expr;
    }

    /**
     * Функция проверяет находится ли курсор в начале строки
     * @return true если expression = "" иначе false
     */
    private static boolean atTheStart(){
        return expression.length() == 0;
    }

    /**
     * Обработка нажатия кнопки Минус
     */
    private static void addMinus(){
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
        } else if (Pattern.matches(".*[\\d\\()]", expression)){
            //минус после цифры открывающей или закрывающей скобки
            expression += "-";
        }
    }

    /**
     * Удаляем последний символ expression
     */
    private static void deleteLastCharFromExpression(){
        if (expression == null || expression.length() == 0) {
            return;
        } else
            expression =  expression.substring(0, expression.length()-1);
    }
}
