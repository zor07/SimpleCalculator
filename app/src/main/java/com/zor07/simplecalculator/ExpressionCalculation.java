package com.zor07.simplecalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.regex.Pattern;


public class ExpressionCalculation {

    /**
     * Карта приоритета операторов
     */
    private static HashMap<String,Integer> priorityMap = new HashMap(){{
       put("(", 0);
        put("(", 0);
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
        put("<", 3);
    }};

    private ExpressionCalculation() {
    }

    public static void main(String[] args) {
        System.out.println(
                calculate(null) + "\n" +
                calculate("(4 - 2 * (2 + 3) / 8 * (1 + 4))") + "\n" +
                calculate("1+1+ASD") + "\n" +
                calculate("ASDAS") + "\n" +
                calculate("-1+(-10/(4-2*2))") + "\n" +
                calculate("1+1.023*4") + "\n" +
                calculate("1.000039999 + 1.000000001") + "\n"
        );
    }

    /**
     * Вычисляет значение строкового выражения
     * @param expression строковое выражение
     * @return результат выражения, округленное до 4-ех знаков, null если результат не может быть вычислен
     */
    public static BigDecimal calculate(String expression){
        if (expression == null) return null;
        BigDecimal result;
        ArrayList<String> postFixExpression;
        try {
            //переводим исходное выражение в выражение записанное в обратной польской нотации
            postFixExpression = (ArrayList<String>) toPostfix(expression);
            if (postFixExpression == null) return null;
        } catch (IllegalExpressionException e) {
            //отлавливается, если исходное выражение содержите недопустимые символы. Возвращаем null
            return null;
        }
        //вычисляем значение выражения записанное в обратной польской нотации
        result = calculatePostFixExpression(postFixExpression);

        //округляем результат до 4-ех символов
        return result;
//        if (result != null) {
//            result = result.setScale(4, BigDecimal.ROUND_HALF_UP);
//            return result;
//        } else return null;
    }

