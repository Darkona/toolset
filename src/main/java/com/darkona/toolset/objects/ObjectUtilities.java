package com.darkona.toolset.objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class ObjectUtilities {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Pattern ArrayPattern = Pattern.compile("(\\w+)\\[(\\d+)]");

    private ObjectUtilities() {}

    public static <T> Map<String, String> dehydrate(T object) {
        Map<String, Object> dehydrated = mapper.convertValue(object, new TypeReference<>() {
        });
        return recursiveDehydration(dehydrated, null);
    }

    private static Map<String, String> recursiveDehydration(Map<String, Object> map, String parentKey) {
        Map<String, String> dehydrated = new HashMap<>();

        for (var entry : map.entrySet()) {
            String key = parentKey == null ? entry.getKey() : parentKey + "." + entry.getKey();
            Object value = entry.getValue();
            switch (value) {
                case Map<?, ?> mapValue:
                    dehydrated.putAll(recursiveDehydration((Map<String, Object>) mapValue, key));
                    break;
                case Iterable<?> iterableValue:
                    var i = 0;
                    for (var item : iterableValue) {
                        var indexedKey = "[" + i + "]";
                        if (item instanceof Map<?, ?> colMap) {
                            dehydrated.putAll(recursiveDehydration((Map<String, Object>) colMap, key + indexedKey));
                        } else {
                            dehydrated.put(key + indexedKey, item.toString());
                        }
                        i++;
                    }
                    break;
                default:
                    dehydrated.put(key, value.toString());
            }
        }
        return dehydrated;
    }

    public static <T> T rehydrate(Map<String, String> dehydrated, Class<T> clazz) {
        Map<String, Object> rehydrated = new HashMap<>();
        for (var entry : dehydrated.entrySet()) {
            hydrate(rehydrated, entry.getKey().split("\\."), entry.getValue());
        }
        return mapper.convertValue(rehydrated, clazz);
    }

    private static void hydrate(Map<String, Object> rehydrated, String[] keys, String value) {
        Map<String, Object> current = rehydrated;
        for (int i = 0; i < keys.length; i++) {
            var key = keys[i];
            Matcher match = ArrayPattern.matcher(key);
            if (match.matches()) {
                var arrayKey = match.group(1);
                var index = Integer.parseInt(match.group(2));
                current = hydrateList(keys, value, i, current, arrayKey, index);
            } else {
                current = hydrateObject(keys, value, i, current, key);
            }
        }
    }

    private static Map<String, Object> hydrateObject(String[] keys, String value, int i, Map<String, Object> current, String key) {
        if (i == keys.length - 1) {
            current.put(key, value);
        } else {
            current.computeIfAbsent(key, k -> new HashMap<String, Object>());
            current = (Map<String, Object>) current.get(key);
        }
        return current;
    }

    private static Map<String, Object> hydrateList(String[] keys, String value, int i, Map<String, Object> current, String arrayKey, int index) {
        if (i == keys.length - 1) {
            current.computeIfAbsent(arrayKey, k -> new HashMap<>());
            List<Object> list = (List<Object>) current.get(arrayKey);
            ensureListSize(list, index);
            list.set(index, value);
        } else {
            var list = (List<Object>) current.computeIfAbsent(arrayKey, k -> new ArrayList<>());
            ensureListSize(list, index);
            if (list.get(index) == null) {
                list.set(index, new HashMap<String, Object>());
            }
            current = (Map<String, Object>) list.get(index);
        }
        return current;
    }

    private static void ensureListSize(List<Object> list, int index) {
        while (list.size() <= index) {
            list.add(new HashMap<String, Object>());
        }
    }

    public String describe(Object o){
        return o != null ? o.toString() : "null";
    }

}
