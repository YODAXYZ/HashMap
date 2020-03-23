package Project;

import java.util.*;
import java.util.HashMap;

public class ReallyNewHashMap<K,V> extends AbstractMap<K,V> {



    private HashMapper[] table;
    private final int INITIAL_CAPASITY = 75;
    private int capacity;
    private double loadfactor;
    private double treshold;
    private int lenght;

    /**
     * inizial constructor
     */
    public ReallyNewHashMap() {
        loadfactor = 0.75;
        capacity = INITIAL_CAPASITY;
        table = new HashMapper[INITIAL_CAPASITY];
        treshold = INITIAL_CAPASITY * loadfactor;

    }

    /**
     *Add map to HashMap
     * @param map your Map
     */
    public ReallyNewHashMap(Map<K, V> map) {
        map.forEach(this::insert);
    }

    /**
     * Add a hashMap
     * @param hashMap your hashMap
     */
    public ReallyNewHashMap(HashMap<K, V> hashMap) {
        hashMap.forEach(this::insert);
    }

    /**
     * insert key value into HashMap
     * @param key Object
     * @param value Object
     */

    public void insert(K key,V value){
        int hs =hash(key.hashCode()); // вычисляем хэш-код значения в таблице
        int idx = indexFor(key.hashCode(),capacity); //вычисляем индекс Ноды в таблице
        HashMapper e = new HashMapper(hs,key,value,null); // создаем ноду
        if(table[idx] == null){
            table[idx] = e;
            lenght+=1;
            refresh();
        }else{
            HashMapper temp  = table[idx];
            while(temp!=null){
                if(e.hash== temp.hash &&(e.key == temp.key||(e.key).equals(temp.key) )){ /// ситуация если ключи не совпадают а хэши совпадают обработана здесь
                    temp.value = e.value;

                    return;
                }
                if(temp.next==null){
                    break;
                }
                temp = temp.next;
            }
            temp.next = e;
            refresh();
        }
    }

