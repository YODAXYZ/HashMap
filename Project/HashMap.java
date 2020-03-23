package Project;
//fewffffffffffffffffffffffffffff
//wefxXc
//ewfwe/
import java.util.*;

public class HashMap<K, V> extends AbstractMap {
    private int capacity = 75;
    private Entry[] table;
    private int length;

    public HashMap(int capacity) {
        this.capacity = capacity;
        this.length = 1;
        table = new HashMapper[capacity];
    }
    public HashMap(){
        this.length = 1;
        table = new HashMapper[capacity];
    }

    public HashMap(Map<K, V> map) {
        this.length = 1;
        table = new HashMapper[capacity];
    }

    public HashMap(int capacity, Entry[] table) {
        this.capacity = capacity;
        this.table = table;
        this.length = 1;
    }
    public int hash(final K key){

        int hash = 37;
        hash = hash *17 + key.hashCode();
        return hash % table.length;
    }
    private int hash(Entry<K,V> node){
        return node.hashCode() % table.length;
    }
    @Override
    public Set<Entry> entrySet() {
        return null;
    }

    static int indexFor(int h, int length) {
        return h & (length - 1);
    }
    private void expansionMap(){
        table =Arrays.copyOf(table,table.length *2);
        capacity *=2;
    }

    public void insert(K key, V value) {
//        if (key == null) {
//            throw new NullPointerException();
//        }

        int index  = indexFor(key.hashCode(), length);

        if (length==capacity){
            expansionMap();
        }

        if (table[index] != null) {
            table[index] = new HashMapper(key.hashCode(), key, value, (HashMapper) table[index]);
        }
        else {
            table[index] = new HashMapper(key.hashCode(), key, value, null);
        }
        capacity--;
    }
    private class MyIterator implements Iterator<Entry<K,V>>{
        int cursor = 0;


        @Override
        public boolean hasNext() {
            return length < table.length-1;
        }

        @Override
        public Entry<K, V> next() {
            return null;
        }
    }

    public class HashMapper<K, V> implements Entry<K, V> {
        int hash;
        final K key;
        V value;
        HashMapper<K,V> next;

        HashMapper(int hash, K key, V value, HashMapper<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }


        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
  //      public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            hash = 31;
            hash = hash *17 + key.hashCode();
            hash = hash * 17 + value.hashCode();
            return hash;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    public void sout() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
               // System.out.println("EEWQ");
            }
            else {
                System.out.println(table[i].getKey());
                System.out.println(table[i].getValue());
            }
        }
    }

    public static void main(String[] args) {
        HashMap<String,String> rem = new HashMap<String, String>();
        rem.insert("Sand","Origin");
        rem.insert("Sand","rewers");
        rem.insert("Sand","Orqer");
        rem.insert("REVELATION","RERGE");
        rem.sout();

    }

}
