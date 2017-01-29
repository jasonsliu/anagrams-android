/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.text.format.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, List<String>> lettersToWord = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sorted = sortLetters(word);
            if(lettersToWord.containsKey(sorted)) {
                List sortedWords = lettersToWord.get(sorted);
                sortedWords.add(word);
            } else {
                ArrayList<String> ltw = new ArrayList<>();
                ltw.add(word);
                lettersToWord.put(sorted, ltw);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word)) {
            return false;
        }

        int i = 0;
        while(i <= (word.length() - base.length())) {
            if(word.substring(i, base.length()).equals(base)) {
                return false;
            }
            i++;
        }

        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(String word : wordSet) {
            if(sortLetters(targetWord).equals(sortLetters(word))) {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(char letter : alphabet) {
            try {
                for(String withExtraLetter : lettersToWord.get(sortLetters(word + letter))) {
                    if(isGoodWord(withExtraLetter, word)) {
                        result.add(withExtraLetter);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        while(true) {
            String word = wordList.get(random.nextInt(wordList.size()));
            if(getAnagramsWithOneMoreLetter(word).size() >= MIN_NUM_ANAGRAMS) {
                return word;
            }
        }
    }

    private String sortLetters(String sortMe) {
        char[] sorted = sortMe.toCharArray();
        Arrays.sort(sorted);
        return new String(sorted);
    }
}
