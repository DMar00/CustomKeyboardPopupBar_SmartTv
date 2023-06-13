package dama.controllers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SuggestionsController {
    public final static int BASE = 28;
    public final static int NUM_SUG = 8;
    private String fileString;

    public SuggestionsController(Context context) {
        readFileFromAssets(context);
    }

    //read assets - getApplicationContext() as parameter in KeyboardImeService
    private void readFileFromAssets(Context context) {
        List<String> resultList = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("result.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                resultList.add(line);
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileString = resultList.get(0);
    }

    private char numToChar(int n) {
        if (n >= 0 && n <= 25)
            return (char) (n + 'a');
        else if (n == 26)
            return ' ';
        else
            return '*';
    }

    private int charToNum(char c) {
        if (c == ' ')
            return 26;
        else if (c >= 'a' && c <= 'z')
            return c - 'a';
        else
            return 27;
    }

    private int sequenceToNum(String sequence) {
        int num = 0;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            int n = charToNum(c);
            num += n * Math.pow(BASE, i);
        }
        return num;
    }

    private String numToSequence(int num, int sequenceLength) {
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < sequenceLength; i++) {
            int n = num % BASE;
            char c = numToChar(n);
            sequence.append(c);
            num /= BASE;
        }

        return sequence.toString();
    }

    private String getSuggestions(String context, String suggestionData) {
        //todo problem if i write symbols
        int sequenceNumber = sequenceToNum(context);
        return suggestionData.substring(sequenceNumber*NUM_SUG,(sequenceNumber*NUM_SUG)+NUM_SUG);
    }

    public char[] generateSuggestions(String lastLetters){
        char suggestions[] = new char[NUM_SUG];
        String suggestionsString = getSuggestions(lastLetters, fileString);
        for (int i=0; i<NUM_SUG ; i++){
            suggestions[i] = suggestionsString.charAt(i);
        }
        return suggestions;
    }
}
