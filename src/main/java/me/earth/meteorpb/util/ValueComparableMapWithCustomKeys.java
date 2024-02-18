package me.earth.meteorpb.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @see meteordevelopment.meteorclient.utils.misc.ValueComparableMap
 */
public class ValueComparableMapWithCustomKeys<K, V> extends TreeMap<K, V> {
    private final transient Map<K, V> valueMap;

    public ValueComparableMapWithCustomKeys(Comparator<? super V> partialValueComparator, Comparator<? super K> keyComparator) {
        this(partialValueComparator, keyComparator, new HashMap<>());
    }

    private ValueComparableMapWithCustomKeys(Comparator<? super V> partialValueComparator, Comparator<? super K> keyComparator, HashMap<K, V> valueMap) {
        super((k1, k2) -> {
            int cmp = partialValueComparator.compare(valueMap.get(k1), valueMap.get(k2));
            return cmp != 0 ? cmp : keyComparator.compare(k1, k2);
        });

        this.valueMap = valueMap;
    }

    @Override
    public V put(K k, V v) {
        if (valueMap.containsKey(k)) remove(k);
        valueMap.put(k, v);
        return super.put(k, v);
    }

    @Override
    public boolean containsKey(Object key) {
        return valueMap.containsKey(key);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }
}
