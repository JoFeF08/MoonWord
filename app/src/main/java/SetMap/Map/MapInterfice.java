/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package SetMap.Map;

import java.util.Iterator;

import SetMap.Set.Pair;

/**
 *
 * @author daniel
 */
public interface MapInterfice<K,V> {

    /**
     * 
     * @param key
     * @param value
     * @return valor anterior, o null
     */
    public V put(K key, V value);
    
    /**
     * 
     * @param key
     * @return 
     */
    public V get(K key);
    
    /**
     * 
     * @param key
     * @return valor asociat, o nulll
     */
    public V remove(K key);
    
    /**
     * 
     * @return 
     */
    public boolean isEmpty();

    /**
     *
     */
    public Iterator<Pair<K,V>> iterator();

}
