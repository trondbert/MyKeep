package moderate.closest_number;

import com.sun.deploy.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {


    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int tasksCount = Integer.parseInt(br.readLine());
            for (int i = 0; i < tasksCount; i++) {
                String[] strings = StringUtils.splitString(br.readLine(), " ");
                int a = Integer.parseInt(strings[0]);
                int b = Integer.parseInt(strings[0]);
                int x = Integer.parseInt(strings[0]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
