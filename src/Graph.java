import java.util.ArrayList;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

public class Graph {

    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<Edge> edges = new ArrayList<>();

    public Node root = null;

    public Node firstChosenNode = null;
    public Node secondChosenNode = null;
    public Node mouseOverNode = null;

    public Graph(String fileName) {
        FileReader reader;
        // load graph data
        try {
            reader = new FileReader(fileName);
            Scanner scanner = new Scanner(reader);
            String[] data = scanner.nextLine().split(" ");
            // read the number of nodes
            int numOfNodes = Integer.parseInt(data[0]);
            System.out.println("Number of nodes: " + numOfNodes);
            // create nodes
            for (int i = 0; i < numOfNodes; i++) this.nodes.add(new Node(i));
            // set root
            this.root = this.nodes.get(0);
            // read the graph configuration
            for (int i = 0; i < numOfNodes; i++) {
                data = scanner.nextLine().split(" ");
                int numOfChildren = Integer.parseInt(data[1]);
                if (numOfChildren > 0)
                    for (int j = 0; j < numOfChildren; j++) {
                        Node node = getNode(i);
                        Node child = getNode(Integer.parseInt(data[2 + j]));
                        Edge edge = new Edge(node, child);
                        node.outEdges.add(edge);
                        child.inEdges.add(edge);
                        this.edges.add(edge);
                    }
            }
        }
        catch (Exception e) {
            System.out.println("ERROR WHILE READING THE GRAPH");
        }
        calculateGeometry();
    }

    public Node getNode(int nodeCode) {
        Node resNode = null;
        for (Node node : nodes) {
            if (node.getCode() == nodeCode) {
                resNode = node;
                break;
            }
        }
        return resNode;
    }

    public void generateGeometry() {
        for (Node node : this.nodes) {
            node.X = (new Random()).nextInt(100);
            node.Y = (new Random()).nextInt(100);
            //System.out.println("(" + node.X + "; " + node.Y + ")");
        }
    }

    public void calculateGeometry() {
        int nodeSize = nodes.size() == 0 ? 0 : 100 / nodes.size() / 2;
        // generateGeometry();
        root.X = 50 - nodeSize / 2; root.Y = nodeSize / 2;
        int levelsNumber = getDepth(root);
        int dY = levelsNumber != 0 ? (100 - nodeSize) / levelsNumber : 50;
        ArrayList<Node> children = root.getChildren();
        for (int i = 0; i < levelsNumber; i++) {
            ArrayList<Node> nextChildren = new ArrayList<>();
            int dX = (100 - nodeSize) / (children.size() + 1);
            for (int j = 0; j < children.size(); j++) {
                children.get(j).X = (j + 1) * dX + nodeSize / 2;
                children.get(j).Y = (i + 1) * dY + nodeSize / 2;
                for (Node nextChild : children.get(j).getChildren())
                    if (!nextChildren.contains(nextChild))
                        nextChildren.add(nextChild);
            }
            children = nextChildren;
        }

    }

    public int getDepth(Node rootNode) {
        int depth = 0;
        Node node = rootNode;
        while (!node.isLeaf()) {
            depth++;
            ArrayList<Node> children = node.getChildren();
            Node longestChild = children.get(0);
            int longestDepth = getDepth(children.get(0));
            for (Node child : children) {
                int childDepth = getDepth(child);
                if (childDepth > longestDepth) {
                    longestDepth = childDepth;
                    longestChild = child;
                }
            }
            node = longestChild;
        }
        return depth;
    }

    public Node getNode(int X, int Y) {
        Node chosenNode = null;
        int nodeSize = nodes.size() == 0 ? 0 : 100 / nodes.size() / 2;
        //
        for (Node node : nodes) {
            if (X > node.X && Y > node.Y &&
                    X < node.X + nodeSize && Y < node.Y + nodeSize) {
                chosenNode = node;
                break;
            }
        }
        return chosenNode;
    }

    public int getMaxNodeCode() {
        int maxNodeCode = root.getCode();
        for (Node node : nodes) {
            if (node.getCode() > maxNodeCode)
                maxNodeCode = node.getCode();
        }
        return maxNodeCode;
    }

    public boolean edgeExists(Node outNode, Node inNode) {
        boolean exists = false;
        for (Edge edge : edges) {
            if (edge.getOutNode() == outNode && edge.getInNode() == inNode) {
                exists = true;
                break;
            }
        }
        return exists;
    }

}
