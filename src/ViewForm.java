import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class ViewForm {
    private JButton btnDeleteNode, btnAddNode;
    private JButton btnAddEdge, btnDeleteEdge;
    private JButton btnMerge;
    private JPanel pnlMain, pnlTools, pnlView;

    private Dimension screenSize;
    private Graph graph;

    public ViewForm() {
        graph = new Graph("graph.txt");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        btnAddNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.firstChosenNode != null && graph.secondChosenNode == null) {
                    Node newNode = new Node(graph.getMaxNodeCode() + 1);
                    Edge newEdge = new Edge(graph.firstChosenNode, newNode);
                    newNode.inEdges.add(newEdge);
                    graph.firstChosenNode.outEdges.add(newEdge);
                    graph.edges.add(newEdge);
                    graph.nodes.add(newNode);
                }
                // start new graph, if empty
                if (graph.nodes.size() == 0) {
                    graph.nodes.add(new Node(0));
                }
                graph.calculateGeometry();
                pnlMain.repaint();
            }
        });

        btnAddEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.firstChosenNode != null &&
                        graph.secondChosenNode != null &&
                        !graph.edgeExists(graph.firstChosenNode, graph.secondChosenNode)) {
                    Edge newEdge = new Edge(graph.firstChosenNode, graph.secondChosenNode);
                    graph.firstChosenNode.outEdges.add(newEdge);
                    graph.secondChosenNode.inEdges.add(newEdge);
                    graph.edges.add(newEdge);
                    graph.calculateGeometry();
                    pnlMain.repaint();
                }
            }
        });

        btnDeleteNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.firstChosenNode != null && graph.secondChosenNode == null &&
                        graph.firstChosenNode.isLeaf()) {
                    ArrayList<Node> parents = graph.firstChosenNode.getParents();
                    for (Node parent : parents) {
                        Edge edgeToRemove = parent.outEdges.get(0);
                        for (Edge edge : parent.outEdges)
                            if (edge.getInNode() == graph.firstChosenNode)
                                edgeToRemove = edge;
                        parent.outEdges.remove(edgeToRemove);
                        graph.edges.remove(edgeToRemove);
                    }
                    graph.nodes.remove(graph.firstChosenNode);
                    graph.firstChosenNode = null;
                    graph.calculateGeometry();
                    pnlMain.repaint();
                }
            }
        });
    }

    public static void main(String[] args) {
        ViewForm vf = new ViewForm();
        JFrame frame = new JFrame("Graph Editor");
        frame.setContentPane(vf.pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setPreferredSize(new Dimension(vf.screenSize.width / 2,
                                             vf.screenSize.height / 2));

        vf.pnlTools.setSize((int)(vf.screenSize.width * 0.2), vf.screenSize.height);
        vf.pnlView.setSize((int)(vf.screenSize.width * 0.8), vf.screenSize.height);

        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        pnlView = new ViewPanel();

        // set event listeners
        pnlView.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int panelSize = pnlView.getWidth() < pnlView.getHeight() ?
                        pnlView.getWidth() : pnlView.getHeight();
                int x = 100 * e.getX() / panelSize;
                int y = 100 * e.getY() / panelSize;
                //
                Node mouseOverNode = graph.getNode(x, y);
                if (mouseOverNode != null) {
                    if (mouseOverNode != graph.firstChosenNode &&
                            mouseOverNode != graph.secondChosenNode)
                        graph.mouseOverNode = mouseOverNode;
                    System.out.println("Mouse over node: " + mouseOverNode.getCode());
                }
                else {
                    graph.mouseOverNode = null;
                }
                pnlMain.repaint();
            }
        });

        pnlView.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int panelSize = pnlView.getWidth() < pnlView.getHeight() ?
                        pnlView.getWidth() : pnlView.getHeight();
                int x = 100 * e.getX() / panelSize;
                int y = 100 * e.getY() / panelSize;
                //
                Node currentlyChosenNode = graph.getNode(x, y);
                if (currentlyChosenNode != null) {
                    if (graph.firstChosenNode != null)
                        graph.secondChosenNode = currentlyChosenNode;
                    else
                        graph.firstChosenNode = currentlyChosenNode;
                    graph.mouseOverNode = null;
                    System.out.println("Chosen node: " + currentlyChosenNode.getCode());
                }
                else {
                    graph.firstChosenNode = null;
                    graph.secondChosenNode = null;
                }
                pnlMain.repaint();
            }
        });

    }

    private class ViewPanel extends JPanel {

        Color notChosenNodeColor = Color.RED;
        Color firstChosenNodeColor = Color.GREEN;
        Color secondChosenNodeColor = Color.BLUE;
        Color mouseOverNodeColor = Color.PINK;
        Color fontColor = Color.WHITE;

        public int getPanelSize() {
            return getWidth() < getHeight() ? getWidth() : getHeight();
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            // get the number of graph nodes
            int nodesNumber = graph.nodes.size();
            // get the view panel size
            int panelSize = getPanelSize();
            // set nodes size
            int nodeSize = nodesNumber == 0 ? 0 : panelSize / nodesNumber / 2;

            // draw graph edges
            for (Edge edge : graph.edges) {
                Node outNode = edge.getOutNode();
                Node inNode = edge.getInNode();
                int X1 = panelSize * outNode.X / 100 + nodeSize / 2;
                int Y1 = panelSize * outNode.Y / 100 + nodeSize / 2;
                int X2 = panelSize * inNode.X / 100 + nodeSize / 2;
                int Y2 = panelSize * inNode.Y / 100 + nodeSize / 2;
                g2.draw(new Line2D.Double(X1, Y1, X2, Y2));
            }

            // draw graph nodes
            g2.setFont(new Font("Arial", Font.PLAIN, 3 * nodeSize / 5));
            for (Node node : graph.nodes) {
                int X = panelSize * node.X / 100;
                int Y = panelSize * node.Y / 100;
                Shape circle = new Ellipse2D.Double(X, Y, nodeSize, nodeSize);
                if (node.equals(graph.mouseOverNode))
                    g2.setColor(mouseOverNodeColor);
                else if (node.equals(graph.firstChosenNode))
                    g2.setColor(firstChosenNodeColor);
                else if (node.equals(graph.secondChosenNode))
                    g2.setColor(secondChosenNodeColor);
                else
                    g2.setColor(notChosenNodeColor);
                g2.fill(circle);
                g2.setColor(Color.BLACK);
                g2.draw(circle);
                g2.setColor(fontColor);
                g2.drawString(node.getCode().toString(),
                        X + nodeSize / 3, Y + 3 * nodeSize / 4);
            }

        }
    }
}