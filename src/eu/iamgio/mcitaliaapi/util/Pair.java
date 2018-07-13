package eu.iamgio.mcitaliaapi.util;

/**
 * Represents a pair of two objects
 * @author Gio
 */
public class Pair<K, V> {

    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "[" + first + ", " + second + "]";
    }

    /**
     * @return First member
     */
    public K getFirst() {
        return first;
    }

    /**
     * @return Second member
     */
    public V getSecond() {
        return second;
    }
}
