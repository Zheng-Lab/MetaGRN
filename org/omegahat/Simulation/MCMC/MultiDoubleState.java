

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import java.util.ArrayList;
    import java.lang.reflect.Array;

    import org.omegahat.GUtilities.Distance;
    import org.omegahat.GUtilities.ArrayTools;


/**
 * MCMCState that can hold several Double[] states.  Its purpose is
 * to allow a multi-chain sampler to easily store and retrieve the
 * states of individual chains.  
*/
public class MultiDoubleState extends MultiState
{
    public static final int Mahalanobis = 0;
    public static final int Euclidean   = 1;

    
    /* inherited */
    // ArrayList contents;

    public boolean DEBUG;

    public double[][]       distanceCacheMat  = null;
    public double[][]       covCacheMat       = null;
    public double[]         meanCacheVec      = null;
    public CovAccumulator[] sumSqrSubsetCache = null;
    public double[]         minCache          = null;
    public double[]         maxCache          = null;
    public double[]         rangeCache        = null;

    
    public void  invalidateCache()
    {
        distanceCacheMat  = null;
        covCacheMat       = null;
        meanCacheVec      = null;
        sumSqrSubsetCache = null;
        minCache          = null;
        maxCache          = null;
        rangeCache        = null;
    }

    public void  invalidateCache( int index )
    {
        if (distanceCacheMat != null)
            for(int i=0; i < size(); i++)
                distanceCacheMat[index][i] = distanceCacheMat[i][index] = -1;
        
        covCacheMat       = null;
        meanCacheVec      = null;
        sumSqrSubsetCache = null;
        minCache          = null;
        maxCache          = null;
        rangeCache        = null;
    }



    public void set( int index, Object value ) 
    { 

        invalidateCache(index);
        super.set(index, ArrayTools.Otod(value) );
    }

    public void add( Object value )
    {
        invalidateCache();
        super.add( ArrayTools.Otod(value) );
    }

    public void remove( int which  )
    {
        invalidateCache();
        super.remove( which  );
    }


    

        public MultiDoubleState( int size )
        {
          super(size);
        }

        public MultiDoubleState( int size, Double[] oneValue )
        {
          super(size, ArrayTools.Dtod(oneValue) );
        }

        public MultiDoubleState( int size, double[] oneValue )
        {
          super(size, oneValue);
        }

        public MultiDoubleState()
        {
          super();
        }

    /**
     * This constructor creates a new MultDoubleState by duplicating
     * the contents of the provided MultiState, checking that each non-null
     * component is a Double[].  (If the component is a double[] convert it.)
     **/
    public MultiDoubleState( MultiState state )
    {
        copyFromTo(state,this);
    }



    
    /** creat a new MultiState object by copying the contents of this one.  **/
    public MultiState copy()
    {
        
        MultiDoubleState retval = new MultiDoubleState();

        return copyFromTo(this,retval);
    }

    public MultiDoubleState copyFromTo( MultiDoubleState source, MultiDoubleState target )
    {
        for(int i=0; i<source.size(); i++)
            target.add( source.get(i) );
        
        if(source.distanceCacheMat != null)
            {
                target.distanceCacheMat = new double[size()][size()];
                for(int i=0; i< size(); i++)
                    for(int j=0; j<=i; j++)
                        target.distanceCacheMat[i][j] = target.distanceCacheMat[j][j] = source.distanceCacheMat[i][j];
            }
        return target;
    }

