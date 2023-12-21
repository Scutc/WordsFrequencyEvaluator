package com.cyolo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cyolo.error.WordsFrequencyEvaluatorException.WORDS_NOT_FOUND;

import com.cyolo.error.BaseException;
import jakarta.inject.Singleton;

@Singleton
public class WordServiceImpl implements WordService {
    private final ConcurrentMap<String, Integer> wordsCount = new ConcurrentHashMap<>();
    private final PriorityQueue<Map.Entry<String, Integer>> topWords = new PriorityQueue<>(
        Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
    private final AtomicInteger minWordFrequency = new AtomicInteger();
    private final AtomicInteger totalWordsCount = new AtomicInteger();


    @Override
    public void addWord(String inputWords) {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
        String[] words = inputWords.split(",");
        for (String word : words) {
            wordsCount.merge(word, 1, Integer::sum);
        }
    }

    @Override
    public String getWordStatistics() {
        if (wordsCount.isEmpty()) {
            throw new BaseException(WORDS_NOT_FOUND);
        }
        List<Map.Entry<String, Integer>> sortedWordEntities = new ArrayList<>(wordsCount.entrySet());
        sortedWordEntities.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        int totalWordsCount = sortedWordEntities.size();
        int leastWordFrequency = sortedWordEntities.get(sortedWordEntities.size() - 1).getValue();
        int medianWordFrequency = calculateMedianFrequency(sortedWordEntities);
        return String.format("%s %s %s", totalWordsCount, leastWordFrequency, medianWordFrequency);
    }

    private int calculateMedianFrequency(List<Map.Entry<String, Integer>> sortedEntries) {
        int middle = wordsCount.size() / 2;
        if (sortedEntries.size() % 2 == 0) {
            return (sortedEntries.get(middle - 1).getValue() + sortedEntries.get(middle).getValue()) / 2;
        } else {
            return sortedEntries.get(middle).getValue();
        }
    }
}
