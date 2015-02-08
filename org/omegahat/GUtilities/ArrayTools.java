

// line 206 "ArrayTools.jweb"
  package org.omegahat.GUtilities;


// line 212 "ArrayTools.jweb"
    import java.lang.reflect.Array;


// line 8 "ArrayTools.jweb"
public class ArrayTools
{
    
// line 20 "ArrayTools.jweb"
    /* none */

// line 11 "ArrayTools.jweb"
    
// line 25 "ArrayTools.jweb"
    
    /* none */

// line 12 "ArrayTools.jweb"
    
// line 33 "ArrayTools.jweb"
    /* none */

// line 13 "ArrayTools.jweb"
    
// line 39 "ArrayTools.jweb"
static public String arrayToString( Object contents)
{
    return arrayToString(contents, "[ ", " ", "] " );

}

static public String arrayToString( Object contents, 
				    String open, 
				    String sep,
				    String close)
{

  String contentsString = "" ;

  try
    {
      int len = Array.getLength( contents );
      
      contentsString += open;
      
      for(int j = 0; j < len ; j++)
	{
	  contentsString += arrayToString(Array.get(contents,j)) + sep;
	}
      
      contentsString += close;
    }
  catch ( Throwable e )
    {
	      contentsString = contents.toString();
    }
  
  
  return contentsString;
  
}



static public double[] Otod( Object in )
{
    if ( in instanceof double[] )
        return (double[]) in ;
    else if ( in instanceof Double[] )
        return Dtod( (Double[]) in );
    else 
        throw new ClassCastException("Cannot covert object of class " + in.getClass() + " to double[].");
}

static public Double[] dToD( double[] in )
{
      Double[] retval = new Double[in.length];
      for(int i=0; i< in.length; i++)
	  retval[i] = new Double(in[i]);

      return retval;
}

static public double[] Dtod( Double[] in )
{
      double[] retval = new double[in.length];
      for(int i=0; i< in.length; i++)
	  retval[i] = in[i].doubleValue();
      
      return retval;
}


static public float[] Otof( Object in )
{
    if ( in instanceof float[] )
        return (float[]) in ;
    else if ( in instanceof Float[] )
        return Ftof( (Float[]) in );
    else 
        throw new ClassCastException("Cannot covert object of class " + in.getClass() + " to double[].");
}

static public Float[] fToF( float[] in )
{
    Float[] retval = new Float[in.length];
    for(int i=0; i< in.length; i++)
      retval[i] = new Float(in[i]);

    return retval;
}

static public float[] Ftof( Float[] in )
{
    float[] retval = new float[in.length];
    for(int i=0; i< in.length; i++)
      retval[i] = in[i].floatValue();

    return retval;
}


static public int[] Otoi( Object in )
{
    if ( in instanceof int[] )
        return (int[]) in ;
    else if ( in instanceof Integer[] )
        return Itoi( (Integer[]) in );
    else 
        throw new ClassCastException("Cannot covert object of class " + in.getClass() + " to double[].");
}

static public Integer[] iToI( int[] in )
{
    Integer[] retval = new Integer[in.length];
    for(int i=0; i< in.length; i++)
      retval[i] = new Integer(in[i]);

    return retval;
}

static public int[] Itoi( Integer[] in )
{
    int[] retval = new int[in.length];
    for(int i=0; i< in.length; i++)
      retval[i] = in[i].intValue();

    return retval;
}



static public boolean[] Otob( Object in )
{
    if ( in instanceof boolean[] )
        return (boolean[]) in ;
    else if ( in instanceof Boolean[] )
        return Btob( (Boolean[]) in );
    else 
        throw new ClassCastException("Cannot covert object of class " + in.getClass() + " to double[].");
}

static public Boolean[] bToB( boolean[] in )
{
    Boolean[] retval = new Boolean[in.length];
    for(int i=0; i< in.length; i++)
      retval[i] = new Boolean(in[i]);

    return retval;
}

static public boolean[] Btob( Boolean[] in )
{
    boolean[] retval = new boolean[in.length];
    for(int i=0; i< in.length; i++)
      retval[i] = in[i].booleanValue();

    return retval;
}



// line 14 "ArrayTools.jweb"
    
// line 200 "ArrayTools.jweb"
    /* none */

// line 15 "ArrayTools.jweb"
}
