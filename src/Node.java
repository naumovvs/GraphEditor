import java.util.ArrayList;

public class Node {

    private Integer code;
    public ArrayList<Edge> outEdges = new ArrayList<>();
    public ArrayList<Edge> inEdges = new ArrayList<>();

    public int X, Y;

    public Node(int code) {
        this.code = code;
        this.X = 0;
        this.Y = 0;
    }

    public Integer getCode() {
        return this.code;
    }

    public boolean isLeaf() {
        return outEdges.isEmpty();
    }

    public boolean isRoot() {
        return inEdges.isEmpty();
    }

    public ArrayList<Node> getChildren() {
        ArrayList<Node> children = new ArrayList<>();
        for (Edge edge : outEdges) {
            children.add(edge.getInNode());
        }
        return children;
    }

    public ArrayList<Node> getParents() {
        ArrayList<Node> parents = new ArrayList<>();
        for (Edge edge : inEdges) {
            parents.add(edge.getOutNode());
        }
        return parents;
    }

    public boolean isParentOf(Node otherNode) {
        boolean isParent = false;
        for (Edge edge : outEdges) {
            if (edge.getInNode() == otherNode) {
                isParent = true;
                break;
            }
        }
        return isParent;
    }

}
