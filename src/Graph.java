import java.util.ArrayList;
import java.io.FileReader;
import java.util.Scanner;

public class Graph {

    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<Edge> edges = new ArrayList<>();

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
            // read the graph configuration
            for (int i = 0; i < numOfNodes; i++) {
                data = scanner.nextLine().split(" ");
                int numOfChildren = Integer.parseInt(data[1]);
                if (numOfChildren > 0)
                    for (int j = 0; j < numOfChildren; j++)
                        this.edges.add(new Edge(getNode(i),
                                getNode(Integer.parseInt(data[2 + j]))));
            }
        }
        catch (Exception e) {
            System.out.println("ERROR WHILE READING THE GRAPH");
        }
    }

    public Node getNode(int nodeCode) {
        Node resNode = null;
        for (Node node : this.nodes) {
            if (node.getCode() == nodeCode) {
                resNode = node;
                break;
            }
        }
        return resNode;
    }

}
