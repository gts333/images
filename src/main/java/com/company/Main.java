package com.company;


import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(400);
        arrayList.add(10);
        arrayList.add(1);
        arrayList.add(110);
        arrayList.add(1110);
        arrayList.add(101);
        arrayList.add(1011);
        arrayList.add(10111);
        arrayList.add(1001);
        Optional<Integer> optionalInteger = Optional.ofNullable(Integer.valueOf(4));
        Optional<String> optional = Optional.empty();
        String s = optional.orElseGet(() -> "");
        Comparator<List<Integer>> comparator = Comparator.comparingInt(l1 -> l1.get(0));


    }


}

