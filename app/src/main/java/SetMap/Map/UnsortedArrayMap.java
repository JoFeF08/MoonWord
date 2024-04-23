package SetMap.Map;

import java.util.Arrays;
import java.util.Iterator;

import SetMap.Set.Pair;
import kotlin.NotImplementedError;

import java.lang.reflect.Array;

public class UnsortedArrayMap<K, V> implements  MapInterfice<K,V>{

    private K keys[];
    private V vals[];

    private int n;

    public UnsortedArrayMap(int maxSize){
        this.keys = (K[]) new Object[maxSize];
        this.vals = (V[]) new Object[maxSize];
        n=0;
    }

    /**
     * @param key
     * @param value
     * @return valor anterior, o null
     */
    @Override
    public V put(K key, V value) {
        if(n==0){
            n++;
            keys[0]=key;
            vals[0]=value;
            return null;
        }
        for(int i=0;i<keys.length;i++){
            if(keys[i]==null){
                n++;
                keys[i]=key;
                vals[i]=value;
                return null;
            }
            if(keys[i].equals(key)){
                V old = vals[i];
                vals[i]=value;
                return old;
            }
        }
        return null;
    }

    /**
     * @param key
     * @return
     */
    @Override
    public V get(K key) {
        for (int i = 0; i < n; i++) {
            if(keys[i].equals(key)){
                return vals[i];
            }
        }
        return null;
    }

    /**
     * @param key
     * @return valor asociat, o nulll
     */
    @Override
    public V remove(K key) {
        throw new NotImplementedError();
    }

    /**
     * @return
     */
    @Override
    public boolean isEmpty() {
        return n==0;
    }

    /**
     *
     */
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new ArrayMapIterator<Pair<K,V>>();
    }

    private class ArrayMapIterator<P> implements Iterator{

        int i;
        public ArrayMapIterator(){
            this.i=0;
        }
        @Override
        public boolean hasNext() {
            return i<n;
        }

        @Override
        public Pair<K,V> next() {
            Pair<K,V> pair= new Pair<>(keys[i], vals[i]);
            i++;
            return pair;
        }
    }
    @Override
    public String toString(){
        return Arrays.toString(keys)+"\n"+Arrays.toString(vals);
    }


}
