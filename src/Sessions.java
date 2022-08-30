
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 206385502
 */
public class Sessions {
    
    public static int CurrentTurn; // current turn= 1- the players turn
    public static int MovesThisTurn=0; //             0- the computers turn
    public static boolean canplay;                               
 
    
    public Sessions(BoardPanel boardPanel,int starter){
        Sessions.CurrentTurn = starter;
        this.canplay = true;
    }
    
    public boolean CheckWin(BoardPanel boardPanel)// by the push of the win button or the end of the computer's turn
    {
        boolean winning=true;
        if(Sessions.canplay== true && Sessions.CurrentTurn==1) // its player's turn
        {
            winning = playerWon(boardPanel);
        }
        else
        {
            if(Sessions.canplay== true && Sessions.CurrentTurn==0)// its computer's turn
            {
                winning = computerWon(boardPanel);
            }
        }
        return winning;
    }
    
    private int returnUnusedColor(boolean [] colors)
    {
        int index = -1;
        for(int i=0 ; i< colors.length; i ++)
        {
            if(colors[i] ==false)
            {
                index = i;
            }
        }
        return index;
    }
    
    public boolean [] CheckSidra(LinkedList<Card> list,int jokerplace,boolean[] ret) //  [checks if it is a sidra   ,  checks if it is sidra ola]
    {                                                                        //  cannot by 2 jokers in 1 sidra!!!!
       
        boolean sidracolors[]={false,false,false,false};
        int sidraKind= -1;// 1 - sidra ola , 2 - colored sidra 
        
        
        if(list.isEmpty()) // in case the list is empty, sidra = 1 to continue check, sidra ola = 0
        {
            return ret;
        }
        
        if(list.size() < 3) // in case the sidra is not a sidra, not sidra  = 0, not sidra ola = 0;
        {
            ret[0]=false; ret[1] = false ; 
            return ret;
        }
        
        
        for(int g = 0 ; g< list.size(); g ++) // checks the colors in the sidra
        {
                  if(sidracolors[list.get(g).cardcolor.ordinal()] == true && list.get(g).CardJoker == false) // if its a same color sidra
                  {
                      sidraKind = 1;
                      break;
                  }
                  else
                  {
                     sidracolors[list.get(g).cardcolor.ordinal()] = true;
                  }                         
        }
        
        if(sidraKind != 1) // fins the sidra kind
        {
            sidraKind = 2;
        }
        
        
        if(jokerplace != -1) // put the right card at the joker spot if its not -1
        {
            if(jokerplace == 0)
            {
                switch(sidraKind)
                {
                    case(1):    // its sidra ola and joker at the beginning
                        list.get(jokerplace).cardcolor=list.get(1).cardcolor;
                        if(list.get(1).cardvalue.ordinal()>0)
                        {
                            list.get(jokerplace).cardvalue=Card.CardValue.values()[list.get(1).cardvalue.ordinal()-1];
                        }
                        else
                        {
                            ret[0]=false; ret[1] = false;
                            return ret;
                        }
                        break;
                    case(2):    // its colored sidra and joker at the beginning
                        list.get(jokerplace).cardvalue=Card.CardValue.values()[list.get(1).cardvalue.ordinal()];
                        int color =returnUnusedColor(sidracolors);
                        if(color != -1)
                        {
                            list.get(jokerplace).cardcolor=Card.CardColor.values()[color];
                        }
                        else
                        {
                           ret[0]=false; ret[1] = false;
                            return ret;   
                        }
                        break;
                }
            }
            if(jokerplace == list.size()-1)
            {
                switch(sidraKind)
                {
                    case(1):    // its sidra ola and joker at the end
                        list.get(jokerplace).cardcolor=list.get(list.size()-2).cardcolor;
                        if(list.get(list.size()-2).cardvalue == Card.CardValue.THIRTEEN)
                        {
                            ret[0]=false; ret[1] = false;
                            return ret;
                        }
                        else
                        {
                            list.get(jokerplace).cardvalue=Card.CardValue.values()[list.get(list.size()-2).cardvalue.ordinal()+1];
                        }
                        break;
                    case(2):    // its colored sidra and joker at the end
                        list.get(jokerplace).cardvalue=Card.CardValue.values()[list.get(list.size()-2).cardvalue.ordinal()];
                        int color =returnUnusedColor(sidracolors);
                        if(color != -1)
                        {
                            list.get(jokerplace).cardcolor=Card.CardColor.values()[color];
                        }
                        else
                        {
                           ret[0]=false; ret[1] = false;
                            return ret;   
                        }
                        break;
                }
            }
            if(jokerplace > 0 && jokerplace < list.size()-1)
            {
                switch(sidraKind)
                {
                    case(1):    // its sidra ola and joker at the middle
                        list.get(jokerplace).cardcolor=list.get(0).cardcolor;
                        list.get(jokerplace).cardvalue = Card.CardValue.values()[(list.get(jokerplace-1).cardvalue.ordinal()+list.get(jokerplace+1).cardvalue.ordinal())/2];
                        break;
                    case(2):    // its colored sidra and joker at the middle
                        list.get(jokerplace).cardvalue=Card.CardValue.values()[list.get(0).cardvalue.ordinal()];
                        int color =returnUnusedColor(sidracolors);
                        if(color != -1)
                        {
                            list.get(jokerplace).cardcolor=Card.CardColor.values()[color];
                        }
                        else
                        {
                           ret[0]=false; ret[1] = false;
                            return ret;   
                        }
                        break;
                }
            }
        }
 
        
        
       if(sidraKind == 1) //checks the ligelity of the sidra ola
       {
           ret[1]=true;
           for(int i =0 ; i<list.size()-1 ; i++)
           {
               if(list.get(i).cardvalue.ordinal()+1 != list.get(i+1).cardvalue.ordinal() ||
                       list.get(i).cardcolor != list.get(i+1).cardcolor)
               {
                  ret[0]=false; ret[1]=false;   
               }
           }
       }
       else  //checks the ligelity of the colored sidra 
       {
        ret[1]=false;
        if(list.size()>4)
        {
            ret[0]=false; ret[1]=false;
        }
        else
        {
            for(int i =0 ; i<list.size()-1 ; i++)
            {
                if(list.get(i).cardvalue != list.get(i+1).cardvalue)
                {
                  ret[0]=false; ret[1]=false;   
                }
            }
        }
    }
   
        return ret;
    }

