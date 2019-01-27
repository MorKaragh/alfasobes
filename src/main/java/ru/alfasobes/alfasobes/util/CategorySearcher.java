package ru.alfasobes.alfasobes.util;

import ru.alfasobes.alfasobes.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategorySearcher {


    public static List<Question> search(String searchString, Iterable<Question> all) {
        String[] criteria = searchString.split(" ");
        List<String> except = new ArrayList<>();
        List<String> contain = new ArrayList<>();
        for (String s : criteria) {
            if (s.startsWith("-")) {
                except.add(s.substring(1));
            } else {
                contain.add(s);
            }
        }
        if (except.isEmpty() && contain.isEmpty()) {
            return null;
        }

        List<Question> selected = new ArrayList<>();
        quest:
        for (Question q : all) {
            List<String> cats = Arrays.asList(q.getCategories().split(" "));
            for (String e : except) {
                if (cats.contains(e)) {
                    continue quest;
                }
            }
            for (String e : contain) {
                if (!cats.contains(e)) {
                    continue quest;
                }
            }
            selected.add(q);
        }
        return selected;
    }

}
