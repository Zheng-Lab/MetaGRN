/* Generated By:JJTree&JavaCC: Do not edit this line. DSLParserTokenManager.java */
package edu.ksu.cis.bnj.bbn.converter.dsl;
/*
 * 
 * This file is part of Bayesian Network for Java (BNJ).
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

/**
 * @author: Roby Joehanes
 */
import edu.ksu.cis.kdd.util.parser.*;
public class DSLParserTokenManager implements DSLParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x400000000L) != 0L)
            return 4;
         if ((active0 & 0xc000L) != 0L)
         {
            jjmatchedKind = 23;
            return 13;
         }
         if ((active0 & 0xe3bfc00000000080L) != 0L || (active1 & 0x11eL) != 0L)
            return 15;
         if ((active0 & 0x1800101000000000L) != 0L || (active1 & 0xc0L) != 0L)
            return 26;
         if ((active0 & 0x40000000000240L) != 0L || (active1 & 0x1L) != 0L)
            return 51;
         if ((active0 & 0x400082000000000L) != 0L || (active1 & 0x20L) != 0L)
            return 20;
         return -1;
      case 1:
         if ((active0 & 0x1800000000000000L) != 0L || (active1 & 0xc0L) != 0L)
            return 27;
         if ((active0 & 0xc000L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 23;
               jjmatchedPos = 1;
            }
            return 13;
         }
         if ((active0 & 0x200L) != 0L)
            return 49;
         if ((active0 & 0x400000000000000L) != 0L || (active1 & 0x20L) != 0L)
            return 15;
         return -1;
      case 2:
         if ((active0 & 0x4000L) != 0L)
            return 13;
         if ((active0 & 0x1000000000000000L) != 0L || (active1 & 0x80L) != 0L)
            return 15;
         if ((active0 & 0x8000L) != 0L)
         {
            if (jjmatchedPos != 2)
            {
               jjmatchedKind = 23;
               jjmatchedPos = 2;
            }
            return 13;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0, long active1)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 9:
         return jjStopAtPos(0, 2);
      case 10:
         return jjStopAtPos(0, 3);
      case 12:
         return jjStopAtPos(0, 5);
      case 13:
         return jjStopAtPos(0, 4);
      case 32:
         return jjStopAtPos(0, 1);
      case 33:
         jjmatchedKind = 38;
         return jjMoveStringLiteralDfa1_0(0x200000000000L, 0x0L);
      case 37:
         jjmatchedKind = 7;
         return jjMoveStringLiteralDfa1_0(0x0L, 0x10L);
      case 38:
         jjmatchedKind = 55;
         return jjMoveStringLiteralDfa1_0(0x800000000000L, 0x2L);
      case 40:
         return jjStopAtPos(0, 26);
      case 41:
         return jjStopAtPos(0, 27);
      case 42:
         jjmatchedKind = 53;
         return jjMoveStringLiteralDfa1_0(0x8000000000000000L, 0x0L);
      case 43:
         jjmatchedKind = 51;
         return jjMoveStringLiteralDfa1_0(0x2002000000000000L, 0x0L);
      case 44:
         return jjStopAtPos(0, 33);
      case 45:
         jjmatchedKind = 52;
         return jjMoveStringLiteralDfa1_0(0x4004000000000000L, 0x100L);
      case 46:
         return jjStartNfaWithStates_0(0, 34, 4);
      case 47:
         jjmatchedKind = 54;
         return jjMoveStringLiteralDfa1_0(0x240L, 0x1L);
      case 58:
         return jjStopAtPos(0, 41);
      case 59:
         return jjStopAtPos(0, 32);
      case 60:
         jjmatchedKind = 37;
         return jjMoveStringLiteralDfa1_0(0x400080000000000L, 0x20L);
      case 61:
         jjmatchedKind = 35;
         return jjMoveStringLiteralDfa1_0(0x40000000000L, 0x0L);
      case 62:
         jjmatchedKind = 36;
         return jjMoveStringLiteralDfa1_0(0x1800100000000000L, 0xc0L);
      case 63:
         return jjStopAtPos(0, 40);
      case 91:
         return jjStopAtPos(0, 30);
      case 93:
         return jjStopAtPos(0, 31);
      case 94:
         jjmatchedKind = 57;
         return jjMoveStringLiteralDfa1_0(0x1000000000000L, 0x8L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0xc000L, 0x0L);
      case 123:
         return jjStopAtPos(0, 28);
      case 124:
         jjmatchedKind = 56;
         return jjMoveStringLiteralDfa1_0(0x400000000000L, 0x4L);
      case 125:
         return jjStopAtPos(0, 29);
      case 126:
         return jjStopAtPos(0, 39);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0, long active1)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0, active1);
      return 1;
   }
   switch(curChar)
   {
      case 38:
         if ((active0 & 0x800000000000L) != 0L)
            return jjStopAtPos(1, 47);
         break;
      case 42:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(1, 9, 49);
         break;
      case 43:
         if ((active0 & 0x2000000000000L) != 0L)
            return jjStopAtPos(1, 49);
         break;
      case 45:
         if ((active0 & 0x4000000000000L) != 0L)
            return jjStopAtPos(1, 50);
         break;
      case 47:
         if ((active0 & 0x40L) != 0L)
            return jjStopAtPos(1, 6);
         break;
      case 60:
         if ((active0 & 0x400000000000000L) != 0L)
         {
            jjmatchedKind = 58;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x20L);
      case 61:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStopAtPos(1, 42);
         else if ((active0 & 0x80000000000L) != 0L)
            return jjStopAtPos(1, 43);
         else if ((active0 & 0x100000000000L) != 0L)
            return jjStopAtPos(1, 44);
         else if ((active0 & 0x200000000000L) != 0L)
            return jjStopAtPos(1, 45);
         else if ((active0 & 0x2000000000000000L) != 0L)
            return jjStopAtPos(1, 61);
         else if ((active0 & 0x4000000000000000L) != 0L)
            return jjStopAtPos(1, 62);
         else if ((active0 & 0x8000000000000000L) != 0L)
            return jjStopAtPos(1, 63);
         else if ((active1 & 0x1L) != 0L)
            return jjStopAtPos(1, 64);
         else if ((active1 & 0x2L) != 0L)
            return jjStopAtPos(1, 65);
         else if ((active1 & 0x4L) != 0L)
            return jjStopAtPos(1, 66);
         else if ((active1 & 0x8L) != 0L)
            return jjStopAtPos(1, 67);
         else if ((active1 & 0x10L) != 0L)
            return jjStopAtPos(1, 68);
         break;
      case 62:
         if ((active0 & 0x800000000000000L) != 0L)
         {
            jjmatchedKind = 59;
            jjmatchedPos = 1;
         }
         else if ((active1 & 0x100L) != 0L)
            return jjStopAtPos(1, 72);
         return jjMoveStringLiteralDfa2_0(active0, 0x1000000000000000L, active1, 0xc0L);
      case 94:
         if ((active0 & 0x1000000000000L) != 0L)
            return jjStopAtPos(1, 48);
         break;
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000L, active1, 0L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x8000L, active1, 0L);
      case 124:
         if ((active0 & 0x400000000000L) != 0L)
            return jjStopAtPos(1, 46);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0, active1);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1)
{
   if (((active0 &= old0) | (active1 &= old1)) == 0L)
      return jjStartNfa_0(0, old0, old1); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0, active1);
      return 2;
   }
   switch(curChar)
   {
      case 61:
         if ((active1 & 0x20L) != 0L)
            return jjStopAtPos(2, 69);
         else if ((active1 & 0x40L) != 0L)
            return jjStopAtPos(2, 70);
         break;
      case 62:
         if ((active0 & 0x1000000000000000L) != 0L)
         {
            jjmatchedKind = 60;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x80L);
      case 100:
         return jjMoveStringLiteralDfa3_0(active0, 0x8000L, active1, 0L);
      case 116:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(2, 14, 13);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0, active1);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1)
{
   if (((active0 &= old0) | (active1 &= old1)) == 0L)
      return jjStartNfa_0(1, old0, old1); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0, active1);
      return 3;
   }
   switch(curChar)
   {
      case 61:
         if ((active1 & 0x80L) != 0L)
            return jjStopAtPos(3, 71);
         break;
      case 101:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(3, 15, 13);
         break;
      default :
         break;
   }
   return jjStartNfa_0(2, active0, active1);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec3 = {
   0x1ff00000fffffffeL, 0xffffffffffffc000L, 0xffffffffL, 0x600000000000000L
};
static final long[] jjbitVec4 = {
   0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL
};
static final long[] jjbitVec5 = {
   0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec6 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffL, 0x0L
};
static final long[] jjbitVec7 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0x0L, 0x0L
};
static final long[] jjbitVec8 = {
   0x3fffffffffffL, 0x0L, 0x0L, 0x0L
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 52;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 27:
                  if (curChar == 62)
                     jjCheckNAdd(15);
                  else if (curChar == 61)
                  {
                     if (kind > 73)
                        kind = 73;
                  }
                  break;
               case 51:
                  if (curChar == 61)
                  {
                     if (kind > 73)
                        kind = 73;
                  }
                  else if (curChar == 42)
                     jjstateSet[jjnewStateCnt++] = 49;
                  break;
               case 26:
                  if (curChar == 62)
                     jjstateSet[jjnewStateCnt++] = 27;
                  if (curChar == 62)
                     jjCheckNAdd(15);
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(0, 6);
                  else if (curChar == 47)
                     jjCheckNAddTwoStates(51, 15);
                  else if (curChar == 62)
                     jjAddStates(7, 8);
                  else if (curChar == 38)
                     jjCheckNAdd(15);
                  else if (curChar == 60)
                     jjstateSet[jjnewStateCnt++] = 20;
                  else if (curChar == 45)
                     jjCheckNAdd(15);
                  else if (curChar == 43)
                     jjCheckNAdd(15);
                  else if (curChar == 37)
                     jjCheckNAdd(15);
                  else if (curChar == 42)
                     jjCheckNAdd(15);
                  else if (curChar == 61)
                  {
                     if (kind > 73)
                        kind = 73;
                  }
                  else if (curChar == 36)
                  {
                     if (kind > 23)
                        kind = 23;
                     jjCheckNAdd(13);
                  }
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(10, 11);
                  else if (curChar == 46)
                     jjCheckNAdd(4);
                  if ((0x3fe000000000000L & l) != 0L)
                  {
                     if (kind > 16)
                        kind = 16;
                     jjCheckNAddTwoStates(1, 2);
                  }
                  else if (curChar == 48)
                  {
                     if (kind > 16)
                        kind = 16;
                     jjCheckNAddStates(9, 11);
                  }
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAddTwoStates(1, 2);
                  break;
               case 3:
                  if (curChar == 46)
                     jjCheckNAdd(4);
                  break;
               case 4:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddStates(12, 14);
                  break;
               case 6:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(7);
                  break;
               case 7:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddTwoStates(7, 8);
                  break;
               case 9:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(10, 11);
                  break;
               case 10:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(10, 11);
                  break;
               case 11:
                  if (curChar == 34 && kind > 22)
                     kind = 22;
                  break;
               case 12:
                  if (curChar != 36)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(13);
                  break;
               case 13:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(13);
                  break;
               case 14:
                  if (curChar == 61 && kind > 73)
                     kind = 73;
                  break;
               case 15:
                  if (curChar == 61 && kind > 73)
                     kind = 73;
                  break;
               case 16:
                  if (curChar == 42)
                     jjCheckNAdd(15);
                  break;
               case 17:
                  if (curChar == 37)
                     jjCheckNAdd(15);
                  break;
               case 18:
                  if (curChar == 43)
                     jjCheckNAdd(15);
                  break;
               case 19:
                  if (curChar == 45)
                     jjCheckNAdd(15);
                  break;
               case 20:
                  if (curChar == 60)
                     jjCheckNAdd(15);
                  break;
               case 21:
                  if (curChar == 60)
                     jjstateSet[jjnewStateCnt++] = 20;
                  break;
               case 22:
                  if (curChar == 38)
                     jjCheckNAdd(15);
                  break;
               case 25:
                  if (curChar == 62)
                     jjAddStates(7, 8);
                  break;
               case 28:
                  if (curChar == 62)
                     jjstateSet[jjnewStateCnt++] = 27;
                  break;
               case 29:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(0, 6);
                  break;
               case 30:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(30, 31);
                  break;
               case 31:
                  if (curChar != 46)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddStates(15, 17);
                  break;
               case 32:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddStates(15, 17);
                  break;
               case 34:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(35);
                  break;
               case 35:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddTwoStates(35, 8);
                  break;
               case 36:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(36, 37);
                  break;
               case 38:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(39);
                  break;
               case 39:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddTwoStates(39, 8);
                  break;
               case 40:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(18, 20);
                  break;
               case 42:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(43);
                  break;
               case 43:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(43, 8);
                  break;
               case 44:
                  if (curChar != 48)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAddStates(9, 11);
                  break;
               case 46:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAddTwoStates(46, 2);
                  break;
               case 47:
                  if ((0xff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAddTwoStates(47, 2);
                  break;
               case 48:
                  if (curChar == 47)
                     jjCheckNAddTwoStates(51, 15);
                  break;
               case 49:
                  if (curChar == 42)
                     jjstateSet[jjnewStateCnt++] = 50;
                  break;
               case 50:
                  if ((0xffff7fffffffffffL & l) != 0L && kind > 8)
                     kind = 8;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 23)
                        kind = 23;
                     jjCheckNAdd(13);
                  }
                  else if (curChar == 124)
                     jjCheckNAdd(15);
                  else if (curChar == 94)
                     jjCheckNAdd(15);
                  break;
               case 2:
                  if ((0x100000001000L & l) != 0L && kind > 16)
                     kind = 16;
                  break;
               case 5:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(21, 22);
                  break;
               case 8:
                  if ((0x5000000050L & l) != 0L && kind > 20)
                     kind = 20;
                  break;
               case 10:
                  jjAddStates(23, 24);
                  break;
               case 12:
               case 13:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(13);
                  break;
               case 23:
                  if (curChar == 94)
                     jjCheckNAdd(15);
                  break;
               case 24:
                  if (curChar == 124)
                     jjCheckNAdd(15);
                  break;
               case 33:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(25, 26);
                  break;
               case 37:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(27, 28);
                  break;
               case 41:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(29, 30);
                  break;
               case 45:
                  if ((0x100000001000000L & l) != 0L)
                     jjCheckNAdd(46);
                  break;
               case 46:
                  if ((0x7e0000007eL & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAddTwoStates(46, 2);
                  break;
               case 50:
                  if (kind > 8)
                     kind = 8;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 13:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 23)
                     kind = 23;
                  jjCheckNAdd(13);
                  break;
               case 10:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(23, 24);
                  break;
               case 50:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 8)
                     kind = 8;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 52 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjMoveStringLiteralDfa0_3()
{
   switch(curChar)
   {
      case 42:
         return jjMoveStringLiteralDfa1_3(0x1000L);
      default :
         return 1;
   }
}
private final int jjMoveStringLiteralDfa1_3(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      return 1;
   }
   switch(curChar)
   {
      case 47:
         if ((active0 & 0x1000L) != 0L)
            return jjStopAtPos(1, 12);
         break;
      default :
         return 2;
   }
   return 2;
}
private final int jjMoveStringLiteralDfa0_1()
{
   return jjMoveNfa_1(0, 0);
}
private final int jjMoveNfa_1(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 3;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x2400L & l) != 0L)
                  {
                     if (kind > 10)
                        kind = 10;
                  }
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if (curChar == 10 && kind > 10)
                     kind = 10;
                  break;
               case 2:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjMoveStringLiteralDfa0_2()
{
   switch(curChar)
   {
      case 42:
         return jjMoveStringLiteralDfa1_2(0x800L);
      default :
         return 1;
   }
}
private final int jjMoveStringLiteralDfa1_2(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      return 1;
   }
   switch(curChar)
   {
      case 47:
         if ((active0 & 0x800L) != 0L)
            return jjStopAtPos(1, 11);
         break;
      default :
         return 2;
   }
   return 2;
}
static final int[] jjnextStates = {
   30, 31, 36, 37, 40, 41, 8, 26, 28, 45, 47, 2, 4, 5, 8, 32, 
   33, 8, 40, 41, 8, 6, 7, 10, 11, 34, 35, 38, 39, 42, 43, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      default : 
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec4[i2] & l2) != 0L);
      case 48:
         return ((jjbitVec5[i2] & l2) != 0L);
      case 49:
         return ((jjbitVec6[i2] & l2) != 0L);
      case 51:
         return ((jjbitVec7[i2] & l2) != 0L);
      case 61:
         return ((jjbitVec8[i2] & l2) != 0L);
      default : 
         if ((jjbitVec3[i1] & l1) != 0L)
            return true;
         return false;
   }
}
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, null, null, 
null, "\156\145\164", "\156\157\144\145", null, null, null, null, null, null, null, 
null, null, null, "\50", "\51", "\173", "\175", "\133", "\135", "\73", "\54", "\56", 
"\75", "\76", "\74", "\41", "\176", "\77", "\72", "\75\75", "\74\75", "\76\75", 
"\41\75", "\174\174", "\46\46", "\136\136", "\53\53", "\55\55", "\53", "\55", "\52", 
"\57", "\46", "\174", "\136", "\74\74", "\76\76", "\76\76\76", "\53\75", "\55\75", 
"\52\75", "\57\75", "\46\75", "\174\75", "\136\75", "\45\75", "\74\74\75", "\76\76\75", 
"\76\76\76\75", "\55\76", null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
   "IN_SINGLE_LINE_COMMENT", 
   "IN_FORMAL_COMMENT", 
   "IN_MULTI_LINE_COMMENT", 
};
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, 1, 1, 2, 3, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0xfffffffffcd1c001L, 0x3ffL, 
};
static final long[] jjtoSkip = {
   0x1c3eL, 0x0L, 
};
static final long[] jjtoSpecial = {
   0x1c3eL, 0x0L, 
};
static final long[] jjtoMore = {
   0x23c0L, 0x0L, 
};
private JavaCharStream input_stream;
private final int[] jjrounds = new int[52];
private final int[] jjstateSet = new int[104];
StringBuffer image;
int jjimageLen;
int lengthOfMatch;
protected char curChar;
public DSLParserTokenManager(JavaCharStream stream)
{
   if (JavaCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public DSLParserTokenManager(JavaCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 52; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 4 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

private final Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public final Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      matchedToken.specialToken = specialToken;
      return matchedToken;
   }
   image = null;
   jjimageLen = 0;

   for (;;)
   {
     switch(curLexState)
     {
       case 0:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_0();
         break;
       case 1:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_1();
         if (jjmatchedPos == 0 && jjmatchedKind > 13)
         {
            jjmatchedKind = 13;
         }
         break;
       case 2:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_2();
         if (jjmatchedPos == 0 && jjmatchedKind > 13)
         {
            jjmatchedKind = 13;
         }
         break;
       case 3:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_3();
         if (jjmatchedPos == 0 && jjmatchedKind > 13)
         {
            jjmatchedKind = 13;
         }
         break;
     }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
           matchedToken.specialToken = specialToken;
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
           {
              matchedToken = jjFillToken();
              if (specialToken == null)
                 specialToken = matchedToken;
              else
              {
                 matchedToken.specialToken = specialToken;
                 specialToken = (specialToken.next = matchedToken);
              }
              SkipLexicalActions(matchedToken);
           }
           else 
              SkipLexicalActions(null);
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
        MoreLexicalActions();
      if (jjnewLexState[jjmatchedKind] != -1)
        curLexState = jjnewLexState[jjmatchedKind];
        curPos = 0;
        jjmatchedKind = 0x7fffffff;
        try {
           curChar = input_stream.readChar();
           continue;
        }
        catch (java.io.IOException e1) { }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
   }
  }
}

final void SkipLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
   }
}
final void MoreLexicalActions()
{
   jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
   switch(jjmatchedKind)
   {
      case 8 :
         if (image == null)
              image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen)));
         else
            image.append(input_stream.GetSuffix(jjimageLen));
         jjimageLen = 0;
                   input_stream.backup(1);
         break;
      default : 
         break;
   }
}
}
