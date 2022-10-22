package oplossing;

public class Node {
    private Node[] children = new Node[3];
    private Comparable[] keys = new Comparable[2];

    private Node parent = null;

    //node word aange
    public Node(Comparable value){
        for(int i = 0; i< keys.length ; i++){
            this.keys[i] = keys[i];
        }
    }

    public Comparable[] getValue(){
        return keys;
    }

    //returns amount of empty children
    public int emptyChildren(){
        int counter = 0;
        for(Node child : children){
            if(child == null){
                counter++;
            }
        }
        return counter;
    }

    //naar welk kind(index) zou dit value moeten gaan (gebaseerd op waarde t.o.v. keys) -> kleiner dan key 1 -> links, tussen de twee -> midden etc...
    public int whatChild(Comparable value){
        for(int i=0 ; i<keys.length ; i++){
            if(keys[i] == null || value.compareTo(keys[i]) == -1){
                return i;
            }
        }
        return keys.length;
    }

    public Node[] getChildren(){
        return children;
    }

    public boolean addChild(Node child){
        int index = whatChild(child.getValue()[0]);
        for(int i=0 ; i<children.length ; i++){
            if(children[i] == null){
                children[i] = child;
                child.setParent(this);
                return true;
            }
        }
        return false;
    }
    //mag enkel met node met maar 1 key, wordt niet getest, specifiek gebruikt om binaire boom toe te voegen aan 1 sleutel ouder
    public boolean addNode(Node node){
        if(addKey(node.getValue()[0])){
            //organisatie van kinderen bepalen, 2 scenarios: ofwel is teogevoege node kleiner ofwel groter
            if(keys[1] == node.getValue()[0]){
                children[1] = node.getChildren()[0];
                children[2] = node.getChildren()[1];
            }
            else{
                children[2] = children[0];
                children[0] = node.getChildren()[0];
                children[1] = node.getChildren()[1];
            }
            return true;
        }
        return false;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Node getParent(){
        return parent;
    }

    public boolean addKey(Comparable key){
        if(keys[1] == null){
            if(key.compareTo(keys[0]) == 1){
                keys[1] = key;
            }
            else{
                keys[1] = keys[0];
                keys[0] = key;
            }
            return true;
        }
        return false;
    }
}