package oplossing;

import opgave.SearchTree;
import java.util.Iterator;

public class TwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {
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
    public Node findLeaf(Node<E> node, E newVal){
        if(node.emptyChildren() == 3){
            return node;
        }
        return findLeaf(node.getChild(node.whatChild(newVal)), newVal);
    }

    @Override
    public boolean add(E val) {
        //als root nog leeg is word 1e toegevoegde gwn root
        if(root == null){
            root = new Node<E>(val);
            nodeCount++;
            return true;
        }

        Node<E> leaf = findLeaf(root, val);
        //probeer value toe te voegen als key van het blad
        //werkt ook zonder maar is sneller zo
        if(leaf.addKey(val)){
            return true;
        }

        //anders voegen we het toe als kind van het blad en krijgen we een bijna 2-3 boom
        //nu moeten we de boom herbalanceren
        //eerst vervangen van blad door kleine binaire boom
        Node<E> subRoot = recursiveSubtree(leaf, new Node<E>(val));
        //omhoog via ouders blijven gaan en we 1e ouder met slechts 1 key vinden
        //daarna voegen we subtree toe aan die node



        return true;
    }

    public Node<E> recursiveSubtree(Node<E> parent, Node<E> child){
        if(parent == null){
            root = child;
            return root;
        }
        if(parent.getKey2() == null){
            parent.addNode(child);
            return parent;
        }
        return recursiveSubtree(parent.getParent(),subTree(parent, child));
    }

    //hier maken we een gepaste subtree van de parent node en het kind
    //zoeken eigenlijk gewoon steeds de middelste waarde die de subwortel word en voegen dan de gepaste kinderen toe
    public Node<E> subTree(Node<E> parent, Node<E> child){
        Node<E> subRoot = null;
        Node<E> left = null;
        Node<E> right = null;
        if(parent.getKey1().compareTo(child.getKey1()) == 1){
            subRoot = new Node(parent.getKey1());
            right = new Node(parent.getKey2());
            right.addChild(parent.getChild2());
            right.addChild(parent.getChild3());
            subRoot.addChild(child);
            subRoot.addChild(right);
            return subRoot;
        }
        else if(parent.getKey2().compareTo(child.getKey1()) == -1){
            subRoot = new Node(parent.getKey2());
            subRoot.setParent(parent.getParent());
            left = new Node(parent.getKey1());
            left.addChild(parent.getChild1());
            left.addChild(parent.getChild2());
            subRoot.addChild(left);
            subRoot.addChild(child);
            return subRoot;
        }
        else{
            subRoot = new Node(child.getKey1());
            left = new Node(parent.getKey1());
            right = new Node(parent.getKey2());
            left.addChild(parent.getChild1());
            left.addChild(child.getChild1());
            right.addChild(child.getChild2());
            right.addChild(parent.getChild3());
            subRoot.addChild(left);
            subRoot.addChild(right);
            return subRoot;
        }
    }

    @Override
    public boolean remove(E val) {
        //we vinden eerst de node die we nodig hebben
        Node node = containsRecursive(val, root);

        //als het 2 keys heeft kunnen we gewoon 1 van de 2 verwijderen
        if(node.getKey1() != null && node.getKey2() != null){
            node.removeValue(val);
            return true;
        }



        if(node.getKey1() == val){

        }


        return false;
    }



    //subtree voor onze remove functie
    public Node removeSubtree(Node<E> parent, Node<E> empty){
            parent.removeChild(empty);
            //aantal sleutels bepalen in de subtree
            int amountOfKeys = parent.amountOfKeys();
            for(int i=1; i<4 ; i++){
                Node<E> child = parent.getChild(i);
                if(child != null){
                    for(int k=1 ; k<4 ; k++){
                        if(child.getChild(k) != null){
                            amountOfKeys+=1;
                        }
                    }
                }
            }

            //5 scenarios gebaseerd op totaal aantal keys
            if(amountOfKeys == 2){
                parent.clearChildren();
                parent.addChild(empty.getChild1());
                parent.addNode(parent.getChild1());
                parent.addNode(parent.getChild3());

                //als parent de root was is de volledige boom terug in orde
                if(parent == root){
                    return parent;
                }

                //anders moeten we nog voortgaan
                return removeSubtree(parent.getParent(), parent);
            }

            else if(amountOfKeys == 3){
                Node<E> newTop = null;
                //als empty kind 1 is
                if(parent.getChild1() == null){
                    newTop = new Node<E>(parent.getChild3().getKey1());
                    Node<E> left = new Node(parent.getKey1());
                    left.addChild(empty.getChild1());
                    left.addChild(parent.getChild3().getChild1());
                    Node<E> right = new Node<E>(parent.getChild3().getKey2());
                    right.addChild(parent.getChild3().getChild2());
                    right.addChild(parent.getChild3().getChild3());
                    newTop.addChild(left);
                    newTop.addChild(right);
                }
                else{
                    newTop = new Node(parent.getChild1().getKey1());
                    Node<E> left = new Node<E>(parent.getChild1().getKey2());
                    left.addChild(parent.getChild1().getChild1());
                    left.addChild(parent.getChild1().getChild2());
                    Node<E> right = new Node<E>(parent.getKey1());
                    right.addChild(empty.getChild1());
                    right.addChild(parent.getChild1().getChild3());
                    newTop.addChild(left);
                    newTop.addChild(right);
                }
                if(parent == root){
                    newTop = root;
                }
                return newTop;
            }

            else if(amountOfKeys == 4){
                Node<E> newTop = null;
                //als empty kind 1 is
                if(parent.getChild1() == null){
                    newTop = new Node(parent.getKey2());
                    Node<E> left = new Node<>(parent.getKey1());
                    left.addKey(parent.getChild2().getKey2());
                    left.addChild(parent.getChild2().getChild1());
                    left.addChild(parent.getChild2().getChild3());
                    left.addChild(empty.getChild1());
                    newTop.addChild(parent.getChild3());
                    newTop.addChild(left);
                }
                //als empty kind 2 is
                else if(parent.getChild2() == null){
                    newTop = new Node<E>(parent.getKey2());
                    Node<E> left = parent.getChild1();
                    left.addKey(parent.getKey1());
                    left.addChild(empty.getChild1());
                    newTop.addChild(parent.getChild3());
                }
                //als empty kind 3 is
                else if(parent.getChild3() == null){
                    newTop = new Node<E>(parent.getKey1());
                    newTop.addChild(parent.getChild1());
                    Node<E> right = new Node(parent.getChild1().getKey1());
                    right.addKey(parent.getKey2());
                    right.addChild(empty.getChild1());
                    right.addChild(parent.getChild2().getChild3());
                    right.addChild(parent.getChild2().getChild1());
                    newTop.addChild(right);
                }
                return newTop;
            }
            else if(amountOfKeys == 5){
                //todo
            }

            else if(amountOfKeys == 6){
                //todo
            }

            return null;
    }

    @Override
    public void clear(){
        root = null;
        nodeCount = 0;
    }

    public E printTree(){
        //System.out.println(root.getKey1());
        //System.out.println(root.getKey2());
        //System.out.println(root.getChild1().getKey1());
        //System.out.println(root.getChild1().getKey2());
        //System.out.println(root.getChild2().getKey1());
        //System.out.println(root.getChild2().getKey2());
        //System.out.println(root.getChild3().getKey1());
        //System.out.println(root.getChild3().getKey2());
        return null;
    }


    @Override
    public Iterator iterator() {
        return null;
    }
}
