package oplossing;

import opgave.SearchTree;

import java.util.Iterator;
import java.util.LinkedList;

public class TopDownSemiSplayTwoThreeTree <E extends Comparable<E>> implements SearchTree<E> {
    Node<E> root = null;
    int nodeCount = 0;
    @Override
    public int size() {
        return nodeCount;
    }

    @Override
    public boolean isEmpty() {
        return nodeCount == 0;
    }
    @Override
    public boolean contains(E o) {
        //check of gereturnde node null is
        return containsRecursive(o, root) != null;
    }

    @Override
    public boolean add(E newVal) {
        if(root == null){
            root = new Node<>(newVal);
            nodeCount++;
            return true;
        }

        if(contains(newVal)){
            return false;
        }
        nodeCount++;

        Node<E> leaf = findLeaf(root, newVal);
        if(leaf.getKey2() == null){
            if(newVal.compareTo(leaf.getKey1()) == 1){
                leaf.setKeys(leaf.getKey1(), newVal);
            }
            else {
                leaf.setKeys(newVal, leaf.getKey1());
            }
            leaf.rebalanceChildren();
            splay(leaf, newVal);
        }
        else{
            Node<E> newLeaf = new Node<>(newVal);
            leaf.addChild(newLeaf);
            splay(newLeaf, newVal);
        }
        return true;
    }

    @Override
    public boolean remove(E e) {
        //todo
        return true;
    }

