package Utils;

@FunctionalInterface
public interface TriPredicate<S,U,V> {
    boolean test(S s, U u, V v);
}
