package utils;

import domini.Document;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Joan Vazquez
 * És una classe per a ordenar un Map per valor
 */

public class SortedValue {

    /**
     * Funció per a ordenar un Map per valor
     * @param vec_ordenar map a ordenar per valor
     * @return map ordenat per valor
     */
    public static Map<Document, Double> sortByValue(final HashMap<Document, Double> vec_ordenar){
        return vec_ordenar.entrySet()
                .stream()
                .sorted(((Map.Entry. <Document, Double>comparingByValue().reversed())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
