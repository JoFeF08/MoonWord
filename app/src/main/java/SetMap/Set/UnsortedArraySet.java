/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SetMap.Set;

import java.util.Iterator;

/**
 *
 * @author daniel
 */
public class UnsortedArraySet<E> implements SetInterfice<E> {

    private E[] arr;
    private int n;

    public UnsortedArraySet(int size) {
        arr = (E[]) new Object[size];
        n = 0;
    }

    @Override
    public boolean add(E e) {
        if (n >= arr.length || contains(e)) {
            return false;
        }

        arr[n++] = e;
        return true;
    }

    @Override
    public boolean contains(E e) {
        for (int i = 0; i < n; i++) {
            if (arr[i].equals(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(E e) {
        if (n <= 0) {
            return false;
        }
        int i;
        boolean t = false;
        for (i = 0; i < n && !t; i++) {
            if (arr[i].equals(e)) {
                t = true;
            }
        }
        i--;
        if (t) {
            arr[i] = arr[--n];
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public Iterator iterator() {
        return new ArraySetIterator<E>();   
    }

    private class ArraySetIterator<E> implements Iterator {

        private int i;
        private ArraySetIterator(){
            i=0;
        }
        
        @Override
        public boolean hasNext() {
            return i<n;
        }

        @Override
        public E next() {
            return (E) arr[i++];
        }

    }

}
