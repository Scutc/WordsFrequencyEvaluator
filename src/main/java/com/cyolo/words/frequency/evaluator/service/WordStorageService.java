package com.cyolo.words.frequency.evaluator.service;

import java.util.List;

import com.cyolo.words.frequency.evaluator.model.WordFrequency;

public interface WordStorageService {
    void addWords(List<String> words);

    List<WordFrequency> getWordFrequenciesSortedDesc();
}
