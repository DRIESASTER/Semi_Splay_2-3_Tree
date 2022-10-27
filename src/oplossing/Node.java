package oplossing;

import java.lang.reflect.Array;

public class Node<E extends Comparable<E>> {
    //private Node<E>[] children = (Node<E>[]) Array.newInstance(this.getClass(), 3);
    //private E[] keys = (E[]) Array.newInstance(Comparable.class, 2);
    //private Comparable<E>[] keys = new Comparable[2];
    private Node<E> child1 = null;
    private Node<E> child2 = null;
    private Node<E> child3 = null;

    private E key1 = null;
    private E key2 = null;


    //node word aangemaakt
    public Node(E value){
        key1 = value;
    }

    public Node<E> getChild1(){
        return child1;
    }
    public Node<E> getChild2(){
        return child2;
    }
    public Node<E> getChild3(){
        return child3;
    }

    public Node<E> getChild(int childNr){
        if(childNr == 1){
            return child1;
        }
        if(childNr == 2){
            return child2;
        }
        if(childNr ==3){
            return child3;
        }
        return null;
    }
    public E getKey1(){
        return key1;
    }

    public E getKey2(){
        return key2;
    }

    private Node<E> parent = null;

    public void clearChildren(){
        child1 = null;
        child2 = null;
        child3 = null;
    }


    //returns amount of empty children
    public int emptyChildren(){
        int counter = 0;
        if(child1 == null){
            counter++;
        }
        if(child2 == null){
            counter++;
        }
        if(child3 == null){
            counter++;
        }
        return counter;
    }

    public int amountOfKeys(){
        int count = 0;
        if(key1 != null){
            count++;
        }
        if(key2 != null){
            count++;
        }
        return count;
    }

    public boolean removeValue(Comparable<E> o){
        if(key1 == o){
            key1 = key2;
            key2 = null;
            return true;
        }
        else if(key2 == o){
            key2 = null;
            return true;
        }
        return false;
    }
    public void rebalanceChildren(){
        Node<E> oldChild1 = child1;
        Node<E> oldChild2 = child2;
        Node<E> oldChild3 = child3;
        clearChildren();
        addChild(oldChild1);
        addChild(oldChild2);
        addChild(oldChild3);
    }

    public void removeChild(Node node){
        if(child1 == node){
            child1 = null;
        }
        if(child2 == node){
            child2 = null;
        }
        if(child3 == node){
            child3 = null;
        }
    }

    //naar welk kind(index) zou dit value moeten gaan (gebaseerd op waarde t.o.v. keys) -> kleiner dan key 1 -> links, tussen de twee -> midden etc...
    public int whatChild(E value){
        if(key1.compareTo(value) == 1){
            return 1;
        }
        else if(key2 == null || key2.compareTo(value) == -1){
            return 3;
        }
        return 2;
    }

    public boolean addChild(Node<E> child){
        //optie voor als we aan een lege node toevoegen
        if(key1 == null && key2 == null){
            child1 = child;
            return true;
        }
        if(child == null){
            return false;
        }
        child.setParent(this);
        int index = whatChild(child.getKey1());
        if(key1.compareTo(child.getKey1()) == 1){
            child1 = child;
            return true;
        }
        if(key2 == null || key2.compareTo(child.getKey1()) == -1){
            child3 = child;
            return true;
        }
        else{
            child2 = child;
            return true;
        }
    }
    //mag enkel met node met maar 1 key, wordt niet getest, specifiek gebruikt om binaire boom toe te voegen aan 1 sleutel ouder
    public boolean addNode(Node<E> node){
        if(node == null){
            return false;
        }
        if(addKey(node.getKey1())){
            //organisatie van kinderen bepalen, 2 scenarios: ofwel is teogevoege node kleiner ofwel groter
            if(key1 == node.getKey1()){
                this.addChild(node.getChild1());
                this.addChild(node.getChild3());
            }
            else{
                this.addChild(node.getChild1());
                this.addChild(node.getChild3());
            }
            return true;
        }
        return false;
    }

    public void setParent(Node<E> parent){
        this.parent = parent;
    }

    public Node<E> getParent(){
        return parent;
    }

    public void setKeys(E key1, E key2){
        this.key1 = key1;
        this.key2 = key2;
    }

    public void emptyKeys(){
        key1 = null;
        key2 = null;
    }

    public boolean addKey(E key){
        if(key2 == null){
            if(key.compareTo(key1) == 1){
                key2 = key;
            }
            else{
                key2 = key1;
                key1 = key;
            }
            return true;
        }
        return false;
    }
}