package com.cyolo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.cyolo.model.WordFrequency;
import jakarta.inject.Singleton;

@Singleton
public class WordStorageServiceImp implements WordStorageService {
    private final ConcurrentMap<String, Integer> wordStatisticsStorage = new ConcurrentHashMap<>();

    @Override
    public void addWords(List<String> words) {
        for (String word : words) {
            wordStatisticsStorage.merge(word, 1, Integer::sum);
        }
    }

    @Override
    public List<WordFrequency> getWordFrequenciesSortedDesc() {
        List<WordFrequency> wordFrequencies = new ArrayList<>();
        wordStatisticsStorage.forEach((key, value) -> wordFrequencies.add(new WordFrequency(key, value)));
        wordFrequencies.sort((t1, t2) -> t2.frequency().compareTo(t1.frequency()));
        return wordFrequencies;
    }
}