    public MultiDoubleState copyFromTo( MultiState source, MultiDoubleState target )
    {
        for(int i=0; i < source.size(); i++)
            {
                Object tmp = source.get(i);
                
                try
                    {
                        if(tmp != null )
                            if( tmp instanceof double[] )
                                target.add( (double[]) tmp );
                            else if (tmp instanceof Double[] )
                                target.add( ArrayTools.Dtod( (Double[]) tmp )) ;
                            else
                                target.add( null );
                    }
                catch (ClassCastException e)
                    {
                        throw new ClassCastException( " Error converting MultiState to MutliDoubleState: \n" +
                                                      " MultiState contains an object that is not a Double[] or double[]." );
                    }
            }

        return target;
    }


    
    /** Compute the (local) covariance matrix at one state using a specified number of nearest neighbors 
     *  @param which   index of state around which covariance is computed
     *  @param howmany number of neighbors to use (Use nn=0 for all neighbors).
     @  @param method  method used to compute distances (0=Mahalanobis, 1=euclidean)
     *  
     * Note that this covariance matrix is the *local* covaraince matrix.
     * To be used as a "robust" method for estimating a *global*
     * covariance matrix, it should be scaled up by the factor 
     * (Total # Obs)/(howmany) .
     **/
    public double[][] nearestNeighborVar( int which, int howmany, int method )
    {

        if (howmany<2)
            throw new RuntimeException("nearestNeighborVar() requiers at least 2 neighbors.");


        int closest[] = findClosest( which, howmany, method );
                
        CovAccumulator accum = cumSumSqrSubsetCache(which,closest);
        double[][] retval = new double[accum.dim][accum.dim];
        
        for(int i=0; i < accum.dim; i++) 
            for(int j=0; j < accum.dim; j++)
               //** (E[XY] - E[X]E[Y]) / n-1 **//
               retval[i][j] = ( accum.cummat[i][j] / (double) ( accum.nobs - 1) ) -
                              ( accum.cumsum[i]    / (double) ( accum.nobs      )  *  
                                accum.cumsum[j]    / (double) ( accum.nobs - 1  ) );

        return retval;
    }

    public double[][] nearestNeighborDiagVar( int which, int howmany, int method )
    {

        if (howmany<2)
            throw new RuntimeException("nearestNeighborVar() requiers at least 2 neighbors.");

        int closest[] = findClosest( which, howmany, method );
                
        CovAccumulator accum = cumSumSqrSubsetCache(which,closest);
        double[][] retval = new double[accum.dim][accum.dim];
        
        for(int i=0; i < accum.dim; i++) 
               //** (E[XY] - E[X]E[Y]) / n-1 **//
               retval[i][i] = ( accum.cummat[i][i] / (double) ( accum.nobs - 1) ) -
                              ( accum.cumsum[i]    / (double) ( accum.nobs      )  *  
                                accum.cumsum[i]    / (double) ( accum.nobs - 1  ) );

        return retval;
    }

    public double[] nearestNeighborMean( int which, int howmany, int method )
    {

        if (howmany<2)
            throw new RuntimeException("nearestNeighborVar() requiers at least 2 neighbors.");

        int closest[] = findClosest( which, howmany, method );
                
        CovAccumulator accum = cumSumSqrSubsetCache(which,closest);
        double[] retval = new double[accum.dim];
        
        for(int i=0; i < accum.dim; i++) 
           retval[i] = accum.cumsum[i] / ((double) accum.nobs ) ;

        return retval;
    }






    /** Find closest <code>howmany</code> points to state
     * <code>which</code> using distance method <which>.  
     *
     * Note that the
     * state <code>which</code> will be included in the returned list as
     * the first element.
     *
     * @param which   index of state from which distances are computed.
     * @param howmany number of points (including which) to return, must be >= 2.
     * @param method  distance method to use. 0=Mahalanobis, 1=Euclidean.
     *
    **/

    public int[] findClosest( int which, int howmany, int method  )
    {
        /* first, compute distances between this point and all other states */
        double[] distances = computeDistances( which, method );

        /* find the howmany closest */
        int[]    closest_index    = new int[howmany];      /* indexes of smallest howmany distances 
                                                              (sorted by distance) */
        double[] closest_distance = new double[howmany];   /* corresponding distances  */

        for(int i = 0;  i < howmany ; i++)
        {
            closest_index[i] = -1;
            closest_distance[i] = Double.POSITIVE_INFINITY;

            // run one pass looking for jth smallest entry
            for(int j=0; j < size() ; j++) 
                {
                    if ( distances[j] < closest_distance[i] )
                        {
                            closest_index[i] = j;
                            closest_distance[i] = distances[j];
                        }
                }
            // remove the smallest entry from future consideration
            distances[closest_index[i]] = Double.POSITIVE_INFINITY;

        }

        return closest_index;
    }



    public double[] computeDistances( int from, int method )
    {
        double[] distances = new double[size()];
        for( int to =0; to < size(); to++ )
            distances[to] = distanceCache(from,to,method);

        return distances;
    }

