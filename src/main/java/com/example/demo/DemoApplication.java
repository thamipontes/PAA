package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);

//        Path inputPath = Paths.get("src/main/resources/names.txt");
//
//        Path outputPath = Paths.get("src/main/resources/output.txt");
//
//        String content = Files.readString(inputPath);
//        String content2 = Files.readString(outputPath);
//
//        System.out.println(lock_match(content,content2));
    }

    public static int lock_match(String s, String t) {



        int totalw = word_count(s);
        int total = 100;
        int perw = total / totalw;
        int gotperw = 0;

        if (!s.equals(t)) {

            for (int i = 1; i <= totalw; i++) {
                if (simple_match(split_string(s, i), t) == 1) {
                    gotperw = ((perw * (total - 10)) / total) + gotperw;
                } else if (front_full_match(split_string(s, i), t) == 1) {
                    gotperw = ((perw * (total - 20)) / total) + gotperw;
                } else if (anywhere_match(split_string(s, i), t) == 1) {
                    gotperw = ((perw * (total - 30)) / total) + gotperw;
                } else {
                    gotperw = ((perw * smart_match(split_string(s, i), t)) / total) + gotperw;
                }
            }
        } else {
            gotperw = 100;
        }
        return gotperw;
    }

    public static int anywhere_match(String s, String t) {
        int x = 0;
        if (t.contains(s)) {
            x = 1;
        }
        return x;
    }

    public static int front_full_match(String s, String t) {
        int x = 0;
        String tempt;
        int len = s.length();

        //----------Work Body----------//
        for (int i = 1; i <= word_count(t); i++) {
            tempt = split_string(t, i);
            if (tempt.length() >= s.length()) {
                tempt = tempt.substring(0, len);
                if (s.contains(tempt)) {
                    x = 1;
                    break;
                }
            }
        }
        //---------END---------------//
        if (len == 0) {
            x = 0;
        }
        return x;
    }

    public static int simple_match(String s, String t) {
        int x = 0;
        String tempt;
        int len = s.length();


        //----------Work Body----------//
        for (int i = 1; i <= word_count(t); i++) {
            tempt = split_string(t, i);
            if (tempt.length() == s.length()) {
                if (s.contains(tempt)) {
                    x = 1;
                    break;
                }
            }
        }
        //---------END---------------//
        if (len == 0) {
            x = 0;
        }
        return x;
    }

    public static int smart_match(String ts, String tt) {

        char[] s = new char[ts.length()];
        s = ts.toCharArray();
        char[] t = new char[tt.length()];
        t = tt.toCharArray();


        int slen = s.length;
        //number of 3 combinations per word//
        int combs = (slen - 3) + 1;
        //percentage per combination of 3 characters//
        int ppc = 0;
        if (slen >= 3) {
            ppc = 100 / combs;
        }
        //initialising an integer to store the total % this class genrate//
        int x = 0;
        //declaring a temporary new source char array
        char[] ns = new char[3];
        //check if source char array has more then 3 characters//
        if (slen < 3) {
        } else {
            for (int i = 0; i < combs; i++) {
                for (int j = 0; j < 3; j++) {
                    ns[j] = s[j + i];
                }
                if (cross_full_match(ns, t) == 1) {
                    x = x + 1;
                }
            }
        }
        x = ppc * x;
        return x;
    }

    /**
     *
     * @param s
     * @param t
     * @return
     */
    public static int  cross_full_match(char[] s, char[] t) {
        int z = t.length - s.length;
        int x = 0;
        if (s.length > t.length) {
            return x;
        } else {
            for (int i = 0; i <= z; i++) {
                for (int j = 0; j <= (s.length - 1); j++) {
                    if (s[j] == t[j + i]) {
                        // x=1 if any charecer matches
                        x = 1;
                    } else {
                        // if x=0 mean an character do not matches and loop break out
                        x = 0;
                        break;
                    }
                }
                if (x == 1) {
                    break;
                }
            }
        }
        return x;
    }

    public static String split_string(String s, int n) {

        int index;
        String temp;
        temp = s;
        String temp2 = null;

        int temp3 = 0;

        for (int i = 0; i < n; i++) {
            int strlen = temp.length();
            index = temp.indexOf(" ");
            if (index < 0) {
                index = strlen;
            }
            temp2 = temp.substring(temp3, index);
            temp = temp.substring(index, strlen);
            temp = temp.trim();

        }
        return temp2;
    }

    public static int word_count(String s) {
        int x = 1;
        int c;
        s = s.trim();
        if (s.isEmpty()) {
            x = 0;
        } else {
            if (s.contains(" ")) {
                for (;;) {
                    x++;
                    c = s.indexOf(" ");
                    s = s.substring(c);
                    s = s.trim();
                    if (s.contains(" ")) {
                    } else {
                        break;
                    }
                }
            }
        }
        return x;
    }

//    static int compute_Levenshtein_distanceDP(String str1,
//                                              String str2)
//    {
//
//        // A 2-D matrix to store previously calculated
//        // answers of subproblems in order
//        // to obtain the final
//
//        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
//
//        for (int i = 0; i <= str1.length(); i++)
//        {
//            for (int j = 0; j <= str2.length(); j++) {
//
//                // If str1 is empty, all characters of
//                // str2 are inserted into str1, which is of
//                // the only possible method of conversion
//                // with minimum operations.
//                if (i == 0) {
//                    dp[i][j] = j;
//                }
//
//                // If str2 is empty, all characters of str1
//                // are removed, which is the only possible
//                //  method of conversion with minimum
//                //  operations.
//                else if (j == 0) {
//                    dp[i][j] = i;
//                }
//
//                else {
//                    // find the minimum among three
//                    // operations below
//
//
//                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
//                                    + NumOfReplacement(str1.charAt(i - 1),str2.charAt(j - 1)), // replace
//                            dp[i - 1][j] + 1, // delete
//                            dp[i][j - 1] + 1); // insert
//                }
//            }
//        }
//
//        return dp[str1.length()][str2.length()];
//    }
//
//    // check for distinct characters
//    // in str1 and str2
//
//    static int NumOfReplacement(char c1, char c2)
//    {
//        return c1 == c2 ? 0 : 1;
//    }
//
//    // receives the count of different
//    // operations performed and returns the
//    // minimum value among them.
//
//    static int minm_edits(int... nums)
//    {
//
//        return Arrays.stream(nums).min().orElse(
//                Integer.MAX_VALUE);
//    }

//    public static long filesCompareByByte(Path path1, Path path2) throws IOException {
//        try (BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(path1.toFile()));
//             BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(path2.toFile()))) {
//
//            int ch = 0;
//            long pos = 1;
//            while ((ch = fis1.read()) != -1) {
//                if (ch != fis2.read()) {
//                    return pos;
//                }
//                pos++;
//            }
//            if (fis2.read() == -1) {
//                return -1;
//            }
//            else {
//                return pos;
//            }
//        }
//    }

//    public static long filesCompareByLine(Path path1, Path path2) throws IOException {
//        try (BufferedReader bf1 = Files.newBufferedReader(path1);
//             BufferedReader bf2 = Files.newBufferedReader(path2)) {
//
//            long lineNumber = 1;
//            String line1 = "", line2 = "";
//            while ((line1 = bf1.readLine()) != null) {
//                line2 = bf2.readLine();
//                if (line2 == null || !line1.equals(line2)) {
//                    return lineNumber;
//                }
//                lineNumber++;
//            }
//            if (bf2.readLine() == null) {
//                return -1;
//            }
//            else {
//                return lineNumber;
//            }
//        }
//    }

}
