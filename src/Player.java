
import java.awt.Button;
import java.awt.Point;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 206385502
 */
public class Player {
    Card [][]  cells = new Card[Deck.ROWS][Deck.COLS];
    ArrayList []  cards = new ArrayList[Deck.ROWS];
    BoardPanel boardPanel;
     int starter;
     static int MovesThisTurn;
     static int PlayerbottonStartPutX=320;
     static int PlayerbottonStartPutY=493;
    
    public Player(BoardPanel boardPanel,int starter) {
        int y=Player.PlayerbottonStartPutY;
        this.boardPanel = boardPanel;
        this.starter=starter;
        this.MovesThisTurn=0;
        int x;
        for (int i = 0; i < Deck.ROWS; i++) {
            cards[i] = new ArrayList();
        }
        for(int i=0; i<Deck.ROWS; i++)
        {
            x=PlayerbottonStartPutX;
            for(int j=0; j<Deck.COLS; j++,x+=30)
            {
                cells[i][j] = new Card(boardPanel); 
                cells[i][j].setLocation(x, y);          
                boardPanel.add(cells[i][j]);
            }
            y+=44.8;
        }  
        Init();
    }

    private void Init() {
        int num = starter ==1? 15 : 14;
         for (int i = 0; i < num; i++) {
            Card card = this.boardPanel.deck.cards.pop();
            FindAndPlace(card);
        }       

    }
    public void FindAndPlace(Card card){
        boolean flag =false;
        cards[card.cardcolor.ordinal()].add(card);
        for(int i =0 ;i<Deck.ROWS && !flag;i++)
        {
            for(int j =0 ;j<Deck.COLS && !flag;j++)
            {
                if(cells[i][j].cardvalue==Card.CardValue.NONE && cells[i][j].cardcolor==Card.CardColor.NONE)
                {
                     cells[i][j].SwapCards(card,boardPanel);
                     flag=true;
                }
            }
        }
    }
}