    /**
     * return counts
     * Returns the number of elements where the difference between the key length and the value is less than one
     * @return int count of these elements
     */
    public int intLenNum() {
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                while (true) {
                    String hash = "" + table[i].hash;
                    String value = "" + table[i].value;
                    if (hash.length() == value.length() || hash.length()-1 == value.length() || hash.length()+1 == value.length()) {
                        count++;
                    }
                    if (table[i].next == null) {
                        break;
                    }
                    table[i] = table[i].next;
                }
            }
        }
        return count;
    }

    /**
     *  this method return set of values
     * @return set
     */
    public Set<V> uniqSet(){
        Set set = new TreeSet();
        for(int i = 0;i<capacity;i++){
            if(table[i]!=null){
                HashMapper temp = table[i];
                while(temp!=null){
                    set.add(temp.value);
                    if(temp.next ==null){
                        continue;
                    }
                    temp = temp.next;
                }
            }
        }
        return set;
    }

    /**
     * find value of key
     * @param key Object
     * @return value
     */
    public V get(Object key){
        K keym = (K) key;
        int hs = hash(keym.hashCode());
        int idx = indexFor(keym.hashCode(),capacity);
        HashMapper temp = table[idx];
        while(temp!=null){
            if (temp.key.equals(keym)&&temp.hash ==hs){
                return (V) temp.value;
            }
            if(temp.next!=null) {
                temp = temp.next;
            }
        }
        return null;
    }

    /**
     * delete all elements with this value
     * @param value to delete
     */

    public void delete(V value){
        for(int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                if(table[i].next==null){
                    if(table[i].value.equals((V)value)) {
                        table[i] = null;
                        continue;
                    }

                } else if(table[i].next !=null){
                  while (table[i]!=null && table[i].value.equals(value) ){
                      table[i] = table[i].next;
                      if(table[i].next == null && table[i].value.equals(value)){
                          table[i] = null;
                      }
                  }
                }
                HashMapper temp = table[i];
                while (temp != null && temp.next != null ) {
                        while (  temp.next!=null &&  temp.next.value.equals((V) value)){
                            if(temp.next.next==null){
                                temp.next = null;
                                continue;

                            }
                            temp.next = temp.next.next;
                        }

                   if(temp.next!=null) {

                       temp = temp.next;
                   }




                }



            }
        }
    }

    /**
     * utill function calculates the element index
     * @param h hash of el
     * @param length Arrays lenght
     * @return int index
     */

    private  static int indexFor(int h, int length)   //h = key.hashCode() lenght = capacity
    {
        return h & (length - 1);
    }

    /**
     *
     * @param h hashcode of element
     * @return hash
     */
     private  static int hash(int h)
    {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Show all elements:  key - value
     */
    public void view(){
        for(int i = 0;i<capacity;i++){
            HashMapper temp =table[i];
            while(temp !=null){
                System.out.println("Key = " + temp.key + " " +"Value = " + temp.value);
                if(temp.next==null){
                    break;
                }
                temp =temp.next;
            }

        }
    }

    /**
     * implemented abstract method
     * @return set of Entry
     */

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K,V>> set = new HashSet<>();
        for(int i = 0;i<capacity;i++){
            HashMapper temp = table[i];
            while (temp!=null){
                set.add(temp);
                if(temp.next==null){
                    break;
                }
                temp= temp.next;
            }
        }
        return set;
    }

    /**
     * Expands the array with redistributing all elements
     */
    private void refresh(){
        if(lenght==capacity){
            capacity*=2;
            //HashMapper[] core = new HashMapper[capacity];

            HashMapper[] core = Arrays.copyOf(table,capacity);
            table = new HashMapper[capacity*2];
            for(int i = 0;i<core.length;i++){
                HashMapper temp =core[i];
                while(core[i]!=null){
                    this.insert((K)core[i].key,(V)core[i].value);
                    if(core[i].next!=null) {
                        core[i] = core[i].next;
                    }
                }
            }
        }
    }

    /**
     * Inner Class  what be element in HashMap
     * @param <K> util param
     * @param <V> util param
     */
    private class HashMapper<K, V> implements Map.Entry<K, V> {
          private   int hash;
          private final K key;
          private   V value;
          private HashMapper<K, V> next;

        HashMapper(int hash, K key, V value, HashMapper<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         *
         * @return key
         */
        public final K getKey() {
            return key;
        }

        /**
         *
         * @return value
         */
        public final V getValue() {
            return value;
        }

        /**
         *
         * @return hashcode
         */
        public final int hashCode() {
            hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }

        /**
         * Set abd return  new value
         * @param newValue yourValue
         * @return oldValue that was  on place
         */
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }


    public static void main(String[] args) {
        ReallyNewHashMap<String, String> map = new ReallyNewHashMap<>();
        ArrayList<Long> timeArray = new ArrayList<>();
        long[] mass =new long[300];
       /* map.insert("BENDER8", "GIRONIMK");
        map.insert("BENDER", "GIRONIMK");
        map.insert("BENDERwe", "GIRONIMK");
        map.insert("BENDER1", "GIRONIMK");
        map.insert("BENDER2", "GIRONIMK");
        map.insert("BENDER3", "GIRONIMK");
        map.delete("GIRONIMK");*/

        map.insert("Rem", "Glom");
        map.insert("Rem", "Gloria");
        System.out.println(map.get("Rem"));

      //  map.insert("REVELATION", "Raxer");
      //  map.view();
        //System.out.println(Arrays.toString(map.table));

        for (int j = 0; j < 30; j++) {
            ReallyNewHashMap<String, String> map1 = new ReallyNewHashMap<>();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                String geKey = " " + (Math.random() * 20);
                map1.insert(geKey, "REVELATION");
            }
            long finish = System.currentTimeMillis();
            long timeConsumedMillis = finish - start;
            timeArray.add(timeConsumedMillis);

        }
        for(Long h:timeArray){
            mass[Math.toIntExact(h)] +=1;
        }
        for(int i = 0;i<mass.length;i++){
            if(mass[i]!=0){
                System.out.println(i+" миллисекунд времени"+ " "+mass[i]+" раз");
            }
        }

        System.out.println(timeArray.toString());
        System.out.println(Arrays.toString(mass));

    }



}
