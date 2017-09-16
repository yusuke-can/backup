package util;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * @author Y.Kyan
 *
 * 2016/02/14
 */
public class BkEnumUtils {

  /*
   * functional Interfaceの実装がわからなくなった時用に残す
   */
//public static <E extends Enum<E>> E getEnumType(final String enumStr,final Class<E> clazz) {
//  if(Strings.isNullOrEmpty(enumStr)) {
//    return null;
//  }
//  for (final E en : EnumSet.allOf(clazz)) {
//    if (en.toString().equals(enumStr)) {
//      return en;
//    }
//  }
//  return null;
//}

  public static <E extends Enum<E>> E getTypeIgnoreCase(final String enumStr,final Class<E> clazz) {
    return getEnumType(enumStr, clazz, (enStr,en) -> en.toString().equalsIgnoreCase(enStr));
  }
  public static <E extends Enum<E>> E getType(final String enumStr, final Class<E> clazz) {
    return getEnumType(enumStr, clazz, (enStr,en) -> en.toString().equals(enStr));
  }
  public static <E extends Enum<E>, T> E getEnumType(final T t, final Class<E> clazz, final BiPredicate<T, E> predicateEnumType) {
    return getEnumTypeOrDefault(t, clazz, predicateEnumType, null);
  }

  public static <E extends Enum<E>, T> E getEnumTypeOrDefault(
      final T t,
      final Class<E> clazz,
      final BiPredicate<T, E> predicateEnumType,
      final E defaultEnumType) {
    return Optional.ofNullable(t)
        .flatMap(tv -> EnumSet.allOf(clazz).stream().filter(en -> predicateEnumType.test(tv, en)).findFirst())
        .orElse(defaultEnumType);
  }

}
