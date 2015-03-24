package edu.ksu.cis.bnj.bbn.learning.scorebased.gradient;

/*
 * Created on Tue 10 Jun 2003
 * 
 * This file is part of Bayesian Networks in Java (BNJ).
 *
 * BNJ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * BNJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BNJ in LICENSE.txt file; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

import edu.ksu.cis.bnj.bbn.learning.*;
//import edu.ksu.cis.kdd.data.converter.*;
import edu.ksu.cis.kdd.data.*;
//import edu.ksu.cis.bnj.bbn.learning.scorebased.k2.*;
import edu.ksu.cis.bnj.bbn.learning.score.*;
import edu.ksu.cis.kdd.util.*;
import edu.ksu.cis.bnj.bbn.*;
//import salvo.jesus.graph.*;
import java.util.*;
import java.io.*;
import javax.swing.JDialog;
import java.lang.*;


/**
 * @author Prashanth Boddhireddy
 *
 */
public class GreedySL extends ScoreBasedLearner
{
	protected BBNGraph bbnGraph;
	protected Set[] parentTable;
	protected boolean path[][];
	protected Hashtable mlchash; 
	protected int 
		numOfNodes,
		numOfChanges = 0,
		totalNumOfChanges = 0;	
	protected double 
		tempScore = 0,
		previousScore = -1000,
	  maxScore = -1000;
	protected int recentChange;  
        protected Random rp;
        protected int lmprev = 0; 
	public static final boolean CYCLE  =true;
	public boolean containsCycle = false;
	public static final int 
		MAXNUMCHANGES = 1000,		
		REVERSED = 1,
		REMOVED = 2,
		ADDED = 3,
		NA = -1;
		
		

	public GreedySL(){}
	public GreedySL(Data data)
	{
		super (data);
	}
	
	public BBNGraph getGraph()
	{
		 if (candidateScorer == null) 
        	candidateScorer = new BDEScore(this);
     initializeNodes();   		   
     initializeNodeParents();
     learnGraph();
	  computeCPT(bbnGraph);
     return bbnGraph;
	} 
	
        public double getbde(int nNode, int[][] dag, int nogenes)
        {
  
         double score = 0;
      try{
         if (candidateScorer == null)
                candidateScorer = new BDEScore(this);
     initializeNodes();
     initializeNodeParents();

          // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     parentTable[i].remove(new Integer(j));
                }
                }
               
               // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     if(dag[i][j]>0)
                        parentTable[i].add(new Integer(j));
                }
                }

            score = 0;
            score = candidateScorer.getScore(nNode, NA, parentTable);
                              }
            catch(Exception e){}

            return score;
        }  

        public double getbic(int nNode, int[][] dag, int nogenes)
        {
           if (candidateScorer == null)
                candidateScorer = new BICScore(this);
     initializeNodes();
     initializeNodeParents();

          // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     parentTable[i].remove(new Integer(j));
                }
                }

               // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     if(dag[i][j]>0)
                        parentTable[i].add(new Integer(j));
                }
                }

            double score = 0;
            score = candidateScorer.getScore(nNode, NA, parentTable);

            return score;
        }

         public double getbdeg(int nNode, int[][] dag, int nogenes)
        {
           if (candidateScorer == null)
                candidateScorer = new BDEScoreg(this);
     initializeNodes();
     initializeNodeParents();

          // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     parentTable[i].remove(new Integer(j));
                }
                }

               // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     if(dag[i][j]>0)
                        parentTable[i].add(new Integer(j));
                }
                }

            double score = 0;
            score = candidateScorer.getScore(nNode, NA, parentTable);

          
            return score;
        }

       public double getbdegs(int nNode, int[][] dag, int nogenes)
        {
           if (candidateScorer == null)
                candidateScorer = new BDEScoregs(this);
     initializeNodes();
     initializeNodeParents();

          // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     parentTable[i].remove(new Integer(j));
                }
                }

               // read in the structure
            for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     if(dag[i][j]>0)
                        parentTable[i].add(new Integer(j));
                }
                }

            double score = 0;
            score = candidateScorer.getScore(nNode, NA, parentTable);


            return score;
       }

	protected void learnGraph()
	{
               //read in dag
               int nogenes = 14;
               // read in the structure
               for(int i=0; i<nogenes; i++)
                {
                   for(int j=0; j<nogenes; j++)
                {
                     parentTable[i].remove(new Integer(j));
                }
                }

    try{
        FileReader fr = new FileReader("/home/itic/mpi/jan25/syn4b/ord1bnp/dag.txt");
        BufferedReader br = new BufferedReader(fr);
        String s = "";
        int j=0;
        s=br.readLine();
        String[] cell = s.split(" ");
        do
        {
                cell = s.split(" ");
                for(int i=0; i<nogenes; i++)
                {
                        if(Integer.parseInt(cell[i])>0){
                             int row = j;
                             int col = i;
                             parentTable[row].add(new Integer(col));                                                         
                         } 
             }
                j++;
        }
        while((s=br.readLine())!=null);

          double score = 0;
         // get score
          FileWriter ostreamg = new FileWriter("/home/itic/mpi/jan25/syn4b/ord1bnp/ans.txt");

         for(int i=0;i<nogenes;i++){
            score = candidateScorer.getScore(i, NA, parentTable);
                        ostreamg.write(score+"\n");
         }
          ostreamg.close();


    }
    catch(Exception e){}

  
		mlchash = new Hashtable();
                rp = new Random();
		while (numOfChanges > 0)
		{			
			numOfChanges = 0;
		for(int node = 0; node < numOfNodes; node++)
			{ 
//			   if(numOfChanges>5){
//		       node = rp.nextInt(100000)%numOfNodes;                         }
				for (int parent = 0; parent < numOfNodes; parent++)
				{	
                 //                      if(numOfChanges>5){
                   //    parent = rp.nextInt(100000)%numOfNodes;            }		        
			               if (node == parent) continue;
					if (parentTable[node].contains(new Integer(parent)))				
						reverseOrRemoveEdge(node, parent);				
					else				
						addEdge(node, parent);											
				}		
			}
		 	totalNumOfChanges+= numOfChanges;
                       // System.out.println(totalNumOfChanges+"changes"+" "+MAXNUMCHANGES);
			if (totalNumOfChanges > MAXNUMCHANGES)
          		break;
		}
		displayParentTable();
		addParentsToGraph();

	}
	
	private void addEdge(int node, int parent)
	{
		previousScore = getScore(node, parent);
		parentTable[node].add(new Integer(parent));
		if (!isCyclic())
		{
			tempScore = getScore(node, parent);
			if (tempScore > previousScore)
			{	numOfChanges++;	getScoreGraph(); return;}	
		}	
		parentTable[node].remove(new Integer(parent));	
	}
	
	private void reverseOrRemoveEdge(int node, int parent)
	{		
		previousScore = getScore (node, parent);
		reverseEdge(node,parent);		// node <----parent to node----->parent
		if(!isCyclic())		
			tempScore = getScore(node, parent);				
		else	
		{	
			reverseEdge(parent, node);
			removeEdge(node,parent); // since edge is already inversed
			tempScore = getScore(node, parent);
		}	
		if (previousScore > tempScore)
			undoChange(node, parent);
		else		
			{ numOfChanges++; getScoreGraph();}	
	}
	
	private void reverseEdge (int node, int parent)
	{
		parentTable[node].remove(new Integer(parent));
		parentTable[parent].add(new Integer(node));
		recentChange = REVERSED;
	}
	
	private void removeEdge(int node, int parent)
	{
		parentTable[node].remove(new Integer(parent));
		recentChange = REMOVED;
	}
	
	private void undoChange(int node, int parent)
	{
			if (recentChange == REVERSED)
				reverseEdge(parent, node);             
			if (recentChange == REMOVED)
				parentTable[node].add(new Integer(parent));
	}
	
	private boolean isCyclic()
	{
		boolean cycleFound = false;		
		findClosure();
		for (int i = 0; i < numOfNodes; i++)
		{
			if (path[i][i])
			{
				cycleFound = true;
				break;
			}
		}
		return cycleFound;
	}
	
	private void getScoreGraph()
	{
        	double score = candidateScorer.getScore(0, NA, parentTable);
		for (int j=1; j<numOfNodes; j++) 
		score += candidateScorer.getScore(j, NA, parentTable);
		String dags = score+" ";
	    
	      for(int node = 0; node < numOfNodes; node++)
			{ 
				for (int parent = 0; parent < numOfNodes; parent++)
				{
					if (parentTable[node].contains(new Integer(parent)))				
						dags+="1 ";				
					else				
						dags+="0 ";											
				}		
			}
	
	      
	      mlchash.put(new Integer(mlchash.size()), new String(dags));
		System.out.println(score);
		//rollback();
		return;
	}	
	
	
	private double getScore(int i, int j)
	{
	        double 
		score = candidateScorer.getScore(i, NA, parentTable);
		score += candidateScorer.getScore(j, NA, parentTable);
		return score;
	}	
	
	private void rollback()
	{
		 double[] ml1 = new double[mlchash.size()+1];
		 int[] retval = new int[numOfNodes*numOfNodes];
		 
		 //System.out.println(mlchash.size());
		 
		    int lm = 0, mlc=0, myc = 0;

                     try{
                      Enumeration e = mlchash.keys();
                      while(e.hasMoreElements())
                      {
                        int keys = (Integer)e.nextElement();
                        String dags = (String)mlchash.get(keys);
                        String[] tokens = dags.split(" ");
                        ml1[keys-1]=Double.parseDouble(tokens[0]);
                        myc++;
                      }
 
                     }catch(Exception e){}

		    
		    mlc = mlchash.size();
                    double loc1=-10000, high = -10000;
                    int[] lmpos = new int[mlc];
                    int lmcnt = 0, check=0, highlm = 0;
                    /* 
                    for(lm=mlc-myc; lm<mlc; lm++)
                    {
                           if(ml1[lm]<=ml1[lm+1] && ml1[lm]<=loc1 && lmcnt>20){check=1; break;}
                           if(ml1[lm]<=ml1[lm+1]){loc1=ml1[lm]; lmpos[lmcnt]=lm; lmcnt++;}
                           if(high < ml1[lm] && mlc-lm < 5 && ml1[lm]!=ml1[0] ){high = ml1[lm]; highlm = lm;}
                    }
                    */
                     
                   if(mlc > 30){
                    loc1 = ml1[mlc-2];
                    for(lm=mlc-3;lm>0;lm--)
                    { 
                         //System.out.println(loc1+" "+ml1[lm]);
                         if(ml1[lm]==loc1)check++;
                         if(Math.abs(ml1[lm]-loc1)>0.05){ System.out.println("exiting loop"); break; }
                    }  
                    }

		    if(check>20)//set dag to previous minima
		    {
                     //lm = lm - rp.nextInt(10);
                   
                     if(lm > lmprev){ 

		     System.out.println("phew "+lm+" "+ml1[lm]+" "+mlc);
		     //lm = highlm;
/*		     try{

			    	String dags = mlchash.toString();
			    	//dags = dags.substring(1);
			    	//dags = dags.substring(0,dags.length());
			    	//System.out.println(dags);
			    	String[] tokens = dags.split(",");
			    	for(int j=0; j<tokens.length; j++){
		            	   String dags2 = tokens[j];
		            	   dags2 = dags2.substring(1,dags2.length());
		            	   //System.out.println(tokens[j]);
		            	   String[] tokens2 = dags2.split("=");
		            	   int keys = Integer.parseInt(tokens2[0]);
		   //         	   System.out.println(keys);
//		            	   if(keys==lm+1){
		            		  // System.out.print(tokens2[1]);
		            	   String[] tokens3 = tokens2[1].split(" ");
		                   
		            	   for(j=1; j<tokens3.length; j++)
			            	   retval[j-1]=Integer.parseInt(tokens3[j]);
		            	   //System.out.print(retval[j]);
	//	            	   }
		            	   //System.out.println();
		            	   if(keys>lm+1)mlchash.remove(new Integer(keys));
			    	}
			    	

			    }catch(Exception e){System.out.println("oops");}
*/		     
                          try{
                          Enumeration e = mlchash.keys();
                          while(e.hasMoreElements())
                          {
                                int keys = (Integer)e.nextElement();
                                String dags = (String) mlchash.get((Integer)keys);
                                if(keys==lm+1){
                                    String[] tokens = dags.split(" ");
                                    for(int j=1; j<tokens.length; j++)
                                    retval[j-1]=Integer.parseInt(tokens[j]);
                                  }
                                if(keys>lm+1)mlchash.remove((Integer)keys);
                          }

                         }
                         catch(Exception e){}

		        for(int node = 0; node < numOfNodes; node++)
						for (int parent = 0; parent < numOfNodes; parent++)
				//System.out.print(retval[node*numOfNodes+parent]);
										if (retval[node*numOfNodes+parent]>0)				
								parentTable[node].add(new Integer(parent));
							else				
								parentTable[node].remove(new Integer(parent));						    
	              lmprev = lm;
	
                      } // end of lmprev
		    } // end of check 
	}
	
	private void initializeNodes()
	{
		numOfNodes = data.getAttributes().size();
    int[] nodes = new int[numOfNodes];       
    for (int i = 0; i < numOfNodes; i++) nodes[i] = i;
    bbnGraph = populateNodes();
    path = new boolean[numOfNodes][numOfNodes];
    for (int j = 0; j < numOfNodes; j++)
    {
	    for (int i = 0; i < numOfNodes; i++)	        
  	  	path[i][j] = false;
    }
	} 
	
	private void initializeNodeParents()
	{
		parentTable = new Set[numOfNodes];
    for (int i = 0; i < numOfNodes; i++)
      parentTable[i] = new HashSet();
	}
	
	private void addParentsToGraph()
	{ 
		for (int node = 0; node < numOfNodes; node++)
		{
			BBNNode child = bbnNodes[node];
      for (Iterator j = parentTable[node].iterator(); j.hasNext();) 
      {
	       int idx = ((Integer) j.next()).intValue();
	        BBNNode parent = bbnNodes[idx];                
	        try {
	            bbnGraph.addEdge(parent, child);
	        } catch(Exception e) {
	            System.err.println("Error on adding parent "+parent);
	        }
      }
		}     
		
	}
	
	private void findClosure()
	{
		for (int i = 0 ; i < numOfNodes; i++)
		{
			for (int j =0; j < numOfNodes; j++)
			{			
				path[i][j] = false;
				path[i][j] = parentTable[i].contains(new Integer(j));
			}	
		}			
		for (int k = 0; k < numOfNodes; k ++)
		{
			for (int i = 0; i < numOfNodes; i++)
			{
				if (path[i][k])
				{		
					for (int j = 0; j < numOfNodes; j++)				
						path[i][j] = path[i][j]  ||  path [k][j] ;
				}		
			}
		}
	}
	
	private void displayParentTable()
	{
		for (int node = 0; node < numOfNodes; node++)		
			System.out.println("  node " + node +", parents "+ parentTable[node]);		
	}
	
	
	
	
	public static void main(String[] args)
	{
		
		ParameterTable params = Parameter.process(args);
        String inputFile = params.getString("-i");
        String outputFormat = params.getString("-f");
        String outputFile = params.getString("-o");    
        int maxParent = params.getInt("-k", defaultParentLimit);
        boolean quiet = params.getBool("-q");

        if (inputFile == null) {
            System.out.println("Usage: edu.ksu.cis.bnj.bbn.learning.k2.K2 -i:inputfile [-o:outputfile] [-f:outputformat] [-q] [-k:parentlimit] [-s:sequenceorder]");
            System.out.println("-f: default=xml. Acceptable values are {xml, net, bif, xbn}");
            System.out.println("-k: parent limit. Default="+defaultParentLimit);
            return;
        }
      

       try {
            Runtime r = Runtime.getRuntime();
            long origfreemem = r.freeMemory();
            long freemem;
            long origTime = System.currentTimeMillis();
            Table tuples = Table.load(inputFile);
            GreedySL greedyLearner = new GreedySL(tuples);
            //k2.setCalculateCPT(false);
            greedyLearner.setParentLimit(maxParent);            
            System.gc();
            long afterLoadTime = System.currentTimeMillis();
            freemem = r.freeMemory() - origfreemem;

            if (!quiet) {
                System.out.println("Memory needed after loading tuples = "+freemem);
                System.out.println("Loading time = "+((afterLoadTime - origTime) / 1000.0));
            }

            BBNGraph g = greedyLearner.getGraph();
            System.out.println(g);
            long learnTime = System.currentTimeMillis();
            freemem = r.freeMemory() - origfreemem;

            if (!quiet) {
                System.out.println("Memory needed after learning K2 = "+freemem);
                System.out.println("Learning time = "+((learnTime - afterLoadTime) / 1000.0));
            }

            if (outputFile != null) {
                if (outputFormat != null) {
                    g.save(outputFile, outputFormat);
                } else {
                    g.save(outputFile);
                }
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }		
	}
}
