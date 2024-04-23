/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package SetMap.Set;

import java.util.Iterator;

/**
 *
 * @version 1.0
 * @author daniel
 * @param <E>
 */
public interface SetInterfice<E> {

    /**
     *
     * @param e
     * @return true si canvia
     */
    public boolean add(E e);

    /**
     *
     * @param e
     * @return
     */
    public boolean contains(E e);

    /**
     *
     * @param e
     * @return true si canvia
     */
    public boolean remove(E e);

    /**
     *
     * @return
     */
    public boolean isEmpty();
    
    public Iterator iterator();
}
