package opgave;

import java.util.ArrayList;

public class CustomNode {
    private CustomNode[] children = new CustomNode[3];
    private Comparable value;

    public CustomNode(Comparable o){
        value = o;
    }

    public Comparable getValue(){
        return value;
    }

    public CustomNode[] getChildren(){
        return children;
    }
}