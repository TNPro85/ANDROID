package com.zing.demo.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static CharSequence cutEmojiString(CharSequence emojiChars, int index) {
        try {
            long cur = System.currentTimeMillis();
            String emoji = emojiChars.toString();

            if (index < 0 || index > emoji.length())
                throw new IllegalStateException("Index out of bounds!");

            int range = 5;
            int startSplit = index - range < 0 ? 0 : index - range;
            int endSplit = index + range > emoji.length() ? emoji.length() : index + range;
            // spilt sub string of emoji
            String subEmoji = emoji.substring(startSplit, endSplit);

            byte[] utf8Bytes = subEmoji.getBytes("UTF-8");
            String utf8Emoji = new String(utf8Bytes, "UTF-8");

            Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8Emoji);

            List<String> matchList = new ArrayList<String>();

            while (unicodeOutlierMatcher.find()) {
                matchList.add(unicodeOutlierMatcher.group());
            }

            ArrayList<Entry> listEntry = new ArrayList<>();

            int fromIndex = 0;
            for (int i = 0; i < matchList.size(); i++) {
                String temp = matchList.get(i);
                int start = subEmoji.indexOf(temp, fromIndex);
                int end = start + temp.length();
                fromIndex = end;
                listEntry.add(new Entry(start, end));
            }

            int indexHandled = getIndex(listEntry, index - startSplit);
            CharSequence result = emojiChars.subSequence(0, startSplit + indexHandled);
            Log.i("Execute Time: ", System.currentTimeMillis() - cur + "");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static int getIndex(List<Entry> list, int index) {
        for (Entry entry : list) {
            if (index <= entry.start)
                break;
            else if (index < entry.end) {
                index = entry.start;
                break;
            }
        }
        return index;
    }

    static class Entry {
        int start;
        int end;

        public Entry(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
