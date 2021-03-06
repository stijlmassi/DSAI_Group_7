package com.example.application.services.chatbot.spellcheck;

import java.util.*;

public class SpellCheck {

    private static Trie trie = new Trie();
    private static Map<String, Integer> dict = new HashMap<>();

    public static void init(List<String> dictionary){
        for(String line: dictionary){
            String word = line.toLowerCase();
            if (!line.contains(" ")) {
                dict.put(word, dict.getOrDefault(word, 0)+1);
                trie.add(word);
            } else {
                String[] strs= line.split("\\s");
                for(String str: strs) {
                    dict.put(str, dict.getOrDefault(str, 0)+1);
                    trie.add(str);
                }
            }
        }
    }
    
    public static String bestMatch(String inputWord) {
        String s = inputWord.toLowerCase();
        String res;
        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
        TrieNode node = trie.find(s);
        if(node == null) {
            for (String w: dict.keySet()) {
                int dist = editDistance(w, s);
                TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
                int freq = dict.get(w);
                TreeSet<String> set = similarWords.getOrDefault(freq, new TreeSet<>());
                set.add(w);
                similarWords.put(freq, set);
                map.put(dist, similarWords);
            }
            res = map.firstEntry().getValue().lastEntry().getValue().first();
        } else {
            res = s;
        }
        return res;
    }

    private static int editDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][] dp = new int[n+1][m+1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;
                else if (word1.charAt(i-1) == word2.charAt(j-1))
                    dp[i][j] = dp[i-1][j-1];
                else if (i>1 && j>1  && word1.charAt(i-1) == word2.charAt(j-2)
                        && word1.charAt(i-2) == word2.charAt(j-1))
                    dp[i][j] = 1+Math.min(Math.min(dp[i-2][j-2], dp[i-1][j]), Math.min(dp[i][j-1], dp[i-1][j-1]));
                else
                    dp[i][j] = 1 + Math.min(dp[i][j-1], Math.min(dp[i-1][j], dp[i-1][j-1]));
            }
        }
        return dp[n][m];
    }
}
