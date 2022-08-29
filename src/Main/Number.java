package Main;

public class Number {
    public static Double parseDouble(String s) {
        if (s==null || s.equals(""))
            throw new RuntimeException("Main.parseDouble: Empty string");

        int sign = 1;
        double res = 0.0;

        int i = 0;
        char[] arr = s.toCharArray();
        if (arr[0] == '-') {
            i++;
            sign = -1;
        }

        for (; i < s.length() && arr[i]!=',' && arr[i]!='.'; i++) {
            if (Character.isDigit(arr[i]))
                res = res*10 + ((int)(arr[i]) - '0');
        }

        if (i == s.length())
            return res*sign;

        if (arr[i]==',' || arr[i]=='.')
            i++;
        else
            throw new RuntimeException("Main.parseDouble: Wrong separator character");

        double pow = 1.0;
        for (int j = i; j < s.length(); j++) {
            if (Character.isDigit(arr[j])) {
                pow *= 10;
                res += ((int)(arr[j]) - '0')/pow;
            }
        }

        return res*sign;
    }
}