    public double distanceCache(int from, int to, int method)
    {
        if(distanceCacheMat==null) 
            {
                distanceCacheMat = new double[size()][size()];
                for(int i=0; i< size(); i++)
                    for(int j=0; j<=i; j++)
                        distanceCacheMat[i][j] = distanceCacheMat[j][i] = Double.NaN;
            }

        if( Double.isNaN(distanceCacheMat[from][to]) ) 
            return distanceCacheMat[from][to] = distance(from,to,method);
        else
            return distanceCacheMat[from][to];
    }



    public double distance( int from, int to, int method )
    {
        if(DEBUG)
            {
                System.err.println("from=" + from + "==>" + ArrayTools.arrayToString(this.get(from) ));
                System.err.println("to  =" + to   + "==>" + ArrayTools.arrayToString(this.get(to  ) ));
            }


        double[] fromState = ArrayTools.Otod(this.get(from));
        double[] toState   = ArrayTools.Otod(this.get(to));

        double retval;

        switch (method)
            {       
            case Mahalanobis:
                retval = Distance.mahalanobis( fromState, toState, var() );
                break;
            case Euclidean:
                retval = Distance.euclidean( fromState, toState );
                break;
            default:
                throw new RuntimeException("Unknown or invalid distance method");
            }

        if (DEBUG) System.err.println("Distance=" + retval );
        return retval;
    }



    public CovAccumulator cumSumSqrSubset( int[] closest )
    {

      int dim = Array.getLength( this.get(0) );
      CovAccumulator retval = new CovAccumulator( dim );

      for(int obs=0; obs < closest.length; obs++)
      {
          retval.nobs++;

          double[] current = (double[]) this.get(closest[obs]);

          for(int i=0; i < dim; i++)
              {
                  retval.cumsum[i] += current[i];
                  
                  for(int j=0; j < dim; j++)
                      retval.cummat[i][j] += current[i] * current[j];
              }
      }

      return retval;
    }

    public CovAccumulator cumSumSqrSubsetCache(int which,  int[] closest )
    {
        if(sumSqrSubsetCache!=null && sumSqrSubsetCache[which]!=null)
            return sumSqrSubsetCache[which];
        else
            {
                if (sumSqrSubsetCache==null)
                    sumSqrSubsetCache = new CovAccumulator[this.size()];

                return sumSqrSubsetCache[which] = cumSumSqrSubset( closest );
            }
    }
        






    //public double distance_d( int i, int j )
    //{
    //  for(





    /** Compute the minimum 2-way variance vector between a specified state and the other states **/
    /* public Double[] minvar( int center ) */
    /* { */
    /*   Double[] tmp = (Double[]) this.get(0); */

    /*   double[]   cumsum = new double[ tmp.length ];   // for means  */
    /*   double[][] cummat = new double[ tmp.length ][ tmp.length ]; // for sums of squares */
    /*   Double[][] retval = new Double[ tmp.length ][ tmp.length ]; */

    /*   for(int i=0; i < tmp.length; i++) */
    /*   { */
    /*     cumsum[i] = 0.0; */
        
    /*     for(int j=0; j < tmp.length; j++) */
    /*       cummat[i][j] = 0.0; */
    /*   } */

    /*   for(int obs=0; obs < this.size(); obs++) */
    /*   { */
    /*     Double[] current = (Double[]) this.get(obs); */

    /*     for(int i=0; i < tmp.length; i++) */
    /*     { */
    /*       cumsum[i] += current[i].doubleValue(); */

    /*       for(int j=0; j < tmp.length; j++) */
    /*         cummat[i][j] += current[i].doubleValue() * current[j].doubleValue(); */
    /*     } */
    /*   } */
      
    /*   for(int i=0; i < tmp.length; i++)  */
    /*   for(int j=0; j < tmp.length; j++) */
    /*     // (E[XY] - E[X]E[Y]) / n-1  */
    /*     retval[i][j] = new Double( ( cummat[i][j] / (double) ( this.size() - 1) ) - */
    /*                                ( cumsum[i]  / (double) ( this.size() )  *  */
    /*                                  cumsum[j]  / (double) ( this.size() - 1  ) ) */
    /*                                ); */

