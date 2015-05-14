package com.trondvalen.prosjektX;

import java.util.ArrayList;
import java.util.List;

public class ElementReverserImpl implements ElementReverser {

    public List<String> work(List<String> list) {
        ArrayList<String> result = new ArrayList<String>();
        for (String string : list) {
            char[] chars = new char[string.length()];
            int charsIndex = 0;
            for (int stringIndex = string.length() - 1; stringIndex >= 0; stringIndex--) {
                chars[charsIndex] = string.charAt(stringIndex);
                charsIndex++;
            }
            result.add(new String(chars));
        }
        return result;
    }

}
