

import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Card extends JButton{


    public enum CardColor{BLUE,RED,YELLOW,BLACK, NONE};
    public enum CardValue {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, 
                            NINE, TEN, ELEVEN, TWELVE, THIRTEEN, NONE };
    
    public static final int cardW=30;
    public static final int cardH=45;
    
    public  CardColor cardcolor;
    public  CardValue cardvalue;
    public  boolean CardJoker;
    public Point Currentlocation;
    public Card()
    {

    }
    public Card(int color, int value,BoardPanel boardPanel)
    {
        this.CardJoker = false;
        this.cardvalue= CardValue.values()[value];
        this.cardcolor=CardColor.values()[color];
        this.setImage(this.cardcolor, this.cardvalue,false);
        Init(boardPanel);
    }
    
    public Card(Card c,BoardPanel boardPanel)
    {
        this.CardJoker = false;
        this.cardcolor= c.cardcolor;
        this.cardvalue= c.cardvalue;
        this.setImage(c.cardcolor, c.cardvalue,false);
        Init(boardPanel);
    }
    
    public Card(BoardPanel boardPanel){
        this.CardJoker = false;
        this.cardcolor= CardColor.NONE;
        this.cardvalue= CardValue.NONE;
        Init(boardPanel);
    }
    public void SwapCards(Card card,BoardPanel boardPanel){ // cards swapping values between them
        
        boolean condition = this.CardJoker;
        CardColor f =this.cardcolor;
        CardValue g =this.cardvalue;
        this.cardcolor=card.cardcolor;
        this.cardvalue=card.cardvalue;
        this.CardJoker = card.CardJoker;
        card.cardcolor=f;
        card.cardvalue=g;
        card.CardJoker = condition;
        this.setImage(this.cardcolor, this.cardvalue,false);
        card.setImage(card.cardcolor, card.cardvalue,false);
    }
 
    public void HalfSwapCards(Card card,BoardPanel boardPanel,boolean showcard){ // only one card get value
        card.CardJoker = this.CardJoker;
        card.cardcolor=this.cardcolor;
        card.cardvalue= this.cardvalue;

        if(showcard == true)
        {
           card.setImage(card.cardcolor, card.cardvalue,false);
        }
        else
        {
           card.setImage(card.cardcolor, card.cardvalue,true); //using this on Cash while drawing a card
        }
    }
    private void Init(final BoardPanel boardPanel) {
        setSize(Card.cardW, Card.cardH);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boardPanel.jButton1MousePressed(evt);
            }
         
        });
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                boardPanel.jButton1MouseDragged(evt);
            }
        });
         
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                boardPanel.jButton1MouseReleased(evt);
            }
        });
    }


    public void setImage(CardColor cardColor, CardValue cardValue, boolean setempty) {
        if ( cardColor == CardColor.NONE) 
        {
            this.setIcon(null);
            return;
        }
            String [] names= { "b", "r", "y", "bl" };
        try {
            int c_index = cardcolor.ordinal();
            String image_name;
            if(setempty==false && this.CardJoker != true)
            {
                 image_name = "pictures/" + names[c_index] + (cardvalue.ordinal()+1) + ".jpg";
            }
            else
            {
                if(this.CardJoker == true)
                {
                    image_name = "pictures/"+ names[c_index] + "JOKER" +  ".jpg";
                }
                else
                {
                     image_name = "pictures/NONE.jpg";
                }
            }
            this.setIcon(new ImageIcon(image_name));
         } 
        catch (Exception ex) {
         }
    }
    
    public void setImageNONE()
    {
        String image_name = "pictures/NONE.jpg";
         
        this.setIcon(new ImageIcon(image_name));
    } 
  
    public void setImageNULL()
    {
        this.cardcolor=CardColor.NONE;
        this.cardvalue=CardValue.NONE;
        this.setIcon(null);
    } 
}

