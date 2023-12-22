package com.cyolo.mapper;

import com.cyolo.model.WordsStatistic;
import jakarta.inject.Singleton;

@Singleton
public class WordMapperImpl implements WordMapper {
    @Override
    public String fromWordsStatistic(WordsStatistic wordsStatistic) {
        StringBuilder result = new StringBuilder();
        result.append("top5:");
        wordsStatistic.getTop5Words().forEach(wordFrequency ->
            result.append(String.format("\n%s %d", wordFrequency.word(), wordFrequency.frequency())));
        result.append(String.format("\n\nleast: %d", wordsStatistic.getLeast()));
        result.append(String.format("\n\nmedian: %d", wordsStatistic.getMedian()));
        return result.toString();
    }
}
