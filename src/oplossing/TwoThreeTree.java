package oplossing;

import opgave.SearchTree;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class TwoThreeTree<E extends Comparable> implements SearchTree {
    Node root = null;
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
    public boolean contains(Comparable o) {
        //check of gereturnde node null is
        return containsRecursive(o, root) != null;
    }

    //recursieve methode waar we gewoon van een andere node als zogezegde wortel voort gaan zoeken, returned dan gezochte node
    public Node containsRecursive(Comparable o, Node subRoot){
        if(subRoot == null){
            return null;
        }
        if(o.compareTo(subRoot.getValue()[0]) == 0|| (subRoot.getValue()[1] != null && o.compareTo(subRoot.getValue()[1]) == 0)){
            return subRoot;
        }
        return containsRecursive(o, subRoot.getChildren()[subRoot.whatChild(o)]);
    }

    //recursieve methode, ga knopen af tot hij degene vindt die geen kinderen heeft
    public Node findLeaf(Node node, Comparable newVal){
        if(node.emptyChildren() == 3){
            return node;
        }
        return findLeaf(node.getChildren()[node.whatChild(newVal)], newVal);
    }

    @Override
    public boolean add(Comparable val) {
        //als root nog leeg is word 1e toegevoegde gwn root
        if(root == null){
            root = new Node(val);
            nodeCount++;
            return true;
        }

        Node leaf = findLeaf(root, val);
        //probeer value toe te voegen als key van het blad
        //werkt ook zonder maar is sneller zo
        if(leaf.addKey(val)){
            return true;
        }

        //anders voegen we het toe als kind van het blad en krijgen we een bijna 2-3 boom
        //nu moeten we de boom herbalanceren
        //eerst vervangen van blad door kleine binaire boom
        Node subRoot = recursiveSubtree(leaf, new Node(val));
        //omhoog via ouders blijven gaan en we 1e ouder met slechts 1 key vinden
        //daarna voegen we subtree toe aan die node



        return true;
    }

    public Node recursiveSubtree(Node parent, Node child){
        if(parent == null){
            root = child;
            return root;
        }
        if(parent.getValue()[1] == null){
            parent.addNode(child);
            return parent;
        }
        return recursiveSubtree(parent.getParent(),subTree(parent, child));
    }

    //hier maken we een gepaste subtree van de parent node en het kind
    //zoeken eigenlijk gewoon steeds de middelste waarde die de subwortel word en voegen dan de gepaste kinderen toe
    public Node subTree(Node parent, Node child){
        Node subRoot = null;
        Node left = null;
        Node right = null;
        if(parent.getValue()[0].compareTo(child.getValue()[0]) == 1){
            subRoot = new Node(parent.getValue()[0]);
            right = new Node(parent.getValue()[1]);
            right.addChild(parent.getChildren()[1]);
            right.addChild(parent.getChildren()[2]);
            subRoot.addChild(child);
            subRoot.addChild(right);
            return subRoot;
        }
        else if(parent.getValue()[1].compareTo(child.getValue()[0]) == -1){
            subRoot = new Node(parent.getValue()[1]);
            subRoot.setParent(parent.getParent());
            left = new Node(parent.getValue()[0]);
            left.addChild(parent.getChildren()[0]);
            left.addChild(parent.getChildren()[1]);
            subRoot.addChild(left);
            subRoot.addChild(child);
            return subRoot;
        }
        else{
            subRoot = new Node(child.getValue()[0]);
            left = new Node(parent.getValue()[0]);
            right = new Node(parent.getValue()[1]);
            left.addChild(parent.getChildren()[0]);
            left.addChild(child.getChildren()[0]);
            right.addChild(child.getChildren()[1]);
            right.addChild(parent.getChildren()[2]);
            subRoot.addChild(left);
            subRoot.addChild(right);
            return subRoot;
        }
    }

    @Override
    public boolean remove(Comparable comparable) {
        //we vinden eerst de node die we nodig hebben
        Node node = containsRecursive(comparable, root);

        //als het 2 keys heeft kunnen we gewoon 1 van de 2 verwijderen
        if(node.getValue()[0] != null && node.getValue()[1] != null){
            node.removeValue(comparable);
            return true;
        }



        if(node.getValue()[0] == comparable){

        }


        return false;
    }



    //subtree voor onze remove functie
    public Node removeSubtree(Node parent, Node empty){
            parent.removeChild(empty);
            //aantal sleutels bepalen in de subtree
            int amountOfKeys = parent.amountOfKeys();
            for(Node child : parent.getChildren()){
                if(child != null){
                    for(Comparable key : child.getValue()){
                        if(key != null){
                            amountOfKeys+= 1;
                        }
                    }
                }
            }

            //5 scenarios gebaseerd op totaal aantal keys
            if(amountOfKeys == 2){
                parent.clearChildren();
                parent.addChild(empty.getChildren()[0]);
                parent.addNode(parent.getChildren()[0]);
                parent.addNode(parent.getChildren()[2]);

                //als parent de root was is de volledige boom terug in orde
                if(parent == root){
                    return parent;
                }

                //anders moeten we nog voortgaan
                return removeSubtree(parent.getParent(), parent);
            }

            else if(amountOfKeys == 3){
                Node newTop = null;
                //als empty kind 1 is
                if(parent.getChildren()[0] == null){
                    newTop = new Node(parent.getChildren()[2].getValue()[0]);
                    Node left = new Node(parent.getValue()[0]);
                    left.addChild(empty.getChildren()[0]);
                    left.addChild(parent.getChildren()[2].getChildren()[0]);
                    Node right = new Node(parent.getChildren()[2].getValue()[1]);
                    right.addChild(parent.getChildren()[2].getChildren()[1]);
                    right.addChild(parent.getChildren()[2].getChildren()[2]);
                    newTop.addChild(left);
                    newTop.addChild(right);
                }
                else{
                    newTop = new Node(parent.getChildren()[0].getValue()[0]);
                    Node left = new Node(parent.getChildren()[0].getValue()[1]);
                    left.addChild(parent.getChildren()[0].getChildren()[0]);
                    left.addChild(parent.getChildren()[0].getChildren()[1]);
                    Node right = new Node(parent.getValue()[0]);
                    right.addChild(empty.getChildren()[0]);
                    right.addChild(parent.getChildren()[0].getChildren()[2]);
                    newTop.addChild(left);
                    newTop.addChild(right);
                }
                if(parent == root){
                    newTop = root;
                }
                return newTop;
            }

            else if(amountOfKeys == 4){
                Node newTop = null;
                //als empty kind 1 is
                if(parent.getChildren()[0] == null){
                    newTop = new Node(parent.getValue()[1]);
                    Node left = new Node(parent.getValue()[0]);
                    left.addKey(parent.getChildren()[1].getValue()[1]);
                    left.addChild(parent.getChildren()[1].getChildren()[0]);
                    left.addChild(parent.getChildren()[1].getChildren()[2]);
                    left.addChild(empty.getChildren()[0]);
                    newTop.addChild(parent.getChildren()[2]);
                    newTop.addChild(left);
                }
                //als empty kind 2 is
                else if(parent.getChildren()[1] == null){
                    newTop = new Node(parent.getValue()[1]);
                    Node left = parent.getChildren()[0];
                    left.addKey(parent.getValue()[0]);
                    left.addChild(empty.getChildren()[0]);
                    newTop.addChild(parent.getChildren()[2]);
                }
                //als empty kind 3 is
                else if(parent.getChildren()[2] == null){
                    newTop = new Node(parent.getValue()[0]);
                    newTop.addChild(parent.getChildren()[0]);
                    Node right = new Node(parent.getChildren()[0].getValue()[0]);
                    right.addKey(parent.getValue()[1]);
                    right.addChild(empty.getChildren()[0]);
                    right.addChild(parent.getChildren()[1].getChildren()[2]);
                    right.addChild(parent.getChildren()[1].getChildren()[0]);
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

    public Comparable[] printTree(){
        return root.getChildren()[2].getChildren()[2].getValue();
    }


    @Override
    public Iterator iterator() {
        return null;
    }
}
