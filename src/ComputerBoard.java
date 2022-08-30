
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author home
 */
public class ComputerBoard extends Computer{
    Card [][]  cells = new Card[Deck.ROWS][Deck.COLS];
    static int TopButtonStartY=37;
    static int TopButtonStartX=PlayerBoard.PlayerBoardbottonStartPutX-230;
    Card lastCardTrowen;
    
    
    public ComputerBoard(BoardPanel boardPanel,int starter){
        super(boardPanel,starter);
        int y = TopButtonStartY, x;
        for(int i=0; i<Deck.ROWS; i++)
        {
            x=TopButtonStartX;
            for(int j=0; j<Deck.COLS; j++,x+=boardPanel.boardPlayer.deltaX)
            {
                cells[i][j] = new Card(boardPanel); 
                cells[i][j].setLocation(x, y);          
                boardPanel.add(cells[i][j]);
            }
            y+=boardPanel.boardPlayer.deltaY;
        }

}
    
    public Card ClearPlaceCard()
    {
        for(int i =0 ; i < Deck.ROWS; i++)
        {
            for(int j=0; j < Deck.COLS; j++)
            {
                if(this.cells[i][j].cardcolor == Card.CardColor.NONE)
                {
                    return cells[i][j];
                }
            }
        }
        return null;
    }
    
    
    public static boolean IsLegalComputerBoard(double x, double y) {
        return (x >= TopButtonStartX && x < TopButtonStartX + Deck.COLS * PlayerBoard.deltaX && // this Player
                (y >= TopButtonStartY && y < TopButtonStartY + Deck.ROWS * PlayerBoard.deltaY));
    }
}
