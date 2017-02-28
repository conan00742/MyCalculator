package com.example.user.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsualCalculator extends AppCompatActivity {
    @BindView(R.id.tvCalculation)
    TextView tvCalculation;

    @BindView(R.id.tvResult)
    TextView tvResult;

    String displayedValue;
    int numberValue;
    double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usual_calculator);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.btnNumPad0 , R.id.btnNumPad1 , R.id.btnNumPad2
            , R.id.btnNumPad3 , R.id.btnNumPad4 , R.id.btnNumPad5
            , R.id.btnNumPad6 , R.id.btnNumPad7 , R.id.btnNumPad8
            , R.id.btnNumPad9 , R.id.btnNumPadEqual , R.id.btnNumPadPoint
            , R.id.btnNumPadSummation , R.id.btnNumPadSubtraction , R.id.btnNumPadMultiplication
            , R.id.btnNumPadDivision , R.id.btnNumPadClear , R.id.btnNumPadClearAll
            , R.id.btnNumPadFactorial , R.id.btnNumPadMod})
    public void enterOperators(View view){
        int id = view.getId();
        switch (id){
            case R.id.btnNumPad0:
                displayedValue = "0";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad1:
                displayedValue = "1";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad2:
                displayedValue = "2";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad3:
                displayedValue = "3";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad4:
                displayedValue = "4";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad5:
                displayedValue = "5";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad6:
                displayedValue = "6";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad7:
                displayedValue = "7";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad8:
                displayedValue = "8";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPad9:
                displayedValue = "9";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadEqual:
                //do the calculation
                String fullText = tvCalculation.getText().toString();
                if(TextUtils.isDigitsOnly(fullText)){
                    tvResult.setText(fullText);
                }else{
                    tvResult.setText(""+(long)evaluate(fullText));
                }
                break;
            case R.id.btnNumPadPoint:
                displayedValue = ".";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadClear:
                //delete 1 character
                break;
            case R.id.btnNumPadClearAll:
                tvCalculation.setText("");
                tvResult.setText("");
                break;
            case R.id.btnNumPadSummation:
                //do the +
                displayedValue = " + ";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadSubtraction:
                //do the -
                displayedValue = " - ";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadMultiplication:
                //do the ×
                displayedValue = " × ";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadDivision:
                //do the ÷
                displayedValue = " ÷ ";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadMod:
                //do the mod
                displayedValue = " % ";
                tvCalculation.append(displayedValue);
                break;
            case R.id.btnNumPadFactorial:
                //do the factorial
                displayedValue = "!";
                tvCalculation.append(displayedValue);
                break;
        }



    }



    public static double evaluate(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('×')) x *= parseFactor(); // multiplication
                    else if (eat('÷')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