    /*   return retval; */
    /* } */


    



    public double[] min()
    {
        if(minCache==null)
            computeMinMaxRange();

        return minCache;
    }

    public double[] max()
    {
        if(maxCache==null)
            computeMinMaxRange();

        return maxCache;
    }

    public double[] range()
    {
        if(rangeCache==null)
            computeMinMaxRange();

        return rangeCache;
    }


    protected void computeMinMaxRange()
    {
        int dim = Array.getLength( this.get(0) );
        
        minCache   = new double[ dim ];
        maxCache   = new double[ dim ];
        rangeCache = new double[ dim ];
        
        // initialize 
        for(int j=0; j < dim; j++)
            {
                minCache[j] = Double.POSITIVE_INFINITY;
                maxCache[j] = Double.NEGATIVE_INFINITY;
            }
        
        
        // compute min and max
        for(int i=0; i < this.size(); i++)
            {
                double[] current = (double[]) this.get(i);
                
                for(int j=0; j < dim; j++)
                    {
                        if(current[j] < minCache[j])
                            minCache[j] = current[j];
                        
                        if(current[j] > maxCache[j])
                            maxCache[j] = current[j];
                    }
            }
        
        // compute range
        for(int j=0; j < dim; j++)
            {
                rangeCache[j] = maxCache[j] - minCache[j];
            }

        return;
    }



    /** Compute accumulated sums **/

    public double[] cumsum( double[] cumsum )
    {
      int dim = Array.getLength( this.get(0) );

      for(int i=0; i < this.size(); i++)
      {
        double[] current = (double[]) this.get(i);

        for(int j=0; j < dim; j++)
            cumsum[j] += current[j];
      }
      
      return cumsum;
    }
     

    public double[] cumsum()
    {
      int dim = Array.getLength( this.get(0) );


      double[] cumsum = new double[ dim ];

      for(int j=0; j < dim; j++)
        cumsum[j] = 0.0;

      return cumsum( cumsum );
    }
     



    /** Compute the mean **/
    public double[] mean()
    {
        if(meanCacheVec != null)
          return meanCacheVec;
        else
          {

            int dim = Array.getLength( this.get(0) );

            double[] retval = new double[ dim ];
            
            double[] cumsum = cumsum();
            
            for(int j=0; j < dim; j++)
              retval[j] = cumsum[j] / (double) this.size();

            meanCacheVec = retval;
          
            return retval;
          }
    }


    public Double[] Mean()
    {

        double[] dmat = mean();
        Double[] retval = new Double[ dmat.length ];

        for(int j=0; j < dmat.length; j++)
           retval[j] = new Double( dmat[j] );

        return retval;
    }

    /***************************************/
    /** Compute/Update Covariance Matrix  **/
    /***************************************/

    public class CovAccumulator
    {
        public double[][] cummat;
        public double[]   cumsum;
        public int        dim;
        public int        nobs;

        public CovAccumulator( int dim )
        {
            this.dim = dim;
            this.nobs = 0;
            this.cummat = new double[dim][dim];
            this.cumsum = new double[dim];
            
            for(int i=0; i < dim; i++)
                {
                    cumsum[i] = 0.0;
                    
                    for(int j=0; j < dim; j++)
                        cummat[i][j] = 0.0;
                }
        }
    }

    public CovAccumulator cumSumSqr()
    {

      int dim = Array.getLength( this.get(0) );

      CovAccumulator retval = new CovAccumulator( dim  );

      return cumSumSqr( retval );
    }

    public CovAccumulator cumSumSqr( CovAccumulator retval )
    {

      int dim = Array.getLength( this.get(0) );

      for(int obs=0; obs < this.size(); obs++)
      {
          retval.nobs++;

          double[] current = (double[]) this.get(obs);

          for(int i=0; i < dim; i++)
              {
                  retval.cumsum[i] += current[i];
                  
                  for(int j=0; j < dim; j++)
                      retval.cummat[i][j] += current[i] * current[j];
              }
      }

      return retval;
    }

