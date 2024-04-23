/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SetMap.Set;

/**
 *
 * @author daniel
 */
public class Node<E> {

    private E val;
    private Node<E> next;
    
    public Node(E val, Node<E> next){
        this.val = val;
        this.next = next;
    }

    public E getVal() {
        return val;
    }

    public void setVal(E val) {
        this.val = val;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" + "val=" + val + '}';
    }
    
    

}
