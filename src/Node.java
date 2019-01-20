import java.util.ArrayList;

public class Node {

    private Integer code;
    public ArrayList<Edge> outEdges = new ArrayList<>();
    public ArrayList<Edge> inEdges = new ArrayList<>();

    public Node(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

}
