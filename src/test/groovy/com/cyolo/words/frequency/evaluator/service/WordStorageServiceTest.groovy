package com.cyolo.words.frequency.evaluator.service

import com.cyolo.words.frequency.evaluator.model.WordFrequency
import com.cyolo.words.frequency.evaluator.service.WordStorageService
import com.cyolo.words.frequency.evaluator.service.WordStorageServiceImp
import spock.lang.Specification

class WordStorageServiceTest extends Specification {
    WordStorageService service = new WordStorageServiceImp()

    def "addWords should add words to storage"() {
        given:
        List<String> wordsToAdd = ["apple", "banana", "apple"]

        when:
        service.addWords(wordsToAdd)

        then:
        service.getWordFrequenciesSortedDesc() == [
                new WordFrequency("apple", 2),
                new WordFrequency("banana", 1)
        ]
    }

    def "getWordFrequenciesSortedDesc should return frequencies in descending order"() {
        given:
        service.addWords(["apple", "banana", "apple", "cherry", "banana", "banana"])

        when:
        List<WordFrequency> frequencies = service.getWordFrequenciesSortedDesc()

        then:
        frequencies.size() == 3
        frequencies[0].word() == "banana"
        frequencies[0].frequency() == 3
        frequencies[1].word() == "apple"
        frequencies[1].frequency() == 2
        frequencies[2].word() == "cherry"
        frequencies[2].frequency() == 1
    }

    def "getWordFrequenciesSortedDesc should return an empty list if no words added"() {
        when:
        List<WordFrequency> frequencies = service.getWordFrequenciesSortedDesc()

        then:
        frequencies.isEmpty()
    }
}
