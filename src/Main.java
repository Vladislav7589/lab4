import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JFrame;

import edu.uci.ics.jung.graph.*;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/** Этот класс предназначен для того, чтобы производить поиск в ширину по заданной вершине
 * @author Vlad
 */
class BFS {
    static ArrayList<Integer> rezult;
    public static void main(String[] args) throws InterruptedException {

        boolean[][] adjacencyMatrix = {
                /*0*/   /*1*/   /*2*/   /*3*/   /*4*/   /*5*/   /*6*/   /*7*/   /*8*/   /*9*/   /*10*/  /*11*/
         /*0*/ {false,  false,  true,   false,  false,  false,  true,   false,  false,  false,  true,   true},
         /*1*/ {false,  false,  true,   false,  false,  false,  false,  false,  false,  true,   true,   false},
         /*2*/ {true,   true,   false,  false,  true,   false,  false,  false,  false,  false,  false,  false},
         /*3*/ {false,  false,  false,  false,  false,  false,  false,  false,  true,   true,   false,  false},

         /*4*/ {false,  false,  true,  false,  false,  false,  false,  true,  true,  true,  false,  false},

         /*5*/ {false,  false,  false,  false,  false,  false,  true,  true,  false,  false,  false,  false},

         /*6*/ {true,  false,  false,  false,  false,  true,  false,  false,  false,  false,  false,  false},

         /*7*/ {false,  false,  false,  false,  true,  true,  false,  false,  false,  false,  false,  true},

         /*8*/ {false,  false,  false,  true,  true,  false,  false,  false,  false,  false,  false,  false},
         /*9*/ {false, true,  false,  true,  true,  false,  false,  false,  false,  false,  false,  false},
         /*10*/{true,  true,  false,  false,  false,  false,  false,  false,  false,  false,  false,  false},
         /*11*/{true,  false,  false,  false,  false,  false,  false,  true,  false,  false,  false,  false} };
        Scanner in = new Scanner(System.in);

        System.out.print("Введите вершину от которой производить поиск 0 - 11: ");
        int root = in.nextInt();
        if(root >=0 && root <=11) {
        }else {
            while ((root < 0) || (root > 11)) {
                System.out.println("Ошибка");
                System.out.print("Введите вершину от которой производить поиск 0 - 11: ");
                root = in.nextInt();
            }
        }
        rezult = QueueBFSMatrix(adjacencyMatrix, root);
        System.out.println(rezult.toString());
        display(adjacencyMatrix, "Поиск в ширину");

    }

    /** Этот метод принимает вершину и матрицу смежности и на основе их простраивает поиск в ширину
     * @param adjmat это матрица смежности
     * @param source вершина с которой начинается поиск
     * @return  возвращает последовательность поиска в ширину
     */
    public static ArrayList<Integer> QueueBFSMatrix(boolean[][] adjmat, int source){
        ArrayList<Integer> rezult = new ArrayList<Integer>();
        boolean[] visited = new boolean[adjmat.length+1];
        Queue<Integer> q=new LinkedList<Integer>();
        q.add(source);
        visited[source] = true;

        while(!q.isEmpty()){
            int top = q.poll();
            rezult.add(top);
            visited[top] = true;
            for(int node=1; node <= adjmat.length; node++){
                boolean[] adj = adjmat[top-1];
                if(adj[node-1] && !visited[node]){
                    q.add(node);
                    visited[node] = true;
                }
            }
        }
        return rezult;
    }


    /** Этот метод отрисовывает поиск в ширину с помощью библеотеки JUNG
     * @param adj это матрица смежности
     * @param name имя окна
     */
    public static void display(boolean[][] adj, String name) throws InterruptedException {

        //Populate Graph object
        Graph<Integer,Integer> graph = new SortedSparseMultigraph<Integer,Integer>();
        for(int i=0;i<adj.length;i++){
            graph.addVertex(i+1);
            for(int j=0;j<adj[0].length;j++){
                if(adj[i][j]){
                    adj[j][i] = false;
                    graph.addEdge((int)(Math.random()*Integer.MAX_VALUE), i+1, j+1, EdgeType.UNDIRECTED);}
            }
        }


        Layout<Integer, String> layout = new CircleLayout(graph);
        layout.setSize(new Dimension(620,620));
        BasicVisualizationServer<Integer,String> vs = new BasicVisualizationServer<Integer,String>(layout);
        vs.setPreferredSize(new Dimension(650,650));



        JFrame frame = new JFrame(name);
        frame.getContentPane().add(vs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                return Color.RED;
            }
        };

        vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

        for(int g = 0; g < rezult.size(); g++) {
            Thread.sleep(2000);
            int finalG = g;
            Transformer<Integer, Paint> vertexPaint2 = new Transformer<Integer, Paint>() {

                public Paint transform(Integer i) {
                    for(int j = 0; j <= finalG; j++)
                        if(i == rezult.get(finalG-j)) return Color.GREEN;
                        return Color.RED;

                }
            };
            vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint2);
            vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
            vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

            frame.repaint();

        }
    }

}