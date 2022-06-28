package Main;

public class Number {
    public static Double parseDouble(String s) {
        if (s==null || s.equals(""))
            throw new RuntimeException("Main.parseDouble: Empty string");

        double res = 0.0;

        int i;
        char[] arr = s.toCharArray();
        for (i = 0; i < s.length() && arr[i]!=',' && arr[i]!='.'; i++) {
            if (Character.isDigit(arr[i]))
                res = res*10 + ((int)(arr[i]) - '0');
        }

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

        return res;
    }
}
