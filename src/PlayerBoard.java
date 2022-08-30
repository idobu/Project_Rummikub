


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author home
 */
public class PlayerBoard extends Player{
    Card [][]  PlayerBoardcells = new Card[Deck.ROWS][Deck.COLS];
    static int PlayerBoardbottonStartPutX=320;
    static int PlayerBoardbottonStartPutY=300;
    public static int deltaX = 30, deltaY = 45;
    Card lastCardTrowen;
    
    
    
    
    public PlayerBoard(BoardPanel boardPanel,int starter) {
        super(boardPanel,starter);
        int y =PlayerBoardbottonStartPutY ,x;
        for(int i=0; i<Deck.ROWS; i++)
        {
            x=PlayerBoardbottonStartPutX;
            for(int j=0; j<Deck.COLS; j++,x+=deltaX)
            {
                PlayerBoardcells[i][j] = new Card(boardPanel); 
                PlayerBoardcells[i][j].setLocation(x, y);          
                boardPanel.add(PlayerBoardcells[i][j]);
            }
            y+=deltaY;
        }  
        
    }

   public static boolean IsLegalPlayerBoard(double x, double y) {
        return  ((x>= PlayerBoardbottonStartPutX && x<PlayerBoardbottonStartPutX+ Deck.COLS*deltaX) && // this PlayerBoard
                (y>= PlayerBoardbottonStartPutY && y<PlayerBoardbottonStartPutY+ Deck.ROWS*deltaY));
                
    }
    public static boolean IsLegalPlayer(double x, double y) {
        return (x>= PlayerbottonStartPutX && x<PlayerbottonStartPutX+ Deck.COLS*deltaX && // this Player
                (y>= PlayerbottonStartPutY && y<PlayerbottonStartPutY+ Deck.ROWS*deltaY));
    }

    boolean IfCard(Card card) {
        for(int i=0; i< Deck.ROWS; i++) {
            for (int j = 0; j < cards[i].size(); j++) {            //CHANGE TO VALUE OF THE ADDRESS AND NOT CARDVALUE
                if ( card.cardcolor == ((Card)cards[i].get(j)).cardcolor && 
                     card.cardvalue == ((Card)cards[i].get(j)).cardvalue )
                        return true;
                
            }
        }
       return false;
    }

    public  void removeLastcard() {
        for(int i =0 ;i< Deck.ROWS; i++)
        {
            for(int j =0 ; j< Deck.COLS; j++)
            {
                if(this.PlayerBoardcells[i][j] == this.lastCardTrowen)
                {
                    this.PlayerBoardcells[i][j].setImageNULL();
                    return;
                }
            }
        }
    }
}
