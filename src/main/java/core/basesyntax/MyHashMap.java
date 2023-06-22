package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold = 12;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode;
        int hashcode = 0;

        if (key == null) {
            newNode = new Node<>(0, null, value, null);
        } else {
            hashcode = key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode();
            newNode = new Node<>(hashcode, key, value, null);
        }

        int position = hashcode % table.length;
        if (table[position] == null) {
            table[position] = newNode;
            ++size;
            return;
        }
        if (table[position].checkIfExists(newNode)) {
            return;
        }

        table[position].add(newNode);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = 0;
        if (key != null) {
            int hashcode = key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode();
            position = hashcode % table.length;
        }

        Node<K, V> currNode = table[position];
        while (currNode != null) {
            if (Objects.equals(currNode.key, key)) {
                return currNode.value;
            }
            currNode = currNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        if (size + 1 < threshold) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);

        for (Node<K, V> kvNode : oldTable) {
            for (Node<K, V> currNode = kvNode; currNode != null; currNode = currNode.next) {
                Node<K, V> copyOfNode =
                        new Node<>(currNode.hashcode, currNode.key, currNode.value, null);
                int position = currNode.hashcode % table.length;

                if (table[position] == null) {
                    table[position] = copyOfNode;
                } else {
                    table[position].add(copyOfNode);
                }
            }
        }
    }

    private class Node<K, V> {
        private int hashcode;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hashcode, K key, V value, Node<K, V> next) {
            this.hashcode = hashcode;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public boolean checkIfExists(Node<K, V> newNode) {
            Node<K, V> currNode = this;

            while (currNode != null) {
                if (Objects.equals(newNode.key, currNode.key)) {
                    currNode.value = newNode.value;
                    return true;
                }
                currNode = currNode.next;
            }
            return false;
        }

        public void add(Node<K, V> newNode) {
            Node<K, V> currNode = this;

            while (currNode.next != null) {
                currNode = currNode.next;
            }

            currNode.next = newNode;
        }
    }
}
