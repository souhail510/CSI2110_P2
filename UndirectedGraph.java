
import java.util.*;

class UndirectedGraph<T> {
    private Map<T, LinkedList<T>> adjacencyList;

    // Constructs an empty graph
    public UndirectedGraph() {
        adjacencyList = new HashMap<>();
    }

    // adds a vertex to the graph with an empty adjacency list 
    public void addVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new LinkedList<>());
        }
    }

    // adds an edge between vertex1 and vertex 2.
	// the two adjacency lists are updated
    public void addEdge(T vertex1, T vertex2) {
        addVertex(vertex1);
        addVertex(vertex2);

        adjacencyList.get(vertex1).add(vertex2);
        adjacencyList.get(vertex2).add(vertex1);
    }

    // gets the list of all adjacency vertices of a vertex
    public List<T> getNeighbors(T vertex) {
        return adjacencyList.getOrDefault(vertex, new LinkedList<>());
    }
	
	public int size() { return adjacencyList.size(); }

    public static void main(String[] args) {

		// read query points
        PointSet queryPts = new PointSet(PointSet.read_ANN_SIFT("siftsmall_query.fvecs"));
		// read point set
        PointSet pointSet = new PointSet(PointSet.read_ANN_SIFT("siftsmall_base.fvecs"));
		
        System.out.println("Query set: "+queryPts.getPointsList().size());
        System.out.println("Point set: "+pointSet.getPointsList().size());
	

    }
}
