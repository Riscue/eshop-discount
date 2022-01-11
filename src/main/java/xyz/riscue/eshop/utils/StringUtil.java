package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import name.fraser.neil.plaintext.DiffMatchPatch;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class StringUtil {

    public static boolean isEqualEnough(String str1, String str2) {
        int diffSum = 0;
        List<DiffMatchPatch.Diff> diffs = new DiffMatchPatch().diff_main(str1, str2);
        for (DiffMatchPatch.Diff diff : diffs) {
            if (diff.operation != DiffMatchPatch.Operation.EQUAL) {
                diffSum += diff.text.length();
            }
        }

        return diffSum < 5;
    }

    public static boolean notEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static String ifPresentOrDefault(String value, String defaulValue) {
        return value != null && !value.trim().isEmpty() ? value : defaulValue;
    }

    public static String capitalize(String str) {
        if (!StringUtils.isEmpty(str)) {
            str = str.toLowerCase();
            char[] buffer = str.toCharArray();
            boolean capitalizeNext = true;

            for (int i = 0; i < buffer.length; ++i) {
                char ch = buffer[i];
                if (Character.isWhitespace(ch)) {
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer[i] = Character.toTitleCase(ch);
                    capitalizeNext = false;
                }
            }

            return new String(buffer);
        } else {
            return str;
        }
    }

}
