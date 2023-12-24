package com.cyolo.words.frequency.evaluator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WordsStatistic {
    private List<WordFrequency> top5Words;
    private Integer least;
    private Integer median;
}
