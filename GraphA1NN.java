/* Student name:     Souhail Daoudi
 * Student number:    300135458
*/

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.ConcurrentModificationException;


class GraphA1NN {
	
	UndirectedGraph<LabelledPoint> annGraph;
     private PointSet dataset;
	 ArrayList<List<Integer> > adjacency;
	private int counter=1;
	private static int K;
	private int count=0;
	private int M;
	private int nextIndex=0;
	private int S;
	private LabelledPoint W;
	private LabelledPoint[] A ;

	
	// construct a graph from a file
    public GraphA1NN(String fvecs_filename,int S) {

	    annGraph= new UndirectedGraph<>();
		dataset= new PointSet(PointSet.read_ANN_SIFT(fvecs_filename));
		A=new LabelledPoint[S];
    }

	// construct a graph from a dataset
    public GraphA1NN(PointSet set){
		
	   annGraph= new UndirectedGraph<>();
       this.dataset = set;
	  
    }
	public int setS(int S){
		this.S=S;
		return S;
	}
public LabelledPoint findRandomVertex(){

 Random random = new Random();
        int MyRandom = random.nextInt(dataset.getPointsList().size());
		// select a random vertex
       return dataset.getPointsList().get(MyRandom);
	  
}

public void sort(LabelledPoint pt){

	if ((A[S-1]!=null)){
	for(int i=0;i<S-1;i++){
		if(A[i].distanceTo(pt) > (A[i+1].distanceTo(pt))) {
        LabelledPoint temp=A[i+1];
        A[i+1]=A[i];
		A[i]=temp;
		}
	}
	}

}
	public LabelledPoint [] find1NN(LabelledPoint pt,int K) {
		System.out.println(" ");
		System.out.print(pt.getLabel()+": ");


	W=findRandomVertex();
	W.distanceTo(pt);
    A[0]=W;
	W.setIKey(pt.getLabel());  // distance already computed
	W.setKey(0); // first element 
	W.unchecked();
	
	

   for(LabelledPoint lb : A){
	
	if(lb==null){
		break;
	}
      
    if (!lb.isChecked()){
        

	// adjacent vertices

       for(LabelledPoint lb2 : annGraph.getNeighbors(lb)){
        sort(pt);  // sort array A
		
           if(lb2.getIKey()==-1){
    	   lb2.distanceTo(pt);
           lb2.setIKey(pt.getLabel());

         // array not full
		 if(nextIndex+1<=S-1){
		   if(A[nextIndex+1]==null){
		       A[nextIndex+1]=lb2;
			   lb2.checked();
		       nextIndex++;	
		   }
		}

       // array full
		   else{

		   if (lb2.distanceTo(pt)<A[S-1].distanceTo(pt)){
			A[S-1].unchecked(); // uncheck removed element
            A[S-1]=lb2;
	
		   }
		}
		  
              
			}
     }
	}

   }
count=0;
   // print array A
   	System.out.print(" [ ");
for(LabelledPoint lb3: A){
	if((lb3==null)||(count==K)){
		break;
	}
	count++;
	System.out.print(lb3.getLabel());
	System.out.print(" , ");

}
System.out.print("]");
    return A ;
	}

    // build the graph
    public void constructKNNGraph(int K) {		
     for (int i = 0; i < adjacency.size(); i++) {
		M=K+1;
         for (int knn : adjacency.get(i)) {     // find knn of vertices
		 	M--;
			if(M==0){
				break;
			}
			

		// print the vertices and their knn	
		
    /*  if (counter-i==1){
		 System.out.println();
         System.out.print(dataset.getPointsList().get(i).getLabel()+" : ");
	     counter++;
                }
                // this is to remove last comma from list
				if (M==1){
                   System.out.print(dataset.getPointsList().get(knn).getLabel());
                  }
                else{
			       System.out.print(dataset.getPointsList().get(knn).getLabel()+" , ");
                 }*/

         // connecter les vertices
         annGraph.addEdge(dataset.getPointsList().get(i), dataset.getPointsList().get(knn));
                }
            }	
    }
	
	public static ArrayList<List<Integer> > readAdjacencyFile(String fileName, int numberOfVertices) 
	                                                                 throws Exception, IOException
	{	
		ArrayList<List<Integer> > adjacency= new ArrayList<List<Integer> >(numberOfVertices);
		for (int i=0; i<numberOfVertices; i++) 
			adjacency.add(new LinkedList<>());
		
		// read the file line by line
	    String line;
        BufferedReader flightFile = 
        	      new BufferedReader( new FileReader(fileName));
        
		// each line contains the vertex number followed 
		// by the adjacency list
        while( ( line = flightFile.readLine( ) ) != null ) {
			StringTokenizer st = new StringTokenizer( line, ":,");
			int vertex= Integer.parseInt(st.nextToken().trim());
			while (st.hasMoreTokens()) { 
			    adjacency.get(vertex).add(Integer.parseInt(st.nextToken().trim()));
			}
        } 
	
	    return adjacency;
	}

	public int size() { return annGraph.size(); }

    public static void main(String[] args) throws IOException, Exception {
		int S=Integer.parseInt(args[1]);
		int K=Integer.parseInt(args[0]);
         GraphA1NN graph = new GraphA1NN(args[2],S);
		

		graph.adjacency= GraphA1NN.readAdjacencyFile("knn.txt", graph.dataset.getPointsList().size());
		graph.constructKNNGraph(K);
		PointSet queryPts = new PointSet(PointSet.read_ANN_SIFT(args[3]));
		graph.setS(S);
		System.out.println("finding nearest neighbor of query points");

		long startTime = System.currentTimeMillis(); // start timer	

	   // call find1NN with querry points
	 	for(LabelledPoint q : queryPts.getPointsList()) {	
	     	graph.find1NN(q,K);
		}
		long endTime = System.currentTimeMillis(); // end timer
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
		
	    System.out.println();
        System.out.println("Number of vertices: "+ graph.size());
		
	}
}

