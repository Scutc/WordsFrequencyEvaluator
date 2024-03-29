package com.cyolo.words.frequency.evaluator.service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.cyolo.words.frequency.evaluator.model.WordFrequency;
import jakarta.inject.Singleton;

@Singleton
public class WordStorageServiceImp implements WordStorageService {
    private final ConcurrentMap<String, Integer> wordsStatisticStorage = new ConcurrentHashMap<>();

    @Override
    public void addWords(List<String> words) {
        words.forEach(word -> wordsStatisticStorage.merge(word, 1, Integer::sum));
    }

    @Override
    public List<WordFrequency> getWordFrequenciesSortedDesc() {
        return wordsStatisticStorage.entrySet()
            .stream()
            .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparingInt(WordFrequency::frequency).reversed())
            .collect(Collectors.toList());
    }
}
