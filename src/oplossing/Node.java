package opgave;

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
        for(int i=0 ; i<children.length ; i++){
            if(children[i] == null){
                children[i] = child;
                child.setParent(this);
                return true;
            }
        }
        return false;
    }

    public void setParent(Node parent){
        this.parent = parent;
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