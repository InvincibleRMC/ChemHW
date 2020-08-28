import java.util.Arrays;

import java.util.Comparator;
import java.util.Scanner;

public class ChemHW {

    static String[][] periodicTable = new String[23][2];

    public static void main(final String[] args) {

        periodicTable[0][0] = "h";
        periodicTable[0][1] = "1.00794";
        periodicTable[1][0] = "he";
        periodicTable[1][1] = "4.002602";
        periodicTable[2][0] = "li";
        periodicTable[2][1] = "6.941";
        periodicTable[3][0] = "be";
        periodicTable[3][1] = "9.012182";
        periodicTable[4][0] = "b";
        periodicTable[4][1] = "10.811";
        periodicTable[5][0] = "c";
        periodicTable[5][1] = "12.0107";
        periodicTable[6][0] = "n";
        periodicTable[6][1] = "	14.0067";
        periodicTable[7][0] = "o";
        periodicTable[7][1] = "15.9994";
        periodicTable[8][0] = "f";
        periodicTable[8][1] = "18.9984032";
        periodicTable[9][0] = "ne";
        periodicTable[9][1] = "20.1797";
        periodicTable[10][0] = "na";
        periodicTable[10][1] = "22.989770";
        periodicTable[11][0] = "mg";
        periodicTable[11][1] = "24.3050";
        periodicTable[12][0] = "al";
        periodicTable[12][1] = "26.981538";
        periodicTable[13][0] = "si";
        periodicTable[13][1] = "28.0855";
        periodicTable[14][0] = "p";
        periodicTable[14][1] = "30.973761";
        periodicTable[15][0] = "s";
        periodicTable[15][1] = "32.065";
        periodicTable[16][0] = "cl";
        periodicTable[16][1] = "35.453";
        periodicTable[17][0] = "ar";
        periodicTable[17][1] = "39.948";
        periodicTable[18][0] = "k";
        periodicTable[18][1] = "39.0983";
        periodicTable[19][0] = "ca";
        periodicTable[19][1] = "40.078";
        periodicTable[20][0] = "sc";
        periodicTable[20][1] = "44.955910";
        periodicTable[21][0] = "ti";
        periodicTable[21][1] = "47.867";
        periodicTable[22][0] = "v";
        periodicTable[22][1] = "50.9415";
        StrinArrayComparator cmp = new StrinArrayComparator();

        Arrays.sort(periodicTable, cmp);

        final Scanner scanner = new Scanner(System.in);

        // System.out.println(tryParseDouble("yikes"));

        while (true) {

            String equation = getEquation(scanner);

            // System.out.println(equation);
            System.out.println(eval(equation));

        }
    }

    public static String getEquation(Scanner scanner) {

        String str = scanner.nextLine();

        for (int i = 0; i < periodicTable.length - 1; i++) {
            str = str.replace(periodicTable[i][0], periodicTable[i][1]);
        }

        return str;
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length())
                    throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+'))
                        x += parseTerm(); // addition
                    else if (eat('-'))
                        x -= parseTerm(); // subtraction
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*'))
                        x *= parseFactor(); // multiplication
                    else if (eat('/'))
                        x /= parseFactor(); // division
                    else
                        return x;
                }
            }

            double parseFactor() {
                if (eat('+'))
                    return parseFactor(); // unary plus
                if (eat('-'))
                    return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z')
                        nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt"))
                        x = Math.sqrt(x);
                    else if (func.equals("sin"))
                        x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos"))
                        x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan"))
                        x = Math.tan(Math.toRadians(x));
                    else
                        throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^'))
                    x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}