package com.tungbt.apigateway.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class ComparatorUtility {

    public static Comparator<Map<String, String>> mapComparator() {
        return Comparator.comparing(m -> m.keySet().stream().collect(Collectors.joining()));
    }

}
