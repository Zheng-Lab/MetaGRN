

// line 80 "Utilities.jweb"
  package org.omegahat.GUtilities;


// line 86 "Utilities.jweb"
    import java.lang.reflect.Array;


// line 8 "Utilities.jweb"
public class Utilities
{
    
// line 20 "Utilities.jweb"
    /* none */

// line 11 "Utilities.jweb"
    
// line 25 "Utilities.jweb"
    
    /* none */

// line 12 "Utilities.jweb"
    
// line 33 "Utilities.jweb"
    /* none */

// line 13 "Utilities.jweb"
    
// line 39 "Utilities.jweb"
static public String arrayToString( Object contents )
{

  String contentsString = "" ;
       
  try
    {
      int len = Array.getLength( contents );
      
      contentsString += "[ ";
      
      for(int j = 0; j < len ; j++)
	{
	  contentsString += Array.get(contents,j) + " ";
	}
      
      contentsString += "] ";
    }
  catch ( Throwable e )
    {
	      contentsString = contents.toString();
    }
  
  
  return contentsString;
  
}


    /* none */

// line 14 "Utilities.jweb"
    
// line 74 "Utilities.jweb"
    /* none */

// line 15 "Utilities.jweb"
}
