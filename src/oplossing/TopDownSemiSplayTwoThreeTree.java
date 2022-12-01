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
            root = new Node<E>(newVal);
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
            splay(root, newVal);
        }
        else{
            Node<E> newLeaf = new Node<E>(newVal);
            leaf.addChild(newLeaf);
            splay(root, newVal);
        }
        return true;
    }

    public boolean remove(E e) {
        Node<E> node = containsRecursive(e, root);

        if(node == null){
            return false;
        }

        E toSplay = null;

        nodeCount--;

        Boolean rootCheck = node == root;

        //als het een leaf is kunnen we de waarde er gewoon uithalen
        if(node.getChild1() == null && node.getChild2() == null && node.getChild3() == null){
            if(node.getKey2() == null){
                if(rootCheck){
                    root = null;
                    return true;
                }
                node.getParent().removeChild(node);
            }
            node.removeValue(e);
            if(rootCheck){
                return true;
            }
            splay(root, node.getParent().getKey1());
            return true;
        }

        if(node.getKey2() == null){
            if (node.getChild1() == null){
                if (node == root){
                    root = node.getChild3();
                    root.setParent(null);
                } else {
                    node.getParent().addChild(node.getChild3());
                }
            } else if (node.getChild3() == null){
                if (node == root){
                    root = node.getChild1();
                    root.setParent(null);
                } else {
                    node.getParent().addChild(node.getChild1());
                }
            } else {
                //heeft 2 kinderen
                //we vervangen hier de verwijderde top door de 1e sleutel van het kleinste rechterblad
                Node<E> smallestRightNode = smallestRightChild (node);
                node.setKeys(smallestRightNode.getKey1(), null);
                if (smallestRightNode.getKey2() == null){
                    smallestRightNode.getParent().removeChild(smallestRightNode);
                    smallestRightNode.getParent().addChild(smallestRightNode.getChild3());
                    splay(root, smallestRightNode.getParent().getKey1());
                } else {
                    smallestRightNode.setKeys(smallestRightNode.getKey2(), null);
                    smallestRightNode.rebalanceChildren();
                    splay(root, smallestRightNode.getParent().getKey1());
                }
                return true;
            }
        }
        else {
            int amountOfChildren = node.getAmountOfChildren();
            Node<E> child1 = node.getChild1();
            Node<E> child2 = node.getChild2();
            Node<E> child3 = node.getChild3();
            if (amountOfChildren == 2){
                if (e.equals(node.getKey1())){
                    if(node.getChild3() == null){
                        Node<E> smallesMiddleChild = smallestRightChildRecursive(child2);
                        node.setKeys(smallesMiddleChild.getKey1(), node.getKey2());
                        if(smallesMiddleChild.getKey2() == null){
                            smallesMiddleChild.getParent().removeChild(smallesMiddleChild);
                            smallesMiddleChild.getParent().addChild(smallesMiddleChild.getChild3());
                        }
                        else{
                            smallesMiddleChild.setKeys(smallesMiddleChild.getKey2(), null);
                            smallesMiddleChild.rebalanceChildren();
                        }
                    }

                    else {
                        node.setKeys(node.getKey2(), null);
                        node.rebalanceChildren();
                    }
                } else {
                    if(node.getChild1() == null){
                        Node<E> largestMiddleChild = largestLeftChildRecursive(child2);
                        if(largestMiddleChild.getKey2() == null){
                            node.setKeys(node.getKey1(), largestMiddleChild.getKey1());
                            largestMiddleChild.getParent().removeChild(largestMiddleChild);
                            largestMiddleChild.getParent().addChild(largestMiddleChild.getChild1());
                        }
                        else{
                            node.setKeys(node.getKey1(), largestMiddleChild.getKey2());
                            largestMiddleChild.setKeys(largestMiddleChild.getKey1(), null);
                            largestMiddleChild.rebalanceChildren();
                        }
                    }
                    else{
                        node.setKeys(node.getKey1(), null);
                        node.rebalanceChildren();
                    }
                }
                return true;
            } else if (amountOfChildren == 1){
                if (e.equals(node.getKey1())){
                    node.setKeys(node.getKey2(), null);
                } else {
                    node.setKeys(node.getKey1(), null);
                }
                node.rebalanceChildren();
            } else {
                Node<E> replacementNode = null;
                Node<E> extraChild = null;
                if(e.equals(node.getKey1())){
                    replacementNode = largestLeftChild(node);
                    extraChild = replacementNode.getChild1();
                }
                else{
                    replacementNode = smallestRightChild(node);
                    extraChild = replacementNode.getChild3();
                }
                if (replacementNode.getKey2() == null){
                    if(e.equals(node.getKey1())) {
                        node.setKeys(replacementNode.getKey1(), node.getKey2());
                    }
                    else {
                        node.setKeys(node.getKey1(), replacementNode.getKey1());
                    }
                    replacementNode.getParent().removeChild(replacementNode);
                    replacementNode.getParent().addChild(extraChild);
                } else {
                    if(e.equals(node.getKey1())) {
                        node.setKeys(replacementNode.getKey2(), node.getKey2());
                        replacementNode.setKeys(replacementNode.getKey1(), null);
                        replacementNode.rebalanceChildren();
                    }
                    else {
                        node.setKeys(node.getKey1(), replacementNode.getKey1());
                        replacementNode.setKeys(replacementNode.getKey2(), null);
                        replacementNode.rebalanceChildren();
                    }
                }
                splay(root, replacementNode.getParent().getKey1());
                return true;
            }
        }
        if (!rootCheck){
            splay(root, node.getKey1());
        }
        return true;
    }


    public Node<E> smallestRightChild (Node<E> node) {
        return smallestRightChildRecursive(node.getChild3());
    }

    public Node<E> smallestRightChildRecursive (Node<E> node) {
        if(node.getChild1() == null){
            return node;
        }
        return smallestRightChildRecursive(node.getChild1());
    }

    public Node<E> largestLeftChild (Node<E> node) {
        return largestLeftChildRecursive(node.getChild1());
    }

    public Node<E> largestLeftChildRecursive (Node<E> node) {
        if(node.getChild3() == null){
            return node;
        }
        return largestLeftChildRecursive(node.getChild3());
    }


    public void splay(Node<E> grandParent, E keyValue){
        if(root.contains(keyValue)){
            return;
        }

        //it's splay time
        //eerst maak ik een normale splay die geen toppen gaat herverdelen etc
        Node<E> parent = grandParent.getChild(grandParent.whatChild(keyValue));

        if(parent == null || parent.getChild(parent.whatChild(keyValue)) == null){
            return;
        }

        Boolean rootCheck = grandParent == root;


        Node<E> val = parent.getChild(parent.whatChild(keyValue));
        Node<E> newTop = new Node<>(null);
        Node<E> greatGrandParent = grandParent.getParent();



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
            }
            if(grandParent.whatChild(parent.getKey1()) == 3){
                newTop.setKeys(parent.getKey1(), parent.getKey2());
                newTop.addChild(parent.getChild2());
                grandParent.removeChild(grandParent.getChild3());
                grandParent.addChild(parent.getChild1());
                newTop.addChild(grandParent);
                newTop.addChild(val);
            }
        }
        if(rootCheck){
            root = newTop;
            root.setParent(null);
        }
        else{
            greatGrandParent.addChild(newTop);
        }
        if(val.getKey1().equals(keyValue)|| (val.getKey2() != null && val.getKey2().equals(keyValue))){
            return;
        }
        if(newTop.getKey1() == null){
            System.out.println("hey");
        }
        splay(newTop.getChild(newTop.whatChild(keyValue)), keyValue);
    }

    //recursieve methode waar we gewoon van een andere node als zogezegde wortel voort gaan zoeken, returned dan gezochte node
    public Node<E> containsRecursive(E o, Node<E> subRoot){
        if(subRoot == null){
            return null;
        }

        if(o.equals(subRoot.getKey1())|| (subRoot.getKey2() != null && o.equals(subRoot.getKey2()))){
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

    public LinkedList<Comparable> dfs(){
        LinkedList<Comparable> list = new LinkedList<>();
        list = recursiveList(root, list);
        return list;
    }

    public LinkedList<Comparable> recursiveList(Node<E> node, LinkedList<Comparable> list)
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
