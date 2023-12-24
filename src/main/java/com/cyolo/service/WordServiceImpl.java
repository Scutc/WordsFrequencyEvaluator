package com.cyolo.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cyolo.error.BaseException;
import com.cyolo.model.WordFrequency;
import com.cyolo.model.WordsStatistic;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import static com.cyolo.error.WordsFrequencyEvaluatorError.NO_WORDS_PROVIDED;
import static com.cyolo.error.WordsFrequencyEvaluatorError.WORDS_NOT_FOUND;

@Singleton
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private final WordStorageService wordStorageService;

    @Override
    public void addWords(String inputWords) {
        List<String> separatedTrimmedWords = splitAndTrimWords(inputWords);
        wordStorageService.addWords(separatedTrimmedWords);
    }

    @Override
    public WordsStatistic getWordsStatistic() {
        List<WordFrequency> wordFrequenciesSortedDesc = wordStorageService.getWordFrequenciesSortedDesc();

        if (wordFrequenciesSortedDesc.isEmpty()) {
            throw new BaseException(WORDS_NOT_FOUND);
        }
        int numWordsToFetch = Math.min(5, wordFrequenciesSortedDesc.size());
        List<WordFrequency> top5Words = wordFrequenciesSortedDesc.subList(0, numWordsToFetch);
        int leastWordFrequency = wordFrequenciesSortedDesc.get(wordFrequenciesSortedDesc.size() - 1).frequency();
        int medianWordFrequency = calculateMedianFrequency(wordFrequenciesSortedDesc);
        return new WordsStatistic(top5Words, leastWordFrequency, medianWordFrequency);
    }

    private int calculateMedianFrequency(List<WordFrequency> wordFrequencies) {
        int middle = wordFrequencies.size() / 2;
        if (wordFrequencies.size() % 2 == 0) {
            return (wordFrequencies.get(middle - 1).frequency() + wordFrequencies.get(middle).frequency()) / 2;
        } else {
            return wordFrequencies.get(middle).frequency();
        }
    }

    private List<String> splitAndTrimWords(String words) {
        if (words.isBlank()) {
            throw new BaseException(NO_WORDS_PROVIDED);
        }
        return Arrays.stream(words.split(","))
            .map(String::trim)
            .collect(Collectors.toList());
    }
}
