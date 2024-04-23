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
public class UnsortedLinkedListSet<E> implements SetInterficie<E> {
    
    private Node<E> first;
    
    public UnsortedLinkedListSet(){
        first=null;
    }

    @Override
    public boolean add(E e) {
        if(contains(e)){
            return false;
        }
        Node<E> n = new Node(e, first);
        first=n;
        return true;
    }

    @Override
    public boolean contains(E e) {
        Node<E> i = first;
        while(i!=null){
            if(i.getVal().equals(e)){
                return true;
            }
            i=i.getNext();
        }
        return false;
    }

    @Override
    public boolean remove(E e) {
        Node<E> i = first;
        Node<E> aux = null;
        while(i!=null){
            if(i.getVal().equals(e)){
                if(aux==null){
                    first=i.getNext();
                }else{
                    aux.setNext(i.getNext());
                }
                return true;
            }
            aux=i;
            i=i.getNext();
        }
        
        return false;
    }

    @Override
    public boolean isEmpty() {
        return first==null;
    }

    
    @Override
    public Iterator<E> iterator() {
        return new LinkedListSetIterator<E>();   
    }

    
    private class LinkedListSetIterator<E> implements Iterator {

        private Node<E> i;
        private LinkedListSetIterator(){
            i=(Node<E>)first;
        }
        
        @Override
        public boolean hasNext() {
            return i!=null;
        }

        @Override
        public E next() {
            Node<E> n = i;
            i=i.getNext();
            return n.getVal();
        }

    }
}
