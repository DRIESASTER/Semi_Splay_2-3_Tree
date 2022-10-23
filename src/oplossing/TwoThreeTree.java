package oplossing;

import opgave.SearchTree;

import java.util.Arrays;
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
        return containsRecursive(o, root);
    }

    public boolean containsRecursive(Comparable o, Node subRoot){
        if(subRoot == null){
            return false;
        }
        if(o.compareTo(subRoot.getValue()[0]) == 0 || (subRoot.getValue()[1] != null && o.compareTo(subRoot.getValue()[1]) == 0)){
            return true;
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
        if(leaf.addKey(val)){
            return true;
        }

        //anders voegen we het toe als kind van het blad en krijgen we een bijna 2-3 boom
        nodeCount++;
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
        return false;
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
