
import java.awt.Point;
import java.util.Collections;
import java.util.Stack;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 206385502
 */
public class Deck {
    public static int COLS=13;
    public static int ROWS=4;  
    public static int CashX=429, CashY=230;  
    static int CashCardX=541;
    Stack<Card> cards ;
    BoardPanel boardpanel; 
     Card Cash;
     static boolean CashOut;
     Card CashCard;
     static boolean CardCashOut;
     
     
     public Deck(BoardPanel boardPanel){
         cards = new Stack<Card>();
         this.boardpanel = boardPanel;
         this.CashOut=false;
         this.CardCashOut=false;
         this.Cash = new Card(boardPanel);
         this.CashCard=new Card(boardPanel);
   
         for(int i=0;i<COLS;i++)
         {
         for(int h=0;h<ROWS;h++)
         {
           for(int j=0;j<2;j++)   
           {
               Card card =new Card(h,i,boardPanel);
               cards.push(card);
           }
         }
        }
        
        //jokers
        Card card1 = new Card(1,1,boardPanel);//insert red joker (value 1 is not important)
        Card card2 = new Card(3,1,boardPanel);//insert black joker(value 1 is not important)
        
        card1.CardJoker=true;
        card2.CardJoker=true;
        cards.push(card1);
        cards.push(card2);
         //NEED FOR SHUFFLE!!!!!!
         Collections.shuffle(cards.subList(0, ROWS*COLS*2+2)); //+2 Jokers!
        
         //puting Cash on the board
        Cash.setLocation(CashX,CashY);
        Cash.setImage(Card.CardColor.NONE,Card.CardValue.NONE,false);
        boardPanel.add(Cash);
        Card rndcard = cards.pop();
        // puting Cash on the board
        this.TakeFromCash(boardPanel);
       
        this.CashCard = new Card(rndcard,boardPanel);
        this.CashCard.setLocation(CashCardX,CashY);
        boardPanel.add(CashCard);
        boardPanel.repaint();
      
     }
     
     public void TakeFromCash(BoardPanel boardpanel){
         this.cards.pop().HalfSwapCards(this.Cash, boardpanel,false);
         this.Cash.setImageNONE();
    }
     
    public static boolean IfCash(Point loc){
        return loc.x>=CashX && loc.x<=CashX + Card.cardW && loc.y>=CashY && loc.y<=CashY + Card.cardH;
    }
     public static boolean IfCashCard(Point loc){
        return loc.x>=CashCardX && loc.x<=CashCardX + Card.cardW && loc.y>=CashY && loc.y<=CashY + Card.cardH;
    }
     
}