    public boolean playerWon(BoardPanel boardPanel)
    {
        int i,j,counter,k;
        boolean check=true,ok=false;
        boolean upsidra= false;
        int joker = -1;
        boolean onethirteenCheck= false;
        
        LinkedList <Card> l = new LinkedList<Card>();
        
        for(i=0;i<Deck.ROWS ;i++)
        {
           
           for(j=0;j<Deck.COLS ;j++)
           {
               Card card = boardPanel.boardPlayer.cells[i][j];
               if(card.cardvalue != Card.CardValue.NONE)
               {
                   l.add(card);
                   if(card.CardJoker == true)
                   {
                       joker = l.size()-1;
                   }
               }
               else
               {
                      boolean g[] = {true,false};
                     CheckSidra(l,joker,g);
                     
                     if(g[0] == false)
                     {
                         return false;
                     }
                     if(g[1] == true )
                     {
                         upsidra = true;
                     }
                     l.clear();
                     joker = -1;
               }
               
           }
           // check when its the end of the row
            boolean g[] = {true,false};
            CheckSidra(l,joker,g);
                          
            if(g[0] == false)
            {
                return false;
            }
            if(g[1] == true )
            {
                upsidra = true;
            }
            l.clear();
            joker = -1;
           
        }
        if(upsidra ==true)
        {
            return true;
        }
       else
        {
            return false;
        }
    }


    private boolean computerWon(BoardPanel boardPanel) {
        // the j cards must be on the board already before the checking!
      int jcounter=0,i,j,currindex= -1,startindex=-1;
      boolean out = true;
      LinkedList<Card> checkSend= new LinkedList<Card>();
      // needed a copy of the 2 matrixes
      SparseMatrix w = new SparseMatrix(boardPanel.boardComputer.CompSparseMatrix,boardPanel);
      
      for(ArrayList<SparseMatrixNode> x : w.SparseMatColors)// check every row in the colored SparceMatrix
      {
          i=0;
          while(++i<x.size())
          {
                  startindex=i-1;
                  currindex = i;

              if(x.size() >2)
              {
                  out=true; 
                  checkSend.add(x.get(0).NodeCard);
                   while(out && currindex<x.size())
                  {
                     
                      Card cardnow= x.get(currindex).NodeCard;
                      Card cardbefore= x.get(currindex-1).NodeCard;
                      
                      if(cardnow != null && cardbefore != null && cardnow.cardvalue.ordinal()==cardbefore.cardvalue.ordinal()+1) // check for the continue of the sidar ola
                      {
                           checkSend.add(x.get(currindex).NodeCard);
                           currindex ++;
                      }
                      else
                      {
                          out = false;
                      }                      
                  }
                  // the current sidar ola has ended, need to check
                  boolean [] ok = new boolean[2];
                  ok[0]=true; ok[1] = false;
                  if(this.CheckSidra(checkSend, -1, ok)[0] == false)
                  {
                      //the sidra is not fine
                      // need to check for the sidra in other direction, to find colored sidra
                      if(currindex - startindex>1)
                      {
                      i=i+(currindex - startindex);// move forward on the 'i' index of the color row
                      }
                  }
                  else // the sidra is fine
                  {
                      // 1) check if the good sidra dosent contains other colored sidra.
                    boolean fullcolored = true;
                    for( j = startindex ;fullcolored && j< currindex; j++)
                    {
                        if(w.SparseMatValues.get(currindex).size()<3)  
                        {
                            fullcolored = false;
                        }
                    }
                    
                    if(fullcolored == false)// not colored full sidra
                                            // 2) if dosent contains other sdarot on his lenght the sidra is fine and remove it.
                    {
                        for(int h =startindex  ; h >= currindex ;h-- )// sidra ola only ! need to remove it.
                        {
                            Card del = x.get(h).NodeCard;
                            w.DeleteFromMatrix(del, false);
                        }
                    }
                    else
                    {
                        // sidra ola full of colored sdarot ! remove each one of them. check later!
                    }    
                  }
              }
              else
              {
                  // its not a sidra ola in this color
                  if(w.SparseMatValues.get(x.get(currindex).NodeCard.cardvalue.ordinal()).size()<3)
                  {
                      //not sidra at all!
                      return false;
                  }
                  else
                  {
                      // its colored sidra. check later!        
                  }
                  
              }
          }
      }
      
      
      for(ArrayList<SparseMatrixNode> x : w.SparseMatValues)// check every row in the colored SparseMatrix
      {
          if(x.size()<3)
          {
              return false;
          }
      }
      if(boardPanel.boardComputer.Sidraola==true)
      {
            return true;
      }
      boardPanel.boardComputer.Sidraola=false;
      return false;
      
    }
    
   
}