import java.util.*;

class BigInt {
    private final ArrayList<Integer> num = new ArrayList<>();
    private final boolean sign;

    public BigInt(String input) { // создание нового элемента, тут всё за линию
        sign = input.charAt(0) != '-';
        input = input.replace("-", ""); // на всякий случай удаляю минус
        for (int i = 0; i < input.length(); i++) {
            num.add(Character.getNumericValue(input.charAt(i)));
        }
        while (num.size() > 1 && num.get(0) == 0) {
            num.remove(0);
        }
    }

    public String toString() { // перевод в строку, также за линию
        String resultString = "";
        if (!sign) {
            resultString = "-";
            for (Integer integer : num) resultString = resultString + integer;
        } else {
            for (Integer integer : num) resultString = resultString + integer;
        }
        return resultString;
    }

    public int compareTo(BigInt secondNum) { // сравнение
        if (this.sign && !secondNum.sign) { // если одно положительное, другое отрицательное, тут очевидно
            return 1;
        } else if (!this.sign && secondNum.sign) {
            return -1;
        } else if (this.toString().equals(secondNum.toString())) {
            return 0;
        } else if (this.sign && secondNum.sign) { // сравнение длины, а если одинаковой длины, то познаковое сравнение
            if (this.num.size() > secondNum.num.size()) {
                return 1;
            } else if (this.num.size() < secondNum.num.size()) {
                return -1;
            } else {
                for (int i = 0; i < num.size(); i++)
                    if (num.get(i).equals(secondNum.num.get(i))) {
                    } else if (num.get(i) > secondNum.num.get(i)) {
                        return 1;
                    } else {
                        return -1;
                    }
            }
        } else if (!this.sign && !secondNum.sign) { // сравнение длины, а если одинаковой длины, то познаковое сравнение
            if (this.num.size() > secondNum.num.size()) {
                return -1;
            } else if (this.num.size() < secondNum.num.size()) {
                return 1;
            } else {
                for (int i = 0; i < num.size(); i++) {
                    if (num.get(i) == secondNum.num.get(i)) {
                        continue;
                    } else if (num.get(i) > secondNum.num.get(i)) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }
        return -1;
    }

    public static BigInt valueOf(long input) { // перевод из лонга
        BigInt num = new BigInt(String.valueOf(input));
        return num;
    }

    public BigInt abs() { // функция модуля, реализованная для более удобного пользования классом в дальнейшем
        String resultString = "";
        for (int i = 0; i < this.num.size(); i++) {
            resultString = resultString + this.num.get(i);
        }
        return new BigInt(resultString);
    }

    public BigInt add(BigInt secondNum) {// сложение за линию, по модулю, если разных знаков, то вычитание сразу запускается
        if (this.sign && !secondNum.sign) {
            return this.subtract(secondNum.abs());
        } else if (!this.sign && secondNum.sign) {
            return secondNum.subtract(this.abs());
        }
        int transfer = 0;
        BigInt summand1 = new BigInt(this.abs().toString());
        BigInt summand2 = new BigInt(secondNum.abs().toString());
        int len1 = summand1.num.size();
        int len2 = summand2.num.size();
// тут добавляются незначащие нули в числе меньшей длины, для удобства дальнейших действий
        if (len1 > len2) {
            for (int i = 0; i < len1 - len2; i++) {
                summand2.num.add(0, 0);
            }
        } else if (len1 < len2) {
            for (int i = 0; i < len2 - len1; i++) {
                summand1.num.add(0, 0);
            }
        }
        len1 = summand1.num.size();
        len2 = summand2.num.size();
        ArrayList<Integer> digits = new ArrayList<>();
        for (int i = Math.max(len1 - 1, len2 - 1); i >= 0; i--) {
            digits.add(0, (summand1.num.get(i) + summand2.num.get(i) + transfer) % 10);
            transfer = (summand1.num.get(i) + summand2.num.get(i) + transfer) / 10;
        }
        if (transfer > 0) {
            digits.add(0, transfer);
        }
        String resultString = "";
        for (int i = 0; i < digits.size(); i++) {
            resultString = resultString + digits.get(i);
        }
        if (this.sign && secondNum.sign) {
            return new BigInt(resultString);
        } else if (!this.sign && !secondNum.sign) {
            return new BigInt("-" + resultString);
        }
        return this;
    }

    public BigInt subtract(BigInt secondNum) { // вычитание за линию, по модулю, если разных знаков, то сразу запустится сложение
        int transfer = 0;
        if (this.sign &&
                !secondNum.sign) {
            return this.add(secondNum.abs());
        } else if (!this.sign && secondNum.sign) {
            return new BigInt("-" + this.abs().add(secondNum).toString());
        }
        BigInt minuend = new BigInt(this.abs().toString());
        BigInt deductible = new BigInt(secondNum.abs().toString());

        int len1 = minuend.num.size();
        int len2 = deductible.num.size();
// тут добавляются незначащие нули в числе меньшей длины, для удобства дальнейших действий
        if (len1 > len2) {
            for (int i = 0; i < len1 - len2; i++) {
                deductible.num.add(0, 0);
            }
        } else if (len1 < len2) {
            for (int i = 0; i < len2 - len1; i++) {
                minuend.num.add(0, 0);
            }
        }
        String resultString = "";
        len1 = minuend.num.size();
        len2 = deductible.num.size();
        ArrayList<Integer> res = new ArrayList<>();
        if (minuend.compareTo(deductible) == 1) {
            for (int i = len1 - 1; i >= 0; i--) {
                if (minuend.num.get(i) + transfer >= deductible.num.get(i)) {
                    res.add(0, minuend.num.get(i) - deductible.num.get(i) + transfer);
                    transfer = 0;
                } else {
                    res.add(0, minuend.num.get(i) + 10 - deductible.num.get(i) + transfer);
                    transfer = -1;
                }
            }
        } else if (minuend.compareTo(deductible) == -1) {
            for (int i = len2 - 1; i >= 0; i--) {
                if (deductible.num.get(i) + transfer >= minuend.num.get(i)) {
                    res.add(0, deductible.num.get(i) - minuend.num.get(i) + transfer);
                    transfer = 0;
                } else {
                    res.add(0, deductible.num.get(i) + 10 - minuend.num.get(i) + transfer);
                    transfer = -1;
                }
            }
        } else {
            resultString = "0";
        }
// удаление незначащих нулей
        while (res.size() > 1 && res.get(0) == 0) {
            res.remove(0);
        }
        if (!resultString.equals("0")) {
            for (int i = 0; i < res.size(); i++) {
                resultString = resultString + res.get(i);
            }
        }
        if (this.sign && secondNum.sign) {
            if (minuend.compareTo(deductible) >= 0) {
                return new BigInt(resultString);
            } else {
                return new BigInt("-" + resultString);
            }
        } else if (!this.sign && !secondNum.sign) {
            if (minuend.compareTo(deductible) <= 0) {
                return new BigInt(resultString);
            } else {
                return new BigInt("-" + resultString);
            }
        }
        return new BigInt(resultString);
    }

    public BigInt multiply(BigInt secondNum) { // общее умножение делается за O(n*m), где n и m длины чисел
        BigInt mult1 = new BigInt(this.abs().toString());
        BigInt mult2 = new BigInt(secondNum.abs().toString());
// если хотя бы 1 ноль - то вернуть ноль
        if (mult2.toString().equals("0") || mult1.toString().equals("0")) {
            return new BigInt("0");
        }
        int len1 = mult1.num.size();
        int len2 = mult2.num.size();


        BigInt ans = new BigInt("0");
        for (int i = len2 - 1; i >= 0; i--) {
            int temp = 0;
            ArrayList<Integer> res = new ArrayList<>();
            for (int j = len1 - 1; j >= 0; j--) {
                res.add(0, (mult1.num.get(j) * mult2.num.get(i) + temp) % 10);
                temp = (mult1.num.get(j) * mult2.num.get(i) + temp) / 10;
            }
            if (temp > 0) {
                res.add(0, temp);
            }
            for (int j = i; j < len2 - 1; j++) {
                res.add(0);
            }
            String resultString = "";
            for (int j = 0; j < res.size(); j++) {
                resultString = resultString + res.get(j);
            }
            ans = ans.add(new BigInt(resultString));
        }
        if (this.sign == secondNum.sign) {
            return ans;
        } else {
            return new BigInt("-" + ans.toString());
        }

    }

    public BigInt divide(BigInt secondNum) { // деление
        BigInt divisible = new BigInt(this.abs().toString());
        BigInt divider = new BigInt(secondNum.abs().toString());

        int len1 = divisible.num.size();
        int len2 = divider.num.size();
        if (divider.toString().equals("0") || divisible.compareTo(divider) == -1 || len1 < len2) {
            return new BigInt("0");
        } else if (divisible.abs().compareTo(divider.abs()) == 0) {
            if (this.sign == secondNum.sign) {
                return new BigInt("1");
            } else {
                return new BigInt("-1");
            }
        } else {
            ArrayList<Integer> ans = new ArrayList<>();
            ArrayList<Integer> reminder = new ArrayList<>();

            for (int i = 0; i < len1; i++) {
                reminder.add(divisible.num.get(i));
                String temp = "";
                for (int j = 0; j < reminder.size(); j++) { // линия перевод в строку для создания нового BigInt
                    temp = temp +
                            reminder.get(j);
                }
                BigInt tmpNum = new BigInt(temp); // линия
                int div = 0;

                while (tmpNum.compareTo(divider) >= 0) { // сранение за O(n), не более, чем 9 раз
                    tmpNum = tmpNum.subtract(divider); //вычитание за O(n), выполняется параллельно со сравнением, макс. ассимптотика этого while O(n)+O(n)=O(n)
                    div++;
                }
                ans.add(div);
                reminder = tmpNum.num;
            } // итоговая ассимптотика этого цикла o(n^2)
            String resultString = "";
            for (int i = 0; i < ans.size(); i++) {
                resultString = resultString + ans.get(i);
            }
            if (this.sign == secondNum.sign) {
                return new BigInt(resultString);
            } else {
                return new BigInt("-" + resultString);
            }
        }
    }

}