    /** Compute the diagonal of the covariance matrix **/
    public double[][] diagVar()
    {
      if( covCacheMat != null) 
        return covCacheMat;
      else
        {
              
          CovAccumulator accum = cumSumSqr();
          double[][] retval = new double[accum.dim][accum.dim];

          double[] means = mean();
          int n = accum.nobs;
          
          for(int i=0; i < accum.dim; i++) 
              // {  Sum( X*Y ) - (n * mean(X) * mean(Y) ) } / (n - 1)
              retval[i][i] = (( accum.cummat[i][i]  - ((double) n) * means[i]*means[i] )) /  ((double) n - 1.0) ;

          covCacheMat = retval;
          return retval;

        }
    }




    /** Compute the covariance matrix **/
    public double[][] var()
    {
      if( covCacheMat != null) 
        return covCacheMat;
      else
        {
              
          CovAccumulator accum = cumSumSqr();
          double[][] retval = new double[accum.dim][accum.dim];

          double[] means = mean();
          int n = accum.nobs;
          
          for(int i=0; i < accum.dim; i++) 
          for(int j=0; j < accum.dim; j++)
              // {  Sum( X*Y ) - (n * mean(X) * mean(Y) ) } / (n - 1)
              retval[i][j] = (( accum.cummat[i][j]  - ((double) n) * means[i]*means[j] )) /  ((double) n - 1.0) ;

          covCacheMat = retval;
          return retval;

        }
    }

    /**
     * This function checks if the number of component states used to
     * estimate the covariance matrix is less than or equal to the number
     * of dimensions.  If so, it inflates the diagonal elements by 5% so
     * that the covariance matrix will be invertible.
    **/

    public double[][] correctedVar()
    {
      double[][] var = var();
      int dim = var.length;
      
      if( this.size() < dim+1 )
      {
        for(int i=0; i < dim; i++)
          {
            double max = 0.0;
            
            for(int j=0; j< i; j++)
                if ( Math.abs(var[i][j]*1.05) > var[i][i] )   
                  var[i][i] = Math.abs(var[i][j]*1.05);
          }
      }
      
      return var;
    }



    /** Compute the covariance matrix **/
    static public double[][] accumToVar( CovAccumulator accum )
    {
        double[][] retval = new double[accum.dim][accum.dim];
        
        for(int i=0; i < accum.dim; i++) 
            for(int j=0; j < accum.dim; j++)
               //** (E[XY] - E[X]E[Y]) / n-1 **//
               retval[i][j] = ( accum.cummat[i][j] / (double) ( accum.nobs - 1) ) -
                              ( accum.cumsum[i]    / (double) ( accum.nobs      )  *  
                                accum.cumsum[j]    / (double) ( accum.nobs - 1  ) );

      return retval;
    }



    /** Compute the covariance matrix **/
    public Double[][] Var()
    {
        double[][] dmat   = var();
        Double[][] retval = new Double[ dmat.length ][ dmat.length ];

        for(int i=0; i < dmat.length; i++)
            for(int j=0; j < dmat.length; j++)
                retval[i][j] = new Double(dmat[i][j]);

        return retval;
    }

    /******************************/
    /** Compute/Update Histogram **/
    /******************************/

    public Histogram[] histogram( Histogram[] hist )
    {
      // iterate over states 
      for(int i=0; i<this.size(); i++)
      {
        double[] data = ArrayTools.Otod(this.get(i));

        // iterate over dimensions
        for(int j=0; j< data.length; j++)
        {
          hist[j].update( data[j] );
        }
      }

      return hist;
    }



    public Histogram[] histogram(double min, double max, int bins )
    {

      int dim = Array.getLength( this.get(0) );
      Histogram[] histVec = new Histogram[dim];
      
      for(int i=0; i< dim; i++)
        histVec[i] = new Histogram(min, max, bins);

      return this.histogram(histVec);
    }



    // public Double[] toDoubleArr( double[] source )
    // {
    //   Double[] retval = new Double[source.length];
    //   for(int i=0; i< source.length; i++)
    //      retval[i] = new Double(source[i]);

    //   return retval;
    // }


    // public Double[] toDoubleArr( double source )
    // {
    //      Double[] retval = new Double[1];
    //      retval[0] = new Double(source);
    //      return retval;
    // }

    // public Double[] toDoubleArr( Double source )
    // {
    //      Double[] retval = new Double[1];
    //      retval[0] = source;
    //      return retval;
    // }

    
        /* none */

}
