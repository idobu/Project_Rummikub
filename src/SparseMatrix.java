
import java.util.*;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author home
 */
public class SparseMatrix {
    ArrayList<ArrayList<SparseMatrixNode>> SparseMatColors ;
    ArrayList<ArrayList<SparseMatrixNode>> SparseMatValues;
    ArrayList<SparseMatrixNode> Jcards;
    LinkedList<Card> j = new LinkedList<Card>(); //list of the jokers to remove from the matrix if not won

    BoardPanel board;
    public SparseMatrix(BoardPanel b)
    {
        this.board=b;
        this.SparseMatColors = new ArrayList<ArrayList<SparseMatrixNode>>();
        for(int i = 0 ;i < Deck.ROWS ;i++) // set the color matrix
        {
            ArrayList<SparseMatrixNode> x = new ArrayList<SparseMatrixNode>();
            SparseMatColors.add(x);
        }
        this.SparseMatValues = new ArrayList<ArrayList<SparseMatrixNode>>();
        for(int i = 0 ;i < Deck.COLS ;i++) // set the color matrix
        {
            ArrayList<SparseMatrixNode> y = new ArrayList<SparseMatrixNode>();
            SparseMatValues.add(y);
        }
        this.Jcards = new ArrayList<SparseMatrixNode>();
    }

    SparseMatrix(SparseMatrix CompSparseMatrix,BoardPanel b) {
        this.board=b;
            this.SparseMatColors = new ArrayList<ArrayList<SparseMatrixNode>>();
    for(int i = 0 ;i < Deck.ROWS ;i++) // set the color matrix
    {
        ArrayList<SparseMatrixNode> x = new ArrayList<SparseMatrixNode>();
        SparseMatColors.add(x);
    }
    this.SparseMatValues = new ArrayList<ArrayList<SparseMatrixNode>>();
    for(int i = 0 ;i < Deck.COLS ;i++) // set the color matrix
    {
        ArrayList<SparseMatrixNode> y = new ArrayList<SparseMatrixNode>();
        SparseMatValues.add(y);
    }
     this.Jcards = new ArrayList<SparseMatrixNode>();
     
     int count =0;
    for(int i =0 ; i<=3 ; i++)    
    {
        count=0;
       if(CompSparseMatrix.SparseMatValues.get(i).size()>0)
       {
       for(SparseMatrixNode x : CompSparseMatrix.SparseMatValues.get(i))
       {
           for(int j = 0 ;j<x.count;j++)
           {
               
                this.AddToMatrix(x.NodeCard,this.board); // adding the card to both matrixes
               
          
           }
       }
       }
        
    }
    }
    
    
    
    public void AddToMatrix(Card card,BoardPanel board) // add card to 2 lists 
    {
        Card set = new Card(card,board);
        SparseMatrixNode  node1 = new SparseMatrixNode(set,1);
        SparseMatrixNode  node2 = new SparseMatrixNode(set,1);
        if(card.CardJoker == false)
        {
            int indexcolor = -1;
            int indexvalue = -1;
            int count=0; 
            for(SparseMatrixNode x : this.SparseMatColors.get(card.cardcolor.ordinal())) // index of the color arraylist
            {
                if( x.NodeCard.cardvalue == card.cardvalue)
                {      
                    indexcolor = count;
                }
                count++;
            }
            count = 0;
            for(SparseMatrixNode y : this.SparseMatValues.get(card.cardvalue.ordinal())) // index in the value arraylist
            {
               if(y.NodeCard.cardcolor == card.cardcolor)
                {
                    indexvalue = count;
                } 
                count++;
            }
            if(indexvalue != -1 && indexcolor != -1)
            {
                this.SparseMatColors.get(card.cardcolor.ordinal()).get(indexcolor).count++;  // added to 2 lists
                this.SparseMatValues.get(card.cardvalue.ordinal()).get(indexvalue).count++;
            }
            else
            {
                this.SparseMatColors.get(card.cardcolor.ordinal()).add(node1);            // added to 2 lists
                this.SparseMatValues.get(card.cardvalue.ordinal()).add(node2);     
                Collections.sort(this.SparseMatColors.get(card.cardcolor.ordinal()), new ComparatorByValue());
                Collections.sort(this.SparseMatValues.get(card.cardvalue.ordinal()), new ComparatorByColor());
            }
            
        }
        else
        {
            if(this.Jcards.isEmpty())
            {
                this.Jcards.add(node1);
            }
            else
            {
                this.Jcards.get(0).count++;
            }
            
        }
    }
    
