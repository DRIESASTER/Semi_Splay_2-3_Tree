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
        return false;
    }

    //recursieve methode, ga knopen af tot hij degene vindt die geen kinderen heeft
    public Node findLeaf(Node node, Comparable newVal){
        if(node.emptyChildren() == 0){
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
        leaf.addChild(new Node(val));
        nodeCount++;
        //nu moeten we de boom herbalanceren
        //eerst vervangen van blad door kleine binaire boom
        Node subRoot = leafToTree(leaf, val);
        //omhoog via ouders blijven gaan en we 1e ouder met slechts 1 key vinden
        //daarna voegen we subtree toe aan die node
        firstEmptyParent(leaf).addNode(subRoot);

        return true;
    }

    //omhoog via ouders blijven gaan en we 1e ouder met slechts 1 key vinden
    public Node firstEmptyParent(Node child){
        if(child.getParent().getValue()[1] == null){
            return child.getParent();
        }
        return firstEmptyParent(child.getParent());
    }

    public Node leafToTree(Node leaf, Comparable val){
        Comparable[] values = new Comparable[3];
        values[0] = val;
        values[1] = leaf.getValue()[0];
        values[2] = leaf.getValue()[1];
        Arrays.stream(values).sorted();
        Node root = new Node(values[1]);
        root.addChild(new Node(values[0]));
        root.addChild(new Node(values[1]));
        return root;
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

    @Override
    public Iterator iterator() {
        return null;
    }
}
