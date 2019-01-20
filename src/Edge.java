public class Edge {

    private Node outNode = null;
    private Node inNode = null;

    public Edge(Node outNode, Node inNode) {
        this.outNode = outNode;
        this.inNode = inNode;
    }

    public Node getOutNode() {
        return outNode;
    }

    public Node getInNode() {
        return inNode;
    }
}