    public void splay(Node<E> val, E keyValue){
        if(val.getParent() == null || val.getParent() == root){
            return;
        }

        //it's splay time
        //eerst maak ik een normale splay die geen toppen gaat herverdelen etc
        Node<E> parent = val.getParent();
        Node<E> grandParent = parent.getParent();
        Node<E> greatGrandParent = grandParent.getParent();
        Node<E> newTop = new Node<>(null);
        //splayen default op val 2e keer (als het moet) maar kan anders zijn
        Node<E> nextSplay = val;
        //neem key 1 maar zou normaal nog steeds moeten kloppen
        //stel is kind1
        if(parent.whatChild(val.getKey1()) == 1){
            //checken op welke positie onze parent staat tegenover zijn parent
            //als kind 1
            if(grandParent.whatChild(parent.getKey1()) == 1){
                newTop.setKeys(parent.getKey1(), parent.getKey2());
                newTop.addChild(parent.getChild2());
                grandParent.removeChild(grandParent.getChild1());
                grandParent.addChild(parent.getChild3());
                newTop.addChild(grandParent);
                newTop.addChild(val);
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }

            //als 2e kind
            if(grandParent.whatChild(parent.getKey1()) == 2){
                newTop.setKeys(parent.getKey1(), parent.getKey2());
                newTop.addChild(parent.getChild2());
                Node<E> leftGrandChild = new Node<>(grandParent.getKey1());
                leftGrandChild.addChild(grandParent.getChild1());
                leftGrandChild.addChild(val.getChild1());
                val.addChild(leftGrandChild);
                newTop.addChild(val);
                Node<E> rightChild = new Node<>(grandParent.getKey2());
                rightChild.addChild(parent.getChild3());
                rightChild.addChild(grandParent.getChild3());
                newTop.addChild(rightChild);
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }

            //als 3e kind
            if(grandParent.whatChild(parent.getKey1()) == 3){
                newTop.setKeys(val.getKey1(), val.getKey2());
                newTop.addChild(val.getChild2());
                grandParent.removeChild(grandParent.getChild3());
                grandParent.addChild(val.getChild1());
                newTop.addChild(grandParent);
                parent.removeChild(parent.getChild1());
                parent.addChild(val.getChild3());
                newTop.addChild(parent);
                nextSplay = newTop;
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }
        }
        if(parent.whatChild(val.getKey1()) == 2){

            if(grandParent.whatChild(parent.getKey1()) == 1){
                newTop.setKeys(parent.getKey1(), parent.getKey2());
                grandParent.removeChild(grandParent.getChild1());
                grandParent.addChild(parent.getChild3());
                newTop.addChild(grandParent);
                newTop.addChild(val);
                newTop.addChild(parent.getChild1());
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }
            if(grandParent.whatChild(parent.getKey1()) == 2){
                Node<E> leftChild = new Node<>(grandParent.getKey1());
                Node<E> rightChild = new Node<>(grandParent.getKey2());
                leftChild.addChild(grandParent.getChild1());
                leftChild.addChild(parent.getChild1());
                rightChild.addChild(grandParent.getChild3());
                rightChild.addChild(parent.getChild3());
                parent.addChild(leftChild);
                parent.addChild(rightChild);
                newTop = parent;
                if(greatGrandParent == null){
                    newTop.setParent(null);
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }
            if(grandParent.whatChild(parent.getKey1()) == 3){
                if(parent.getChild1() == null){
                    grandParent.removeChild(grandParent.getChild3());
                    parent.addChild(grandParent);
                    newTop = parent;
                }
                else{
                    newTop.setKeys(parent.getChild1().getKey1(), parent.getChild1().getKey2());
                    newTop.addChild(parent.getChild1().getChild2());
                    grandParent.removeChild(grandParent.getChild3());
                    grandParent.addChild(parent.getChild1().getChild1());
                    newTop.addChild(grandParent);
                    if(parent.getChild1().getChild3() != null) {
                        parent.addChild(parent.getChild1().getChild3());
                    }
                    else {
                        parent.removeChild(parent.getChild1());
                    }
                    newTop.addChild(parent);
                }
                if(greatGrandParent == null){
                    newTop.setParent(null);
                    root = newTop;
                }
                else{
                    greatGrandParent.addChild(newTop);
                }
            }
        }
        if(parent.whatChild(val.getKey1()) == 3){
            if(grandParent.whatChild(parent.getKey1()) == 1){
                newTop.setKeys(val.getKey1(), val.getKey2());
                parent.removeChild(parent.getChild3());
                parent.addChild(val.getChild1());
                newTop.addChild(parent);
                newTop.addChild(val.getChild2());
                grandParent.removeChild(grandParent.getChild1());
                grandParent.addChild(val.getChild3());
                newTop.addChild(grandParent);
                nextSplay = newTop;
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }
            if(grandParent.whatChild(parent.getKey1()) == 2){
                if(parent.getKey2() == null){
                    newTop.setKeys(parent.getKey1(), grandParent.getKey2());
                    newTop.addChild(val);
                    Node<E> leftChild = new Node<>(grandParent.getKey1());
                    leftChild.addChild(grandParent.getChild1());
                    leftChild.addChild(parent.getChild1());
                    newTop.addChild(leftChild);
                    newTop.addChild(grandParent.getChild3());
                }
                else{
                    newTop.setKeys(parent.getKey2(), grandParent.getKey2());
                    newTop.addChild(val);
                    Node<E> leftChild = new Node<>(null);
                    leftChild.setKeys(grandParent.getKey1(), parent.getKey1());
                    leftChild.addChild(parent.getChild1());
                    leftChild.addChild(grandParent.getChild1());
                    leftChild.addChild(parent.getChild2());
                    newTop.addChild(leftChild);
                    newTop.addChild(grandParent.getChild3());
                }
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }
            if(grandParent.whatChild(parent.getKey1()) == 3){
                newTop.setKeys(parent.getKey1(), parent.getKey2());
                newTop.addChild(parent.getChild2());
                grandParent.removeChild(grandParent.getChild3());
                grandParent.addChild(parent.getChild1());
                newTop.addChild(grandParent);
                newTop.addChild(val);
                if(greatGrandParent == null){
                    root = newTop;
                }
                else {
                    greatGrandParent.addChild(newTop);
                }
            }
        }
        //contains checked of de top of zijn kinderen de waarde bevat
        if(root.contains(keyValue)){
            return;
        }
        Node<E> nextStepDown = root.getChild(root.whatChild(keyValue));
        splay(nextStepDown.getChild(nextStepDown.whatChild(keyValue)), keyValue);
    }

    //recursieve methode waar we gewoon van een andere node als zogezegde wortel voort gaan zoeken, returned dan gezochte node
    public Node containsRecursive(E o, Node<E> subRoot){
        if(subRoot == null){
            return null;
        }
        if(o.compareTo(subRoot.getKey1()) == 0|| (subRoot.getKey2() != null && o.compareTo(subRoot.getKey2()) == 0)){
            return subRoot;
        }

        return containsRecursive(o, subRoot.getChild(subRoot.whatChild(o)));
    }

    //recursieve methode, ga knopen af tot hij degene vindt die geen kinderen heeft
    public Node<E> findLeaf(Node<E> node, E newVal){
        int childNr = node.whatChild(newVal);
        if(node.getChild(childNr) == null){
            return node;
        }
        return findLeaf(node.getChild(node.whatChild(newVal)), newVal);
    }


    @Override
    public void clear(){
        root = null;
        nodeCount = 0;
    }

    @Override
    public Iterator iterator() {
        return dfs().iterator();

    }

    public LinkedList<E> dfs(){
        LinkedList<E> list = new LinkedList<>();
        list = recursiveList(root, list);
        return list;
    }

    public LinkedList<E> recursiveList(Node<E> node, LinkedList<E> list)
    {
        if (node == null)
            return list;
        //links recursie
        list = recursiveList(node.getChild1(), list);

        //node toevoegen
        list.add(node.getKey1());

        //evt middenrecursie
        if(node.getKey2() != null){
            list = recursiveList(node.getChild2(), list);
            list.add(node.getKey2());
        }

        //rechtsrecursei
        list = recursiveList(node.getChild3(), list);
        return list;
    }
}
