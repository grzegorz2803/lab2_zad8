import java.util.Stack;

public class ONPCalculator {
    public   int ONPResult(String expression){
        String onpExpression = convertToONP(expression);
        return  calculateONP(onpExpression);
    }
    private String convertToONP(String expression){ // wyrażenie w notacji 2 * 3 - (18 - 4) / 2 + 2^3 =
        StringBuilder onpExpr = new StringBuilder(); // budowanie stringa z wyrażeniem ONP
        Stack<Character> opStack = new Stack<>(); // stos dla operatorów
        for (char token: expression.toCharArray()) { // pętla po równaniu
            if(Character.isDigit(token)){ // jeśli cyfra dodajemy ją do ONP
                onpExpr.append(token).append(" ");
            } else if (token =='(') { // jeśli otwierający nawias dodajemy na stos operatorów
                opStack.push(token);
            } else if (token==')') { // jeśli zamykający nawias to przesuwamy operatory z góry stosu do ONP aż do nawisu otwierającego
                while (!opStack.isEmpty()&&opStack.peek()!='('){
                    onpExpr.append(opStack.pop()).append(" ");
                }
                opStack.pop(); // usuwamy znak  '(' ze stosu
            }else if(token ==' '){}else if(token=='='){break;}else   {
                // operatory
                while (!opStack.isEmpty()&&operatorPiority(token)<=operatorPiority(opStack.peek())){ // jeśli operatory ze stosu mają mniejszy lub równy priorytet o obecnego to przesuwamy jes z góry stosu
                    onpExpr.append(opStack.pop()).append(" ");
                }
                opStack.push(token);
            }
        }
        while (!opStack.isEmpty()){
            onpExpr.append(opStack.pop()).append(" "); // po zakończeniu pętli przesuwamy resztę operatorów do ONP
        }

        return onpExpr.toString().trim(); // zwracamy wyrażenie ONP i usuwamy białe znaki
    }
    private int operatorPiority(char operator){ // zwraca priorytet operatorów
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }
    private  int calculateONP(String onpExpression){
        Stack<Integer> stack = new Stack<>();
        String[] tokens =  onpExpression.split(" "); // dzielimy wyrażenie onp po " "
        for (String token:tokens) {
            if(isNumeric(token)){ // jeśli liczba dodajemy do stosu
                stack.push(Integer.parseInt(token));
            }else {
                // jeśli operator to wykonujemy operację na dwóch ostatnich liczbach
                int a = stack.pop();
                int b = stack.pop();
                switch (token){
                    case "+":
                        stack.push(a+b);
                        break;
                    case "-":
                        stack.push(b-a);
                        break;
                    case "*":
                        stack.push(a*b);
                        break;
                    case "/":
                        stack.push(b/a);
                        break;
                    case "^":
                        stack.push((int)Math.pow(b,a));
                        break;
                }
            }
        }
        return stack.pop(); // ostateczny wynik na stosie
    }
    private boolean isNumeric(String str){
        try{
            Integer.parseInt(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
