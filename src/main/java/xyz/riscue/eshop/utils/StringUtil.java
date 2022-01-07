package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import name.fraser.neil.plaintext.DiffMatchPatch;

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
}
