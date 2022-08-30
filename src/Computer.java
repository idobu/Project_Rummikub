
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Computer {
    Card [][]  ComputerBoardcells = new Card[Deck.ROWS][Deck.COLS];
    SparseMatrix CompSparseMatrix;
    static int putx = ComputerBoard.TopButtonStartX+520;
    static int puty = ComputerBoard.TopButtonStartY+37;
    
    boolean SetEmptyCards;
    boolean Sidraola;
    int starter;
    BoardPanel boardPanel;
    Card delOption;
    
    public Computer(BoardPanel boardPanel, int starter) {
        this.Sidraola= false;
        this.boardPanel = boardPanel;
        int reply = JOptionPane.showConfirmDialog(null, "Do you wish to hide the computer cards?", "Remmikub Message", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
          this.SetEmptyCards = false;
        }
        else {
          this.SetEmptyCards =true;
        }                             // show the computer's card through the game: false - hide cards
        CompSparseMatrix= new SparseMatrix(this.boardPanel);    //                                            true -  show cards
        //////// create cards on board
        int y =puty ,x;
        for(int i=0; i<Deck.ROWS; i++)
        {
            x=putx;
            for(int j=0; j<Deck.COLS; j++,x+=PlayerBoard.deltaX)
            {
                ComputerBoardcells[i][j] = new Card(boardPanel); 
                ComputerBoardcells[i][j].setLocation(x, y);          
                boardPanel.add(ComputerBoardcells[i][j]);
            }
            y+=PlayerBoard.deltaY;
        }  
       
        Init();
    }
    
    private void Init() {
        int i,num = starter ==1? 15 : 14;
        int x=0,y=0;
         for  (i = 0; i < num; i++) {
             
            Card card = this.boardPanel.deck.cards.pop();
            this.CompSparseMatrix.AddToMatrix(card,this.boardPanel);        
         }
         PlaceCardInBoardFromSparse(this.SetEmptyCards); 
    } 
    
    public void PlaceCardInBoardFromSparse(boolean setempty) // place cards from the matrix (in the colored matrix Order) on the board.
    {
        int i =0,j=0;
        boolean moved = false;
        for(ArrayList<SparseMatrixNode> node : this.CompSparseMatrix.SparseMatColors)
        {
            for(SparseMatrixNode x : node)
            {
                for(int c =0 ; c<  x.count ; c++)
                {
                    if(j < Deck.COLS)   
                    {
                        x.NodeCard.HalfSwapCards(this.ComputerBoardcells[i][j],this.boardPanel,setempty);                    
                    }
                    else
                    {
                        j=0;
                        i++;
                        x.NodeCard.HalfSwapCards(this.ComputerBoardcells[i][j],this.boardPanel,setempty);                    
                    } 
                    j++;
                }
            }
        }

    for(SparseMatrixNode x : this.CompSparseMatrix.Jcards)
    {
        for(int y = 0 ; y< x.count ; y++)
        {
            if(j < Deck.COLS)   
            {
                x.NodeCard.CardJoker=true;
                x.NodeCard.HalfSwapCards(this.ComputerBoardcells[i][j],this.boardPanel,true);   
                j++;
            }
            else
            {
                j=0;
                i++;
                x.NodeCard.CardJoker=true;
                x.NodeCard.HalfSwapCards(this.ComputerBoardcells[i][j],this.boardPanel,true);                     
            }                
    
       }
    } 
     
    }
   
   public int EvaluateBoard(SparseMatrix mat,boolean sugDeleting)
   {
       
      SparseMatrix copy = new SparseMatrix(mat,this.boardPanel);
      // the j cards must be on the board already before the checking!
      int jcounter=0,i=0,j,currindex= -1,startindex=-1,eval =0;
      boolean out = true;
      LinkedList<Card> checkSend= new LinkedList<Card>();
      // needed a copy of the 2 matrixes
      
      for(ArrayList<SparseMatrixNode> x : copy.SparseMatColors)// check every row in the colored SparceMatrix
      { 
          i=0;
          while(++i<x.size())
          {

              if(x.size() >2)
              {
                  out=true; 
                  startindex=i-1;
                  checkSend.add(x.get(0).NodeCard);
                  currindex = i;
                  while(out && currindex <x.size())
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
                  if(this.boardPanel.session.CheckSidra(checkSend, -1, ok)[0] == false)
                  {
                      //the sidra is not fine
                      // need to check for the sidra in other direction, to find colored sidra
                      if(currindex-startindex>1)
                      {
                      i= i+(currindex - startindex);// move forward on the 'i' index of the color row
                      }
                      if(checkSend.size() ==2)
                      {
                         for(int g = 0 ; g < checkSend.size();g++)
                         {      
                            eval+=5;
                         }   
                      }
                      else
                      {
                            eval+=-15;  
                      }
                  checkSend.clear();
                }
                
                  
                  else // the sidra is fine
                  {
                      // 1) check if the good sidra dosent contains other colored sidra.
                    boolean fullcolored = true;
                    for( j = startindex ;fullcolored && j< currindex; j++)
                    {
                        if(copy.SparseMatValues.get(currindex).size()<3)  
                        {
                            fullcolored = false;
                        }
                    }
                    
                    if(fullcolored == false)// not colored full sidra
                                            // 2) if dosent contains other sdarot on his lenght the sidra is fine and remove it.
                    {
                        for(int h =currindex-1  ; h >= startindex ;h-- )// sidra ola only ! need to remove it.
                        {
                            Card del = x.get(h).NodeCard;
                            copy.DeleteFromMatrix(del, false);
                                eval +=15;
                            if(ok[1] == true)    
                            {
                                this.Sidraola=true;
                            }
                        }
                    }
                    else
                    {
                        // sidra ola full of colored sdarot ! remove each one of them
                    }
                    checkSend.clear();
                    i=0;
                 }
              }
              else
              {
                  if(x.size()!=0)
                  {
                 //
                 // its not a sidra ola in this color
                      if(copy.SparseMatValues.get(x.get(i-1).NodeCard.cardvalue.ordinal()).size()<3)
                      {
                          if(copy.SparseMatValues.get(x.get(i-1).NodeCard.cardvalue.ordinal()).size()==2)//potential
                          {
                               LinkedList<Card> test = new LinkedList<Card>();
                               if( x.get(i-1).NodeCard.cardcolor.ordinal()>1)
                               {
                                   for(SparseMatrixNode y : copy.SparseMatValues.get(x.get(i-1).NodeCard.cardcolor.ordinal()))// check every row in the colored SparceMatrix
                                   {
                                       test.add(y.NodeCard);                               
                                   }
                                   for(Card f: test)
                                   {
                                       copy.DeleteFromMatrix(f, false);
                                   }
                               }
                               if(this.Sidraola==true)
                               {
                                   eval+=10;//2*5 potential
                               }
                               else
                               {
                                   eval+=6;//2*3 potential without sidra ola equired
                               }
                               
                          }
                          else//not sidra at all!
                          {
                          eval +=-15;
                              if(sugDeleting)
                              {
                                  this.delOption = x.get(i-1).NodeCard;
                              }
                          }
                      }
                      else
                      {
                          // its colored sidra. 
                          if(this.Sidraola==false)
                          {
                              eval += copy.SparseMatValues.get(x.get(i-1).NodeCard.cardcolor.ordinal()).size()*8;
                          }
                          else
                          {
                              eval += copy.SparseMatValues.get(x.get(i-1).NodeCard.cardcolor.ordinal()).size()*15;
                          }
                          //remove this sidra
                          LinkedList<Card> test = new LinkedList<Card>();
                           if( x.get(i-1).NodeCard.cardcolor.ordinal()>1)
                           {
                               for(SparseMatrixNode y : copy.SparseMatValues.get(x.get(i-1).NodeCard.cardcolor.ordinal()))// check every row in the colored SparceMatrix
                               {
                                   test.add(y.NodeCard);                               
                               }
                               for(Card f: test)
                               {
                                   copy.DeleteFromMatrix(f, false);
                               }
                           }
                      }
                  }
              }
         }
      }
      for(ArrayList<SparseMatrixNode> x : copy.SparseMatValues)// check every row in the colored SparseMatrix
      {
        
          ArrayList<SparseMatrixNode> a=x;
          if(x.size()<3)
          {
             int countrow=0;
             for(int h =0 ;h<x.size();h++)
             {
                 Card card= x.get(h).NodeCard;
                 //SparseMatrixNode iter = t.iterator();
                 
                 if(this.Sidraola==false)
                 {
                     eval+=8;
                 }
                 else
                 {
                     eval+=15;
                 }
                 if(x.get(h).count>1)
                 {
                     //continue
                 }
                 else
                 {
                     h=0;
                 }
                 copy.DeleteFromMatrix(card, false);
                 
             }
          }
          else
          {
               for(int h =0 ;h<x.size();h++)
             {
                 Card card= x.get(h).NodeCard;
                 //SparseMatrixNode iter = t.iterator();
                 
                 eval+=-10;
                 if(x.get(h).count>1)
                 {
                     //continue
                 }
                 else
                 {
                     h=0;
                 }
                 copy.DeleteFromMatrix(card, false);
                 
             }
          }
      }
      return eval;
      
   }
   
    public void EvaluateMove(){  
        // 1) search best card to draw and place in the best place : CashCard \ opponent card
          //if
          // 1.1) the card that the opponent trowed is helpful to me?    
          //if
          // 1.2) the card in the CashCard helpful and its the last one i need?
          //default
          // 1.3) draw a card from the cash.
          
          //reorder and repalce cards
          // 1.4) if there is a Jcard placed in a place that a regular card can replace, replace it.
          
          
        // 2) search for the best card to trow
          // 2.1) a lonely card without a sidra connected
          // 2.2) 
        SparseMatrix c = new SparseMatrix(this.CompSparseMatrix,this.boardPanel);
        SparseMatrix d = new SparseMatrix(this.CompSparseMatrix,this.boardPanel);
        int [] Jplace = new int[2];
        boolean J=false;
        int i,j,EvalSummaryPrevious=0,EvalSummaryAfter=0;
        boolean taken = false;
        
        this.CompSparseMatrix.placejokers();
        
        EvalSummaryPrevious = EvaluateBoard(c,false); // no need to suggest remove card before we located a card to take.
         //if the CashCard is the only card that need to be placed and he is fits in the board  
    
         Card OpponentCard=null;
         if(this.boardPanel.boardPlayer.lastCardTrowen != null){
             OpponentCard = this.boardPanel.boardPlayer.lastCardTrowen;
          }
        if(OpponentCard.CardJoker==false && OpponentCard != null)
        {
            d.AddToMatrix(OpponentCard,this.boardPanel);
        }
        else// the opponent card is a joker
        {
            this.CompSparseMatrix.AddToMatrix(OpponentCard,this.boardPanel);
            // the card is added to the matrix 
            this.boardPanel.boardPlayer.removeLastcard();
            //remove card from the players board
           // this.removeCard();
            //card removed from the matrix
           // this.PlaceCardInBoardFromSparse(true);
            //the new board cards been placed on the screen
            this.boardPanel.repaint();
            System.out.println("Computer draw a card from the opponent's last card");
            taken=true;

        }

       if(taken == false)
       {
        EvalSummaryAfter =EvaluateBoard(d,true);// suggest a card to remove to later on..
        
        if(EvalSummaryAfter > EvalSummaryPrevious)// the board got better grade with the last card trowen
        {
            this.CompSparseMatrix.AddToMatrix(OpponentCard,this.boardPanel);
            // the card is added to the matrix 
            this.boardPanel.boardPlayer.removeLastcard();
            //remove card from the players board
            //card removed from the matrix
            //the new board cards been placed on the screen
            this.boardPanel.repaint();
            System.out.println("Computer draw a card from the opponent's last card");
        }
        else// keep searching better solution, last solution in draw from cash
        {         
           Card draw = new Card(this.boardPanel.deck.Cash,this.boardPanel);
           this.boardPanel.deck.TakeFromCash(boardPanel);
           // card from cash was drawen
           this.CompSparseMatrix.AddToMatrix(draw,this.boardPanel); // place the drawn card on the matrix
           // place the card on the ComputerCells on the board
           // this.PlaceCardInBoardFromSparse(true);     
           this.boardPanel.repaint();
           System.out.println("Computer draw a card from the cash");
        }
       }
    
        this.CompSparseMatrix.returnjokers(); // after thr calculation return the jokers for next useage
        this.removeCard();                    // remove card
        this.PlaceCardInBoardFromSparse(this.SetEmptyCards);

    }

    public void CompWinShowCards()// show computer cards in case of (winning/ end of the game)
    {
        this.PlaceCardInBoardFromSparse(true);
    }

    private void removeCard() {
     if(this.delOption != null &&  !this.CompSparseMatrix.IsJoker(delOption) && this.CompSparseMatrix.IsExist(delOption))
     {
         
        Card remove = new Card( this.CompSparseMatrix.DeleteFromMatrix(delOption, false),this.boardPanel);
        
    
        this.PlaceCardInBoardFromSparse(true);
        Card clear =this.boardPanel.boardComputer.ClearPlaceCard();
        remove.HalfSwapCards(clear, boardPanel, true);
        System.out.println("The computer threw the card: " +remove.cardvalue.toString()+" "+remove.cardcolor.toString() +" you are able to pick him in your turn");
        this.boardPanel.boardComputer.lastCardTrowen=clear;
         
     }
     else
     {
         int count =0;
         for(ArrayList<SparseMatrixNode> x : this.CompSparseMatrix.SparseMatColors)
         {
             for(SparseMatrixNode y : x)
             {
                 if(x.size()==1)
                 {
                          Card remove = new Card(this.CompSparseMatrix.DeleteFromMatrix(x.get(0).NodeCard, false),boardPanel);
                          this.PlaceCardInBoardFromSparse(true);
                          Card clear =this.boardPanel.boardComputer.ClearPlaceCard();
                          remove.HalfSwapCards(clear, boardPanel, true);
                          this.boardPanel.boardComputer.lastCardTrowen=clear;
                          System.out.println("The computer threw the card: " + remove.cardvalue.toString() + " " + remove.cardcolor.toString() + " you are able to pick him in your turn");
                          return;
                 }
                 else
                 {
                           Card remove = new Card(this.CompSparseMatrix.DeleteFromMatrix(y.NodeCard, false),boardPanel);
                           this.PlaceCardInBoardFromSparse(true);
                           Card clear =this.boardPanel.boardComputer.ClearPlaceCard();
                           remove.HalfSwapCards(clear, boardPanel, true);
                           this.boardPanel.boardComputer.lastCardTrowen=clear;
                           System.out.println("The computer threw the card: " +remove.cardvalue.toString()+" "+remove.cardcolor.toString() +" you are able to pick him in your turn");
                           return;
                 }
             }
                 count++;
         }
     }
   
    }
    
    
   }
   

