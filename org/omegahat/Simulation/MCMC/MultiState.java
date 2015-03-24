

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import java.lang.reflect.Array;
    import java.util.*;


/**
 * MCMCState that can hold several individual states.  Its purpose is
 * to allow a multi-chain sampler to easily store and retrieve the
 * states of individual chains.  
*/
public class MultiState implements MCMCState
{
    
    protected Vector    contents;
    protected Hashtable attributes;

    
    public int       size()                         { return contents.size(); }
    public Object    get( int index )               { return (Object) contents.get( index ); }
    public void      set( int index, Object value ) { contents.set(index,value); }
    public void      add( Object value )            { contents.add(value); }
    public void remove( int which  )                { contents.remove( which  ); }

    //public Hashtable attributes()                 { return attributes; }
    public Object   attr( String name )               
    { 
        if (attributes==null) 
            return null;
        else 
            return attributes.get(name);
    }

    public Object   attr( String name, Object value ) 
    {
        if (attributes==null) 
            attributes = new Hashtable(10);

        return attributes.put(name,value);
    }

    public String[] attributeNames()
    {
        String[] retval;
        
        if(attributes==null)
            return new String[0];
        else
        {
            retval = new String[ attributes.size() ];
            int index=0;
            for (Enumeration e = attributes.elements() ; e.hasMoreElements() ;) 
                {
                    retval[index++] = (String) e.nextElement();
                }
        }
        
        return retval;
    }


    //public MCMCState get( int index ) { return (MCMCState) contents.get( index ); }
    //public void      set( int index, MCMCState value ) { contents.set(index,value); }
    //public void      add( MCMCState value ) { contents.add(value); }

    
        public MultiState( int size )
        {
          contents = new Vector(size);
        }

    //    public MultiState( int size, MCMCState oneValue )
        public MultiState( int size, Object oneValue )
        {
           contents = new Vector(size);
           for(int i=0; i<size; i++)
             contents.add( oneValue );
        }

        public MultiState()
        {
          contents = new Vector();
        }



    
        public String toString()
        {
          Object tmp;

            String retval = "MultiState:\n ";
            for( int i = 0; i < contents.size(); i++)
            {
              
              tmp = contents.get( i );

              String contentsString = "";
                  
              try
                {
                  int len = Array.getLength( tmp );
                  
                  contentsString += "[ ";

                  for(int j = 0; j < len ; j++)
                    {
                      contentsString += Array.get(tmp,j) + " ";
                    }

                  contentsString += "] ";
                }
              catch ( Throwable e )
                {
                  contentsString = tmp.toString();
                }
              
              
              retval += "\t Chain " + i + " : " + contentsString + "\n";
            }
          retval = retval.substring(0, retval.length()-2);
          return retval;
        }



    /** creat a new MultiState object by copying the contents of this one.  **/
    public MultiState copy()
    {
        MultiState retval = new MultiState();

        this.copyTo(retval);

        return retval;

    }

    public void copyTo( MultiState target )
    {
        for(int i=0; i<this.size(); i++)
            target.add( this.get(i) );
    }


    
        /* none */

}
