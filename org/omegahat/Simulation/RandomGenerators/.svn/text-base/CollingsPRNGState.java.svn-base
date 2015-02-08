// line 2 "CollingsPRNGState.jweb"
package org.omegahat.Simulation.RandomGenerators;


/**
 * Encapsulates the state of a CollingsPRNG 
 */
public class CollingsPRNGState implements PRNGIntegerState 
{


// line 27 "CollingsPRNGState.jweb"
  /** Class this State corresponds to. */
  String PRNGName = 
  "org.omegahat.Simulation.RandomGenerators.CollingsPRNG";
  
  /** Number of component generators */
  int NumGen;
  
  /** Multiplier constant for mixing generator */
  int MixerMult;
  
  /** Seed for mixing generator */
  int MixerSeed;
  
  /** Multiplier constant for component generators */
  int ComponentMult[]; 
  
  /** Seed for component generators */
  int ComponentSeed[];

// line 116 "CollingsPRNGState.jweb"
  int[] integerState;
  int[] integerStateHash;

// line 49 "CollingsPRNGState.jweb"
  int getNumGen()
    {
      return NumGen;
    }

  int getMixerMult()
    {
      return MixerMult;
    }
  
  int getMixerSeed()
    {
      return MixerSeed;
    }

  int[] getComponentMult()
    {
      int[] retval = new int[ComponentMult.length];
      System.arraycopy( ComponentMult, 0,
			retval, 0,
			NumGen );
      return retval;
    }
			

  int[] getComponentSeed()
    {
      int[] retval = new int[ComponentSeed.length];
      System.arraycopy( ComponentSeed, 0,
			retval, 0,
			NumGen );
      return retval;
    }


// line 88 "CollingsPRNGState.jweb"
public CollingsPRNGState()
{
}

public CollingsPRNGState( int[] intState, int[] hash )
{
  setIntegerState(intState, hash);
}  


// line 103 "CollingsPRNGState.jweb"
  
  /** 
   *  Returns the name of the class that can be enstantiated from this state. 
   */
    public String getPRNGName()
    {
    return PRNGName;
    }

// line 122 "CollingsPRNGState.jweb"
  public int[] getIntegerState()
    {
      integerState = new int[NumGen * 3 + 3];
      integerState[0] = NumGen;
      integerState[1] = MixerMult;
      integerState[2] = MixerSeed;
      System.arraycopy( ComponentMult, 0,
			integerState, 3,
			NumGen );

      System.arraycopy( ComponentMult, 0,
			integerState, NumGen + 3,
			NumGen  );
			
      integerStateHash = makeIntegerStateHash(integerState);

      return(integerState);
      
    }

  public int[] getIntegerStateHash()
    {
      if(integerState==null)
         integerState = getIntegerState();

      return integerStateHash;
    }

  public boolean hashEquals( int[] hash1, int hash2[] )
  { 
      if(hash1.length!=hash2.length) return false;
      for(int i=0; i<hash1.length; i++)
	  if(hash1[i]!=hash2[i]) return false;
      return true;
  }

  public void setIntegerState(  int[] intState, int[] hash )
    {
      
      if( !hashEquals( hash, makeIntegerStateHash(intState) ))
	throw new RuntimeException("IntegerState does not match Hash");

      NumGen    = intState[0];
      MixerMult = intState[1];
      MixerSeed = intState[2];
      
      ComponentMult = new int[NumGen];
      System.arraycopy(intState,  3,
		       ComponentMult, 0,
		       NumGen);

      ComponentSeed = new int[NumGen];
      System.arraycopy(intState,  NumGen + 3,
		       ComponentSeed, 0,
		       NumGen);

    }      
   
  protected int[] makeIntegerStateHash( int[] intState )
   {
     int sum=123456789;
     int[] retval = new int[1];

     for(int i = 1; i < intState.length; i++)
	sum += intState[i];

     retval[0] = sum;

     return retval;
   }



// line 21 "CollingsPRNGState.jweb"
}

