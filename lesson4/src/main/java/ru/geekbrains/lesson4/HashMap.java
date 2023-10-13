package ru.geekbrains.lesson4;

import java.util.Iterator;

/**
 * Хэш-таблица
 *
 * @param <K>
 * @param <V>
 */
public class HashMap<K, V> implements Iterable<HashMap.Entity> {

    private static final int INIT_BUCKET_COUNT = 16;
    private static final double LOAD_FACTOR = 0.5;

    private Bucket<K, V>[] buckets;
    private int size; // Кол-во элементов

    /**
     * Конструктор без параметров
     */
    public HashMap() {
        buckets = new Bucket[INIT_BUCKET_COUNT];
    }

    /**
     * Конструктор с заданным начальным размером
     *
     * @param capacity начальный размер
     */
    public HashMap(int capacity) {
        buckets = new Bucket[capacity];
    }

    /**
     * Добавление нового элемента в хэш-таблицу
     *
     * @param key   ключ
     * @param value значение
     * @return
     */
    public V put(K key, V value) {
        if (buckets.length * LOAD_FACTOR <= size) {
            recalculate();
        }
        int index = calculateBucketIndex(key);
        Bucket<K, V> bucket = buckets[index];
        if (bucket == null) {
            bucket = new Bucket<>();
            buckets[index] = bucket;
        }

        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;

        V buf = bucket.add(entity);
        if (buf == null) {
            size++;
        }
        return buf;
    }

    /**
     * Поиск значения в хэш-таблице по ключу
     *
     * @param key ключ
     * @return значение
     */
    public V get(K key) {
        int index = calculateBucketIndex(key);
        Bucket<K, V> bucket = buckets[index];
        if (bucket == null)
            return null;
        return bucket.get(key);
    }

    /**
     * Удаление элемента из хэш-таблицы по ключу
     *
     * @param key ключ
     * @return значение
     */
    public V remove(K key) {
        int index = calculateBucketIndex(key);
        Bucket<K, V> bucket = buckets[index];
        if (bucket == null)
            return null;
        V buf = bucket.remove(key);
        if (buf != null) {
            size--;
        }
        return buf;
    }

    /**
     * TODO: В минимальном варианте, распечатать все элементы хэш-таблицы
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Bucket<K, V> bucket : buckets) {
            if (bucket != null) {
                Bucket.Node node = bucket.head;
                while (node != null) {
                    result.append(node.value.key).append(" : ").append(node.value.value).append("\n");
                    node = node.next;
                }
            }
        }
        return result.toString();
    }

    private void recalculate() {
        size = 0;
        Bucket<K, V>[] old = buckets;
        buckets = new Bucket[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Bucket<K, V> bucket = old[i];
            if (bucket != null) {
                Bucket.Node node = bucket.head;
                while (node != null) {
                    put(node.value.key, node.value.value);
                    node = node.next;
                }
            }
        }
    }

    private int calculateBucketIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    @Override
    public Iterator<Entity> iterator() {
        return new HashMapIterator();
    }

    class HashMapIterator implements Iterator<Entity> {

        // Реализация методов hasNext() и next() для итератора
        // ...

        @Override
        public boolean hasNext() {
            // ваша реализация
            return false;
        }

        @Override
        public Entity next() {
            // ваша реализация
            return null;
        }
    }

    /**
     * Элемент хэш-таблицы
     */
    class Entity {
        K key;
        V value;
    }

    /**
     * Связный список
     *
     * @param <K>
     * @param <V>
     */
    class Bucket<K, V> {

        private Node head;

        class Node {
            Node next;
            Entity value;
        }

        public V add(Entity entity) {
            Node node = new Node();
            node.value = entity;

            if (head == null) {
                head = node;
                return null;
            }

            Node currentNode = head;
            while (true) {
                if (currentNode.value.key.equals(entity.key)) {
                    V buf = currentNode.value.value;
                    currentNode.value.value = entity.value;
                    return buf;
                }
                if (currentNode.next != null) {
                    currentNode = currentNode.next;
                } else {
                    currentNode.next = node;
                    return null;
                }
            }
        }

        public V get(K key) {
            Node node = head;
            while (node != null) {
                if (node.value.key.equals(key))
                    return node.value.value;
                node = node.next;
            }
            return null;
        }

        public V remove(K key) {
            if (head == null)
                return null;
            if (head.value.key.equals(key)) {
                V buf = head.value.value;
                head = head.next;
                return buf;
            } else {
                Node node = head;
                while (node.next != null) {
                    if (node.next.value.key.equals(key)) {
                        V buf = node.next.value.value;
                        node.next = node.next.next;
                        return buf;
                    }
                    node = node.next;
                }
                return null;
            }
        }
    }
}
