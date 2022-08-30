
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class BoardPanel extends javax.swing.JPanel {

    /**
     * Creates new form BoardPanel
     */
    public int cols=6;
    public int row=7;
    private Image UnknownPic,BoardPic;
    Sessions session;
    PlayerBoard boardPlayer;
    ComputerBoard boardComputer;
    Deck deck;
    int  X,  Y;
    int XNew, YNew;
    byte States;
    public boolean Took; 
    public boolean Put;
    public boolean CashCardTook;
    
    public BoardPanel() {
        initComponents();
        BoardPic = new ImageIcon("pictures/board.jpg").getImage();
        Dimension d = new Dimension( BoardPic.getWidth(this), BoardPic.getHeight(this) ) ;
        this.setPreferredSize(d);
        // set the bottons
        deck = new Deck(this);
        this.Took =false;
        this.Put = false;
        this.CashCardTook=false;
        
        int randomNum = 0 + (int)(Math.random() * 1); 
        if(randomNum ==0)
        {//computer starts
            Sessions.CurrentTurn=0;
            boardComputer = new  ComputerBoard(this,randomNum+1);
            boardPlayer = new PlayerBoard(this,randomNum);
           // boardComputer.MakeMove();
        }
        else{//player starts
            Sessions.CurrentTurn=1;
           boardPlayer = new PlayerBoard(this,randomNum);
            boardComputer= new ComputerBoard(this, randomNum-1);
            this.Took=true;
            Sessions.MovesThisTurn++;
        }
        session = new Sessions(this,randomNum);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BoardPic, 0, 0, this);
    }
    
    public void jButton1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseDragged
               Card card = (Card) evt.getSource();
               Point   POld =  card.getLocation();
               
               if (  evt.getX() < X )
                   XNew = POld.x - ( X - evt.getX() ) ;
               else
                   XNew = POld.x + (   evt.getX() - X ) ;

               if (  evt.getY() < Y )
                   YNew = POld.y - ( Y - evt.getY() ) ;
               else
                   YNew = POld.y + (   evt.getY() - Y ) ;
               
               card.setLocation( XNew, YNew);
     
      
    }//GEN-LAST:event_jButton1MouseDragged
    public void CloseProgram()
    {
    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // this will make sure WindowListener.windowClosing() et al. will be called.
    WindowEvent wev = new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

    // this will hide and dispose the frame, so that the application quits by
    // itself if there is nothing else around. 
    setVisible(false);
    topFrame.dispose();
    // if you have other similar frames around, you should dispose them, too.

    // finally, call this to really exit. 
    // i/o libraries such as WiiRemoteJ need this. 
    // also, this is what swing does for JFrame.EXIT_ON_CLOSE
    System.exit(0); 
    }
    public void jButton1MouseReleased(java.awt.event.MouseEvent evt) {
        Card card = (Card)evt.getSource();
        Point evtloc = new Point(card.getX(),card.getY());
        
        States |=  PlayerBoard.IsLegalPlayer(evtloc.getX(),evtloc.getY()) ? 16 : 0x0;
        States |=  PlayerBoard.IsLegalPlayerBoard(evtloc.getX(),evtloc.getY()) ? 32 : 0x0;
        
        statesMachine(card);   //stateMachine checks and activates player's move

        if(Sessions.MovesThisTurn==2)
        {
            Sessions.CurrentTurn=1;
            boolean won = this.session.CheckWin(this);
            if(!won && this.CashCardTook==true)        
            {
                JOptionPane.showConfirmDialog(this,"Bad Use of Cash Card! \n Game over" ) ;
                // finish the game!
                this.CloseProgram();
                
            }
            if(!won)
            {
            Sessions.CurrentTurn=0;
            this.boardComputer.EvaluateMove();
            if(this.session.CheckWin(this))
            {
                 JOptionPane.showConfirmDialog(this,"Computer Won!" ) ; 
                 // finish the game!
                 this.CloseProgram();

            }

            Sessions.CurrentTurn=1;
            Sessions.MovesThisTurn=0;
            this.Took =false;
            this.Put = false;
            }
            else
            {
                 JOptionPane.showConfirmDialog(this,"Player Won!" );
                // finish the game!
                this.CloseProgram();
            }
        }
    }
    
    
    public void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        Card card = (Card)evt.getSource();
        X = evt.getX();
        Y =  evt.getY();
        card.Currentlocation=new Point(card.getX(),card.getY());       //right way to get the location of the card on screen!\
       
        Point evtloc = card.Currentlocation; // evt location
        if(card.cardvalue != Card.CardValue.NONE)
        {
        States |=  Deck.IfCash(evtloc) ? 0x1 : 0x0;
        States |= Deck.IfCashCard(evtloc) ? 0x8 : 0x0;
        States |=  PlayerBoard.IsLegalPlayer(evtloc.getX(),evtloc.getY()) ? 2 : 0x0;
        States |=  PlayerBoard.IsLegalPlayerBoard(evtloc.getX(),evtloc.getY()) ? 4 : 0x0;
        States |=  ComputerBoard.IsLegalComputerBoard(evtloc.getX(),evtloc.getY()) ? 64 : 0x0;
        }
    }//GEN-LAST:event_jButton1MousePressed
    
    public void statesMachine(Card cardpressed)
    {
        boolean flag=false;
        if(Sessions.canplay ==true)
        {
         switch(this.States){
                case(18):  
                    state1(cardpressed);
                    break;
                case(34):    
                    state2(cardpressed);
                    break;
                case(24):  
                    state3(cardpressed);
                    break;
                case(17):    
                    state4(cardpressed);
                    break;
                case(80):    
                    state5(cardpressed);
                    break;
                default:    
                    illegalmove(cardpressed);
                    flag =true;
                    break;
         }
        
       
           if(this.States != 18 && flag != true)
           {
               Sessions.MovesThisTurn++;;
           }
           //after state completed make states in BoardPanel Zero.
           this.States=0;
        }
        else// in case the game is over
        {
            
        }
    }
    public void illegalmove(Card card)
    {
        //in case of illegal move this function activates
        card.setLocation(card.Currentlocation);
        System.out.println("illeagal move!");
    }
    public void state1(Card card)
    {
        //from player's hand to player's hand

        Point cardloc =new Point (card.getX(),card.getY());
        card.setLocation(card.Currentlocation);
        Card s = (Card)this.getComponentAt(cardloc);
        card.SwapCards(s, this);
        repaint();
    }
    public void state2(Card card)
    {
        //from player's hand to PlayerBoard

        Point cardloc =new Point (card.getX(),card.getY());
        card.setLocation(card.Currentlocation);
        Card s = (Card)this.getComponentAt(cardloc);
        if(s.cardcolor == Card.CardColor.NONE && this.Put==false)
        {
           card.SwapCards(s, this);
           repaint();
           this.boardPlayer.lastCardTrowen=s;
           this.Put=true;
        }
        else
        {
           Sessions.MovesThisTurn--;
           return;
        }
        System.out.println("Player threw a card on his boardgame");
    }
    public void state3(Card card)
    {
        //from Deck's CashCard to player's hand
        Point cardloc =new Point (card.getX(),card.getY());
        card.setLocation(card.Currentlocation);
        Card s = (Card)this.getComponentAt(cardloc);
        if(s.cardcolor == Card.CardColor.NONE && this.Took==false)
        {
           card.SwapCards(s, this);
           repaint();
           this.Took=true;
           // check the winning condition!
           this.CashCardTook=true;
        }
        else
        {
           Sessions.MovesThisTurn--;
           return;
        }  
        System.out.println("Player draw the \"cash card\"");
        

    }
    
     public void state4(Card card)
    {
        //from Cash to Player's hand ///////////
        Point cardloc =new Point (card.getX(),card.getY());
        card.setLocation(card.Currentlocation);
        Card s = (Card)this.getComponentAt(cardloc);
        
        if(s.cardcolor == Card.CardColor.NONE && this.Took==false)
        {
           card.HalfSwapCards(s, this,true);
           this.deck.TakeFromCash(this);
           repaint();
           this.Took=true;
        }
        else
        {
           Sessions.MovesThisTurn--;
           return;
        }  
       System.out.println("Player draw from cash");

        
    }
      public void state5(Card card)
    {
        //from ComputerBoard to player's
        Point cardloc =new Point (card.getX(),card.getY());
        card.setLocation(card.Currentlocation);
        Card s = (Card)this.getComponentAt(cardloc);
        
        if(s.cardcolor == Card.CardColor.NONE && card == this.boardComputer.lastCardTrowen && this.Took==false)
        {
           card.SwapCards(s, this);
           repaint();
           this.Took=true;
        }
          else
        {
           Sessions.MovesThisTurn--;
           return;
        }  
        System.out.println("Player draw the Computer's last card");
    }
    
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
