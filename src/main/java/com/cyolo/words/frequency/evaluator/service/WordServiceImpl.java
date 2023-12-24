package com.cyolo.words.frequency.evaluator.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cyolo.words.frequency.evaluator.error.BaseException;
import com.cyolo.words.frequency.evaluator.model.WordFrequency;
import com.cyolo.words.frequency.evaluator.model.WordsStatistic;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import static com.cyolo.words.frequency.evaluator.error.WordsFrequencyEvaluatorError.NO_WORDS_PROVIDED;
import static com.cyolo.words.frequency.evaluator.error.WordsFrequencyEvaluatorError.WORDS_NOT_FOUND;

@Singleton
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {
    private static final int TOP_WORDS_NUM = 5;

    private final WordStorageService wordStorageService;

    @Override
    public void addWords(String words) {
        List<String> separatedTrimmedWords = separateAndTrimWords(words);
        if (separatedTrimmedWords.isEmpty()) {
            throw new BaseException(NO_WORDS_PROVIDED);
        }
        wordStorageService.addWords(separatedTrimmedWords);
    }

    @Override
    public WordsStatistic getWordsStatistic() {
        List<WordFrequency> wordFrequenciesSortedDesc = wordStorageService.getWordFrequenciesSortedDesc();
        if (wordFrequenciesSortedDesc.isEmpty()) {
            throw new BaseException(WORDS_NOT_FOUND);
        }
        int numWordsToFetch = Math.min(TOP_WORDS_NUM, wordFrequenciesSortedDesc.size());
        List<WordFrequency> topWords = wordFrequenciesSortedDesc.subList(0, numWordsToFetch);
        int leastWordFrequency = wordFrequenciesSortedDesc.get(wordFrequenciesSortedDesc.size() - 1).frequency();
        int medianWordFrequency = calculateMedianFrequency(wordFrequenciesSortedDesc);
        return new WordsStatistic(topWords, leastWordFrequency, medianWordFrequency);
    }

    private int calculateMedianFrequency(List<WordFrequency> wordFrequencies) {
        int middle = wordFrequencies.size() / 2;
        if (wordFrequencies.size() % 2 == 0) {
            return (wordFrequencies.get(middle - 1).frequency() + wordFrequencies.get(middle).frequency()) / 2;
        } else {
            return wordFrequencies.get(middle).frequency();
        }
    }

    private List<String> separateAndTrimWords(String words) {
        return Arrays.stream(words.split(","))
            .map(String::trim)
            .filter(word -> !word.isBlank())
            .collect(Collectors.toList());
    }
}
