import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The GraphProcessor reads a graph stored in a file, using a Strongly Connected Components (SCC)
 * algorithm to determine the following:
 * <p>
 * - Out degree of a vertex
 * - Whether two vertices are in the same SCC
 * - Number of SCC's of the graph
 * - Size of the largest component
 * - Given a vertex v, find all vertices that belong to the same SCC as v
 * - Find shortest (BFS) path from a vertex v to u.
 *
 * @author nkarasch
 */
@SuppressWarnings("WeakerAccess")
public class GraphProcessor {
    HashMap<String, HashSet<String>> graph;

    /**
     * @param graphData The absolute path of a file that stores a directed graph
     */
    public GraphProcessor(String graphData) {
        try {
            initGraphFromFile(graphData);
        } catch (IOException e) {
            System.out.println("Couldn't read file! Given filename:  " + graphData);
            e.printStackTrace();
        }
    }

    /**
     * @param v Represents a vertex in the graph
     * @return the out degree of v
     */
    public int outDegree(String v) {
        HashSet<String> set = graph.get(v);
        return (set == null) ? 0 : set.size();
    }

    /**
     * @param u Represents a vertex in the graph
     * @param v Represents a vertex in the graph
     * @return 'true' if u and v belong to the same SCC; otherwise returns 'false'
     */
    public boolean sameComponent(String u, String v) {
        // TODO
        return false;
    }

    /**
     * @param v Represents a vertex in the graph
     * @return All the vertices that belong to the same Strongly Connected Component of v (including v)
     */
    public ArrayList<String> componentVertices(String v) {
        // TODO
        return new ArrayList<String>();
    }

    /**
     * @return The size of the largest component
     */
    public int largestComponent() {
        // TODO
        return 0;
    }

    /**
     * @return The number of Strongly Connected Components
     */
    public int numComponents() {
        // TODO
        return 0;
    }

    /**
     * This method returns an array list of strings that represents the BFS path from u to v.
     * The first vertex in the path must be u and the last vertex must be v. If there is no
     * path from u to v, then this method returns an empty list.
     *
     * @param u Represents a vertex in the graph
     * @param v Represents a vertex in the graph
     * @return The BFS path from u to v.
     */
    public ArrayList<String> bfsPath(String u, String v) {
        ArrayList<String> pathList = new ArrayList<>();
        if (graph.get(u) == null) {
            // The starting vertex wasn't in the graph, so there's no path
            return pathList;
        }

        // Create a BFS-Tree starting at 'u'
        HashMap<String, String> bfsTree = bfsTree(u);

        // Start at the last vertex in the path
        String current = bfsTree.get(v);
        if (current == null) {
            // There is no path from u to v, so return an empty ArrayList
            return pathList;
        }

        // Add the vertices in the path, from child to parent (from v to u)
        pathList.add(v);
        while (current != null) {
            pathList.add(current);
            current = bfsTree.get(current);
        }

        // Reverse the list before returning it, so it goes from u to v
        Collections.reverse(pathList);
        return pathList;
    }

    // Performs a Bread-First Search of the graph, creating a BFS Tree in the form
    // of a HashMap, where the value is the parent vertex of the key
    private HashMap<String, String> bfsTree(String v) {
        LinkedList<String> queue = new LinkedList<>();
        int initSize = (int) Math.ceil(1.5 * graph.size());
        HashMap<String, String> tree = new HashMap<>(initSize);

        queue.add(v);
        tree.put(v, null);
        while (!queue.isEmpty()) {
            String parent = queue.pop();
            HashSet<String> children = graph.get(parent);
            for (String child : children) {
                if (!tree.containsKey(child)) {
                    queue.add(child);
                    tree.put(child, parent);
                }
            }
        }
        return tree;
    }

    // Assuming the file is in the correct format, this method will read the
    // file, initialize the graph, and fill the graph with data
    private void initGraphFromFile(String filename) throws IOException {
        String errMessage = "Incorrect input file format!";
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int numVertices = 42;

        // Parse number of edges from the first line
        if ((line = reader.readLine()) != null) {
            try {
                numVertices = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println(errMessage);
                throw new IOException(errMessage);
            }
        }

        // Initialize the graph
        graph = new HashMap<>((int) Math.ceil(1.5 * numVertices));

        int splitIndex;
        String from, to;
        // Parse the graph data from the remaining lines
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            splitIndex = line.indexOf(" ");
            from = line.substring(0, splitIndex);
            to = line.substring(splitIndex).trim();

            graph.putIfAbsent(from, new HashSet<>());
            graph.putIfAbsent(to, new HashSet<>());
            graph.get(from).add(to);
        }
        reader.close();
    }
}
