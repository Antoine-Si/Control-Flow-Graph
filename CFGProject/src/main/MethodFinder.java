package main;

import java.util.ArrayList;
import java.util.regex.Pattern;



public class MethodFinder {

    boolean functionNameNotFound = false;
    boolean nextLineIsNeeded = false;
    ArrayList<ArrayList<Statement>> allMethods = new ArrayList<>();
    //Checks for keywords and follows code logic.
    public ArrayList<ArrayList<Statement>> findMethods(ArrayList<Statement> allCodeLines) {

        for (int i = 0; i < allCodeLines.size(); i++) {
            String nextLine = null;
            String currentLine = allCodeLines.get(i).getStatement();

            if (i < allCodeLines.size() - 1) {
                nextLine = allCodeLines.get(i + 1).getStatement();
            }

            if (isMethodStartingLine(currentLine, nextLine)) {
                if (nextLineIsNeeded && !currentLine.endsWith("{")) {
                    i++;
                    nextLineIsNeeded = false;
                    functionNameNotFound = false;
                }

                i++;

                ArrayList<Statement> currentMethod = new ArrayList<>();

                boolean nextMethodFound = false;
                while (!nextMethodFound) {
                    String tempCurrentLine = null;
                    String tempNextLine = null;
                    if (i < allCodeLines.size())
                        tempCurrentLine = allCodeLines.get(i).getStatement();
                    if (i < allCodeLines.size() - 1)
                        tempNextLine = allCodeLines.get(i + 1).getStatement();
                    if (isMethodStartingLine(tempCurrentLine, tempNextLine)) {
                        i--;
                        nextMethodFound = true;
                        break;

                    }

                    if (i >= allCodeLines.size()) {

                        break;
                    }

                    currentMethod.add(allCodeLines.get(i));

                    i++;

                }

                currentMethod.remove(currentMethod.size() - 1);

                allMethods.add(currentMethod);

            }
        }

        allMethods.get(allMethods.size() - 1).remove(allMethods.get(allMethods.size() - 1).size() - 1);
        return allMethods;

    }
    //Finds when a statement is starting
    public boolean isMethodStartingLine(String currentLine, String nextLine) {
        if (currentLine == null)
            return false;
        String allWards[] = currentLine.split(" ");
        boolean isClassDeclaration = checkClassDeclaration(allWards);
        boolean numberOfWordCheck = checkNumberOfWords(allWards);
        boolean bracketFound = checkBracket(currentLine);
        boolean endWithSemicolon = checkSemicolon(currentLine);
        boolean keyWordFound = checkKeyWords(currentLine);
        boolean parenthesisFound = false;

        if (nextLine == null) {
            if (currentLine.endsWith("{"))
                parenthesisFound = true;
        } else {
            if (currentLine.endsWith("{"))
                parenthesisFound = true;
            Pattern MY_PATTERN = Pattern.compile("\\{");
            java.util.regex.Matcher matcher = MY_PATTERN.matcher(nextLine);

            if (!parenthesisFound) {
                functionNameNotFound = true;
            }

            if (matcher.find()) {
                parenthesisFound = true;
                if (functionNameNotFound) {
                    nextLineIsNeeded = true;
                }
            }
        }

        if (!isClassDeclaration && !keyWordFound && numberOfWordCheck && bracketFound && !endWithSemicolon
                && parenthesisFound)
            return true;

        return false;
    }
    //checks for certain words to do the right methods for each statement.
    private boolean checkKeyWords(String currentLine) {
        Pattern MY_PATTERN = Pattern.compile("for");
        java.util.regex.Matcher matcher = MY_PATTERN.matcher(currentLine);
        if (matcher.find())
            return true;
        MY_PATTERN = Pattern.compile("while");
        matcher = MY_PATTERN.matcher(currentLine);
        if (matcher.find())
            return true;
        MY_PATTERN = Pattern.compile("if");
        matcher = MY_PATTERN.matcher(currentLine);
        if (matcher.find())
            return true;
        MY_PATTERN = Pattern.compile("}");
        matcher = MY_PATTERN.matcher(currentLine);
        if (matcher.find())
            return true;
        return false;
    }

    private boolean checkSemicolon(String currentLine) {
        if (currentLine.endsWith(";"))
            return true;
        return false;
    }

    private boolean checkBracket(String currentLine) {
        boolean startingBracket = false, endingBracket = false;
        for (int i = 0; i < currentLine.length(); i++) {
            if (currentLine.charAt(i) == '(')
                startingBracket = true;
            if (currentLine.charAt(i) == ')')
                endingBracket = true;
        }

        if (startingBracket && endingBracket)
            return true;
        return false;
    }

    private boolean checkNumberOfWords(String[] allWards) {
        if (allWards.length >= 2)
            return true;
        return false;
    }

    private boolean checkClassDeclaration(String[] allWards) {
        for (int i = 0; i < allWards.length; i++) {
            if (allWards[i].equals("class"))
                return true;
        }
        return false;
    }

}