    public void placejokers()
    {
    
        if(!this.Jcards.isEmpty())
        {
           boolean out =false;
           for(int e =0 ; e< this.Jcards.get(0).count;e++)
           { 
            for(ArrayList<SparseMatrixNode> x : this.SparseMatColors)// check every row in the colored SparceMatrix for replacemat to the joker
            { 
                int count = 0;
                for(SparseMatrixNode s : x)
                {
                    if(s.count < 2)
                    {
                    if((count+1 < x.size()) &&  s.NodeCard.cardvalue.ordinal()+2 == x.get(count+1).NodeCard.cardvalue.ordinal())
                    {
                        Card j = new Card(s.NodeCard.cardcolor.ordinal(),s.NodeCard.cardvalue.ordinal()+1,this.board);
                        this.j.add(j); // need to remove later
                        out = true;
                        break;
                    }
                    else
                    {
                        if(this.SparseMatValues.get(s.NodeCard.cardvalue.ordinal()).size()==2)// optional for colored sidra placemant
                        {
                            int [] colors = new int[4];
                            colors[0] = 0; colors[1] =0; colors[2]=0; colors[3] =0;
                            for(int i =0 ; i<this.SparseMatValues.get(s.NodeCard.cardvalue.ordinal()).size();i++)
                            {
                                colors[this.SparseMatValues.get(s.NodeCard.cardvalue.ordinal()).get(i).NodeCard.cardcolor.ordinal()]++;
                            }
                            for(int i =0 ;i < colors.length; i++)
                            {
                                if(colors[i]==0)
                                {
                                    Card j = new Card(i,s.NodeCard.cardvalue.ordinal(),this.board);
                                    this.j.add(j); // need to remove later
                                    out = true;
                                    break;
                                }
                            }

                        }
                    }
          
                    count++;
                }
                if(out)
                {
                    break;
                }
                }
                  if(out)
                {
                    break;
                }
            }
               out =false;
           }
           
           //after finding place for each of the joker cards , add them to the matrix
           for(Card card : this.j)
           {
               card.CardJoker=false;
               this.AddToMatrix(card, this.board);
           }
            this.Jcards.get(0).count=0;
        }
    }
    
    public void returnjokers() // return the jokers from their temporary places in the matrix
    {
        for(int i =0 ;i<this.j.size();i++)
        {
              Card t = j.pop();
              t.CardJoker=false;
              t= this.DeleteFromMatrix(t, false);
              t.CardJoker=true;
              this.AddToMatrix(t, this.board);
        }
        j.clear();
    }
    
    
    public Card DeleteFromMatrix(Card delcard, boolean removeJ) // not build for extracting a placed jokers on the board
    {
        SparseMatrixNode ret=null;
        if(removeJ == false && this.IsExist(delcard))
        {

            SparseMatrixNode  node =null;
            boolean containscolor = false;
            boolean containsvalue =false;
            int indexcolor = -1;
            int indexvalue = -1;
            int count=0; 
            for(SparseMatrixNode x : this.SparseMatColors.get(delcard.cardcolor.ordinal())) // index of the color arraylist
            {
                if(x.NodeCard.cardvalue == delcard.cardvalue)
                {
                   
                    indexcolor =count;
                }
                count++;
            }
            count=0;
            for(SparseMatrixNode y : this.SparseMatValues.get(delcard.cardvalue.ordinal())) // index in the value arraylist
            {
               if(y.NodeCard.cardcolor == delcard.cardcolor)
                {
        
                    indexvalue = count;
                } 
               count++;
            }
            if(indexcolor != -1 && indexvalue != -1)
            {

                SparseMatrixNode colorcard= this.SparseMatColors.get(delcard.cardcolor.ordinal()).get(indexcolor); 
                SparseMatrixNode valuecard= this.SparseMatValues.get(delcard.cardvalue.ordinal()).get(indexvalue);

                if(colorcard.count>1)
                {
                    ret = new SparseMatrixNode(colorcard);   // removed from 2 lists
                    colorcard.count--;
                    valuecard.count--;
                }
                else
                {
                    ret = new SparseMatrixNode(this.SparseMatColors.get(delcard.cardcolor.ordinal()).remove(indexcolor)); // removed from 2 lists
                    this.SparseMatValues.get(delcard.cardvalue.ordinal()).remove(indexvalue);

                }
            }
            else
            {
                ret = null; // not found in one or more of the matrixes
            }
        }
        else // will return joker card if removeJ is equal true
        {
            if(!this.Jcards.isEmpty())
            {
                ret = this.Jcards.get(0);
                if( this.Jcards.get(0).count>1)
                {
                    this.Jcards.get(0).count--;
                } 
                else
                {
                    ret =this.Jcards.remove(0);
                }
            }
        }
       return ret.NodeCard;
    }
    
   public boolean IsJoker(Card del){// check if the card is a joker in the matrix
    for(Card x : this.j)
    {
        if(x == del)
        {
            return true;
        }
    }
    return false;
   }
    
   public boolean IsExist(Card del) // check for the existence of the card in the matrix
   {
       for(ArrayList<SparseMatrixNode> x : this.SparseMatColors)
       {
           for(SparseMatrixNode y : x)
           {
               if(y.NodeCard.cardcolor == del.cardcolor
                   &&y.NodeCard.cardvalue == del.cardvalue)
               {
                   return true;
               }
           }
       }
       return false;
   }
   
}




// 2 comperators for sorting the colors and values

class ComparatorByValue implements Comparator<SparseMatrixNode> {
    @Override
    public int compare(SparseMatrixNode o1, SparseMatrixNode o2) {
        return o1.NodeCard.cardvalue.ordinal() - o2.NodeCard.cardvalue.ordinal();
    }
}

class ComparatorByColor implements Comparator<SparseMatrixNode> {
    @Override
    public int compare(SparseMatrixNode o1, SparseMatrixNode o2) {
        return o1.NodeCard.cardcolor.ordinal() - o2.NodeCard.cardcolor.ordinal();
    }
}
