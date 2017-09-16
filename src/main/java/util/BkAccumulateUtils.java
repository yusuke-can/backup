package util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import enums.BooleanType;
import enums.ExtensionType;



/**
 * @author Y.Kyan
 *
 * 2016/02/14
 */
public class BkAccumulateUtils {

  public static <V, U> Map<ExtensionType, List<V>> accumulateToExtensionMap(
      final List<V> list, final U u, final BiFunction<V, U, ExtensionType> biAccumulator) {
    return accumulateToEnumMap(list, u, biAccumulator, ExtensionType.class);
  }

  public static <T, V, U> Map<ExtensionType, List<V>> accumulateToEnumMap(
      final List<T> list, final U u, final BiFunction<T, U, ExtensionType> biAccumulator, final Function<T, V> converter) {
    return accumulateToEnumMap(list, u, biAccumulator, converter, ExtensionType.class);
  }

  public static <E extends Enum<E>, V, U> Map<E, List<V>> accumulateToEnumMap(
      final List<V> list, final U u, final BiFunction<V, U, E> biAccumulator, final Class<E> enumClazz) {
    final Map<E, List<V>> accumulatedMap = new EnumMap<>(enumClazz);
    list.stream().forEach(v -> accumulatedMap.computeIfAbsent(biAccumulator.apply(v, u), k -> new ArrayList<V>()).add(v));
    return accumulatedMap;
  }

