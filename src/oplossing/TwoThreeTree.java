package opgave;

import java.util.Iterator;

public class TwoThreeTree implements SearchTree{

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
        return true;
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