    /**
     * Переводит строковое выражение записанное в инфиксной нотации, в постфиксную нотацию
     * @param expression исходное строковое выражение
     * @return список эллементов, составляющих выражение в обратной польской нотации, null, если
     *         выражение составлено неверно.
     * @throws IllegalExpressionException в случае если исходное выражение содержит недопустимые символы
     */
    private static List<String> toPostfix(String expression) throws IllegalExpressionException{
        //заменяем все унарные минусы на "<"
        expression = replaceSingleMinus(expression);

        Stack<String> stack = new Stack();
        ArrayList<String> elements = (ArrayList<String>) splitToParts(expression);
        ArrayList<String> postFixElements = new ArrayList<>();

        for (String element : elements){
            if (!Pattern.matches("\\(|\\)|\\+|-|\\*|/|<", element)) {
                //текущий эллемент - число
                //число добавляем в стек
                postFixElements.add(element);
            } else {
                //текущий эллемент - знак
                if (element.equals("("))
                    //Если открывающая скобка - кладем её в стэк
                    stack.push(element);
                else if (element.equals(")")){
                    //при закрывающей скобке
                    while (true){
                        String elementFromStack;
                        try {
                            //достаем эллемент из стэка
                            elementFromStack = stack.pop();
                        } catch (EmptyStackException e){
                            //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                            //Ловим соответствующее исключение, возвращаем null
                            return null;
                        }
                        if (elementFromStack.equals("("))
                            //если эллемент открывающая скобка, завершаем цикл
                            break;
                        else
                            //иначе дописываем эллемент в выражение
                            postFixElements.add(elementFromStack);
                    }
                } else {
                    if (stack.isEmpty())
                        //Если стек пуст, записываем в него знак
                        stack.push(element);
                    else {
                        while (true) {
                            if (stack.isEmpty()){
                                //Если стек пуст, записываем в него знак
                                stack.push(element);
                                break;
                            } else {
                                try {
                                    if (isHigherPriority(element, stack.peek())) {
                                        //Если приоритетет текущего оператора выше
                                        //приоритета оператора на вершине стэка
                                        //записываем в стэк оператор
                                        stack.push(element);
                                        break;
                                    } else {
                                        //Иначе достаем из стэка оператор, дописываем его в выражение
                                        postFixElements.add(stack.pop());
                                    }
                                } catch (EmptyStackException e) {
                                    //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                                    //Ловим соответствующее исключение, возвращаем null
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        while (!stack.isEmpty()){
            //Оставшиеся в стэке эллементы
            //дописываем в выражение
            postFixElements.add(stack.pop());
        }
        return postFixElements;
    }

    /**
     * Вычисляет результат выражения, записанного в обратной польской нотации
     * @param expression список эллементов выражения записанного в обратной польской нотации
     * @return результат выражения, null если выражение не может быть вычислено
     */
    private static BigDecimal calculatePostFixExpression(List<String> expression){
        BigDecimal result;
        Stack<BigDecimal> decimals = new Stack<>();

        for (String element : expression) {
            //перебираем эллементы выражения
            if (Pattern.matches("\\(|\\)|\\+|-|\\*|/", element)){
                // текущий эллемент - оператор «+», «-», «*», «/», «.»
                // достаем из стэка два числа
                BigDecimal decimal1 = null, decimal2 = null;
                try {
                    decimal1 = decimals.pop();
                    decimal2 = decimals.pop();
                } catch (EmptyStackException e){
                    //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                    //Ловим соответствующее исключение, возвращаем null
                    return null;
                }

                // производим между числами соответствующее действие
                if (element.equals("+")){
                   decimals.push(decimal2.add(decimal1));
                } else if (element.equals("-")) {
                    decimals.push(decimal2.subtract(decimal1));
                } else if (element.equals("*")) {
                    decimals.push(decimal2.multiply(decimal1));
                } else if (element.equals("/")) {
                    try {
                        decimals.push(decimal2.divide(decimal1, MathContext.DECIMAL32));
                    } catch (ArithmeticException e){
                        //при делении на ноль возвращаем null
                        return null;
                    }

                }
            } else if (element.equals("<")) {
                //текущий элемент - унарный минус
                // достаем из стэка число
                BigDecimal decimal = null;
                try {
                    decimal = decimals.pop();
                } catch (EmptyStackException e){
                    //Если на данном этапе стэк оказался пуст, выражение составлено не верно
                    //Ловим соответствующее исключение, возвращаем null
                    return null;
                }
                //Кладем обратно в стек число с обратным знаком
                decimals.push(decimal.multiply(new BigDecimal("-1")));
            } else {
                // текущий эллемент - оператор, записываем его в стэк
                BigDecimal decimal;
                try {
                    decimal = new BigDecimal(element);
                } catch (NumberFormatException e){
                    // Если не удалось перевести число из String в BigDecimal
                    // изначальное выражение содержало ошибку.
                    // Ловим соответствующее исключение, возвращаем null
                    return null;
                }
                decimals.push(decimal);
            }
        }

        // На данном этапе стэк должен содержать только результат выражения
        // Если содержит еще что то, в исходном выражении была допущена ошибка
        // Возвращаем null
        result = decimals.pop();
        if (decimals.isEmpty())
            return result;
        else
            return null;
    }

    /**
     * Сравнивает приоритеты двух операций (+, -, *, /)
     * @param element1 операция 1
     * @param element2 операция 2
     * @return true если приоритет операции 1 больше приоритета операции 2, иначе false
     */
    private static boolean isHigherPriority(String element1, String element2){
        return priorityMap.get(element1) > priorityMap.get(element2);
    }

    /**
     *
     * Функция разбивает стоковое выражения на список из компонентов выражения
     * Например выражение 1 + (93 * 4.05) преобразуется в список с элементами:
     * [1,+,(,93,*,4.05,)]
     * @param expression - исходное выражение
     * @return список элементов выражения в порядке следования в исходном выражении
     * @throws IllegalExpressionException в случае, если выражение содержит недопустимые символы (любые, кроме цифр, «+», «-», «*», «/», «.», «<»)
     */
    private static List<String> splitToParts(String expression) throws IllegalExpressionException {
        ArrayList<String> partsOfExpression = new ArrayList<>();

        //удаляем все пробелы
        expression = expression.replaceAll(" ", "");
        //разбиваем строку на массив символов
        String[] chars = expression.split("");

        //буферная переменная, собираем в нее числа
        String bufferForNumber = "";
        for (int i = 0; i < chars.length; i++) {
            //перебираем символы
            if (!"".equals(chars[i]) && Pattern.matches("\\(|\\)|\\+|-|\\*|/|<", chars[i])){
                //если текущий символ знак

                if (!bufferForNumber.equals("")) {
                    //если буфер не пуст, записываем его в список, очищаем буфер
                    partsOfExpression.add(bufferForNumber);
                    bufferForNumber = "";
                }
                //добавляем знак в список
                partsOfExpression.add(chars[i]);
            } else if (Pattern.matches("[\\d.]", String.valueOf(chars[i]))){
                //если цифра или знак разделителя
                bufferForNumber += chars[i];
            } else {
                //если не цифра и не оператор и не знак разделителя - пробрасываем исключение
                if (!"".equals(chars[i]))
                    throw new IllegalExpressionException();
            }
        }

        //Если в буфере осталось число, записываем его
        if (!bufferForNumber.equals("")) partsOfExpression.add(bufferForNumber);

        return partsOfExpression;
    }

    /**
     * Заменяет унарный минус на знак "<"
     * @param expression исходное выражение
     * @return
     */
    private static String replaceSingleMinus(String expression){
        String res = expression.replaceAll("^-d*","<");
        res = res.replaceAll("\\(-","(<");
        return res;
    }

}