  public static <E extends Enum<E>, T,V, U> Map<E, List<V>> accumulateToEnumMap(
      final List<T> list, final U u, final BiFunction<T, U, E> biAccumulator, final Function<T,V> converter, final Class<E> enumClazz) {
    final Map<E, List<V>> accumulatedMap = new EnumMap<>(enumClazz);
    list.stream().forEach(t -> accumulatedMap.computeIfAbsent(biAccumulator.apply(t, u), k -> new ArrayList<V>()).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <E extends Enum<E>, V> Map<E, List<V>> accumulateToEnumMap(
      final List<V> list, final Function<V, E> accumulator, final Class<E> enumClazz) {
    final Map<E, List<V>> accumulatedMap = new EnumMap<>(enumClazz);
    list.stream().forEach(v -> accumulatedMap.computeIfAbsent(accumulator.apply(v), k -> new ArrayList<V>()).add(v));
    return accumulatedMap;
  }

  public static <E extends Enum<E>, T,V> Map<E, List<V>> accumulateToEnumMap(
      final List<T> list, final Function<T, E> accumulator, final Function<T,V> converter, final Class<E> enumClazz) {
    final Map<E, List<V>> accumulatedMap = new EnumMap<>(enumClazz);
    list.stream().forEach(t -> accumulatedMap.computeIfAbsent(accumulator.apply(t), k -> new ArrayList<V>()).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <K, V, U> Map<K, List<V>> accumulateToTreeMap(
      final List<V> list, final U u, final BiFunction<V, U, K> BiAccumulator) {
    final Map<K, List<V>> accumulatedMap = new TreeMap<>();
    list.stream().forEach(v -> accumulatedMap.computeIfAbsent(BiAccumulator.apply(v, u), k -> new ArrayList<V>()).add(v));
    return accumulatedMap;
  }

  public static <K, V, T, U> Map<K, List<V>> accumulateToTreeMap(
      final List<T> list, final U u, final BiFunction<T, U, K> BiAccumulator, final Function<T,V> converter) {
    final Map<K, List<V>> accumulatedMap = new TreeMap<>();
    list.stream().forEach(t -> accumulatedMap.computeIfAbsent(BiAccumulator.apply(t, u), k -> new ArrayList<V>()).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <K, V> Map<K, List<V>> accumulateToTreeMap(
      final List<V> list, final Function<V, K> accumulator) {
    final Map<K, List<V>> accumulatedMap = new TreeMap<>();
    list.stream().forEach(v -> accumulatedMap.computeIfAbsent(accumulator.apply(v), k -> new ArrayList<V>()).add(v));
    return accumulatedMap;
  }

  public static <K, T,V> Map<K, List<V>> accumulateToTreeMap(
      final List<T> list, final Function<T, K> accumulator, final Function<T,V> converter) {
    final Map<K, List<V>> accumulatedMap = new TreeMap<>();
    list.stream().forEach(t -> accumulatedMap.computeIfAbsent(accumulator.apply(t), k -> new ArrayList<V>()).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <K, V, U> Map<K, List<V>> accumulateToHashMap(
      final List<V> list, final U u, final BiFunction<V, U, K> BiAccumulator) {
    final Map<K, List<V>> accumulatedMap = new HashMap<>();
    list.stream().forEach(v -> accumulatedMap.computeIfAbsent(BiAccumulator.apply(v, u), k -> new ArrayList<V>()).add(v));
    return accumulatedMap;
  }

  public static <K, V, T, U> Map<K, List<V>> accumulateToHashMap(
      final List<T> list, final U u, final BiFunction<T, U, K> BiAccumulator, final Function<T,V> converter) {
    final Map<K, List<V>> accumulatedMap = new HashMap<>();
    list.stream().forEach(t -> accumulatedMap.computeIfAbsent(BiAccumulator.apply(t, u), k -> new ArrayList<V>()).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <K, V> Map<K, List<V>> accumulateToHashMap(
      final List<V> list, final Function<V, K> accumulator) {
    final Map<K, List<V>> accumulatedMap = new HashMap<>();
    list.stream().forEach(v -> accumulatedMap.computeIfAbsent(accumulator.apply(v), k -> new ArrayList<V>()).add(v));
    return accumulatedMap;
  }

  public static <K, T,V> Map<K, List<V>> accumulateToHashMap(
      final List<T> list, final Function<T, K> accumulator, final Function<T,V> converter) {
    final Map<K, List<V>> accumulatedMap = new HashMap<>();
    list.stream().forEach(t -> accumulatedMap.computeIfAbsent(accumulator.apply(t), k -> new ArrayList<V>()).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <V, U> Map<BooleanType, List<V>> accumulateToBooleanMap(
      final List<V> list, final U u, final BiPredicate<V, U> biPredicate) {
    final Map<BooleanType, List<V>> accumulatedMap = new EnumMap<>(BooleanType.class);
    list.stream().forEach(v -> accumulatedMap.get(BooleanType.valueOf(biPredicate.test(v, u))).add(v));
    return accumulatedMap;
  }

  public static <V, T,U> Map<BooleanType, List<V>> accumulateToBooleanMap(
      final List<T> list, final U u, final BiPredicate<T, U> biPredicate, final Function<T,V> converter) {
    final Map<BooleanType, List<V>> accumulatedMap = new EnumMap<>(BooleanType.class);
    list.stream().forEach(t -> accumulatedMap.get(BooleanType.valueOf(biPredicate.test(t, u))).add(converter.apply(t)));
    return accumulatedMap;
  }

  public static <V> Map<BooleanType, List<V>> accumulateToBooleanMap(final List<V> list, final Predicate<V> predicate) {
    final Map<BooleanType, List<V>> accumulatedMap = new EnumMap<>(BooleanType.class);
    list.stream().forEach(v -> accumulatedMap.get(BooleanType.valueOf(predicate.test(v))).add(v));
    return accumulatedMap;
  }

  public static <V, T> Map<BooleanType, List<V>> accumulateToBooleanMap(
      final List<T> list, final Predicate<T> predicate, final Function<T,V> converter) {
    final Map<BooleanType, List<V>> accumulatedMap = new EnumMap<>(BooleanType.class);
    list.stream().forEach(t -> accumulatedMap.get(BooleanType.valueOf(predicate.test(t))).add(converter.apply(t)));
    return accumulatedMap;
  }
}
