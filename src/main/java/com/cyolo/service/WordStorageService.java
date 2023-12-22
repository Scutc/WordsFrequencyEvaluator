package com.cyolo.service;

import java.util.List;

import com.cyolo.model.WordFrequency;

public interface WordStorageService {
    void addWords(List<String> words);

    List<WordFrequency> getWordFrequenciesSortedDesc();
}
