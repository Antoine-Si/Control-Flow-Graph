package main;

import java.util.regex.Pattern;
//matches keywords to find patterns
public class patternMatcher {

    private Pattern MY_PATTERN;

    public boolean isMatch(String pattern,  String testString) {
        MY_PATTERN = Pattern.compile(pattern);
        java.util.regex.Matcher matcher = MY_PATTERN.matcher(testString);
        if (matcher.find())
            return true;
        return false;
    }

}
