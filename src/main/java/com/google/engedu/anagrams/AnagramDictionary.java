package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private int wordLength= DEFAULT_WORD_LENGTH;

    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();

    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordList.add(word);
            wordSet.add(word);

            ArrayList<String> tempWords1;

            String alphabeticalOrder = alphabeticalOrder(word);
            if(lettersToWord.containsKey(alphabeticalOrder))
            {
                tempWords1 = lettersToWord.get(alphabeticalOrder);
            }
            else
            {
                tempWords1 = new ArrayList<>();
            }
            tempWords1.add(word);
            lettersToWord.put(alphabeticalOrder,tempWords1);

            ArrayList<String> tempWords2;
            int length=word.length();

            if(sizeToWord.containsKey(length))
            {
                tempWords2 = sizeToWord.get(length);
            }
            else
            {
                tempWords2=new ArrayList<>();
            }
            tempWords2.add(word);
            sizeToWord.put(length,tempWords2);
        }
    }

    public String alphabeticalOrder(String word)
    {
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        String finalWord = new String(charArray);
        return finalWord;
    }
    public boolean isGoodWord(String word, String base) {

        if(wordSet.contains(word) && !word.contains(base))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for (char ch='a'; ch <='z'; ch++)
        {
            String alphaOrder = alphabeticalOrder(word + ch);

            //ArrayList<String> tempList;
            if (lettersToWord.containsKey(alphaOrder))
            {
                ArrayList<String> tempList = lettersToWord.get(alphaOrder);
                result.addAll(tempList);
            }
        }

        for (int i = result.size()-1; i >=0 ; i--)
        {
            String currentWord= result.get(i);

            if(!isGoodWord(currentWord, word))
            {
                result.remove(i);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        ArrayList<String> possibleWords = new ArrayList<>();

        if(wordLength <= MAX_WORD_LENGTH)
        {
            possibleWords=sizeToWord.get(wordLength);
        }

        int j;
        int randomValue= random.nextInt(possibleWords.size());
        String returnWord=null;

        for (j=randomValue; j<possibleWords.size(); j++)
        {
            String currentWord = possibleWords.get(j);

            if(getAnagramsWithOneMoreLetter(currentWord).size() >= 5)
            {
                returnWord=currentWord;
                break;
            }
        }

        if(returnWord == null && j==possibleWords.size())
        {
            for (int i=0; i<j; i++ )
            {
                String currentWord = possibleWords.get(j);

                if(getAnagramsWithOneMoreLetter(currentWord).size() >= 5)
                {
                    returnWord=currentWord;
                    break;
                }
            }
        }

        if(wordLength < MAX_WORD_LENGTH)
        {
            wordLength++;
        }

        return returnWord;
    }
}