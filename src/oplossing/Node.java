package oplossing;

public class Node {
    private Node[] children = new Node[3];
    private Comparable[] keys = new Comparable[2];

    private Node parent = null;

    //node word aange
    public Node(Comparable value){
        keys[0] = value;
    }

    public Comparable[] getValue(){
        return keys;
    }

    public void clearChildren(){
        children = new Node[3];
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

    public void removeChild(int index){
        children[index] = null;
    }

    //naar welk kind(index) zou dit value moeten gaan (gebaseerd op waarde t.o.v. keys) -> kleiner dan key 1 -> links, tussen de twee -> midden etc...
    public int whatChild(Comparable value){
        if(keys[0].compareTo(value) == 1){
            return 0;
        }
        else if(keys[1] == null || keys[1].compareTo(value) == -1){
            return 2;
        }
        return 1;
    }

    public Node[] getChildren(){
        return children;
    }

    public boolean addChild(Node child){
        if(child == null){
            return false;
        }
        child.setParent(this);
        int index = whatChild(child.getValue()[0]);
        if(keys[0].compareTo(child.getValue()[0]) == 1){
            children[0] = child;
            return true;
        }
        if(keys[1] == null || keys[1].compareTo(child.getValue()[0]) == -1){
            children[2] = child;
            return true;
        }
        else{
            children[1] = child;
            return true;
        }
    }
    //mag enkel met node met maar 1 key, wordt niet getest, specifiek gebruikt om binaire boom toe te voegen aan 1 sleutel ouder
    public boolean addNode(Node node){
        if(addKey(node.getValue()[0])){
            //organisatie van kinderen bepalen, 2 scenarios: ofwel is teogevoege node kleiner ofwel groter
            if(keys[1] == node.getValue()[0]){
                this.addChild(node.getChildren()[0]);
                this.addChild(node.getChildren()[2]);
            }
            else{
                node.addChild(children[0]);
                this.addChild(node.getChildren()[0]);
                this.addChild(node.getChildren()[1]);
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

    public void setKeys(Comparable[] keys){
        this.keys = keys;
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