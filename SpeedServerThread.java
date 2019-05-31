
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;


/**
 * Speed Server Thread to handle one client connection.
 *
 * @author Sunny Guan
 * @version May 30, 2019
 * @author Period: 5
 * @author Assignment: Speed
 *
 * @author Sources: None
 */
public class SpeedServerThread extends Thread
{
    private int id = -1;

    private Socket socket = null;

    private SpeedServerThread otherPlayer = null;

    private Speed game;

    private String name;

    /**
     * @param socket
     *            Client socket
     * @param game
     *            Game controller
     * @param id
     *            first or second player
     */
    public SpeedServerThread( Socket socket, Speed game, int id )
    {
        super( "KKMultiServerThread" );
        this.id = id;
        this.socket = socket;
        this.game = game;
    }

    /**
     * Get player name
     * 
     * @return player name
     */
    public String getPlayerName()
    {
        return name;
    }

    /**
     * Get instance of the opponent
     * 
     * @return the opponent
     */
    public SpeedServerThread getOtherPlayer()
    {
        return otherPlayer;
    }

    /**
     * Set the other player
     * 
     * @param otherPlayer
     *            the opponent
     */
    public void setOtherPlayer( SpeedServerThread otherPlayer )
    {
        this.otherPlayer = otherPlayer;
    }

    /**
     * Get output stream to the other player's socket
     * 
     * @return
     */
    public PrintWriter getOut()
    {
        return out;
    }

    /**
     * Set output stream to the opponent's socket
     * 
     * @param out
     *            output Print Writer
     */
    public void setOut( PrintWriter out )
    {
        this.out = out;
    }

    private PrintWriter out;

    @Override
    // game logic
    // handles input from clients and respond according to the protocol
    public void run()
    {
        try
        {
            out = new PrintWriter( socket.getOutputStream(), true );

            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            String inputLine = in.readLine();
            name = inputLine.substring( 5 );
            System.out.println( name + " joined the server." );

            if ( id == 1 )
            {
                for ( Card c : game.getHand1() )
                {
                    out.println( "HANDADD|" + c.toString() );
                }
            }
            else
            {
                for ( Card c : game.getHand2() )
                {
                    out.println( "HANDADD|" + c.toString() );
                }
            }
            out.println( "SETPILE|" + game.getCentralPile1().get( 0 ) + "|1" );
            out.println( "SETPILE|" + game.getCentralPile2().get( 0 ) + "|2" );

            while ( ( inputLine = in.readLine() ) != null )
            {
                if ( inputLine.equals( "STUCK" ) )
                {
                    game.stuckCount++;

                    if ( game.stuckCount == 2 )
                    {
                        game.flipSideDeck();
                        String msg1 = "SETPILE|" + game.getCentralPile1Card().toString() + "|1|T";
                        String msg2 = "SETPILE|" + game.getCentralPile2Card().toString() + "|2|T";
                        out.println( msg1 );
                        out.println( msg2 );
                        otherPlayer.out.println( msg1 );
                        otherPlayer.out.println( msg2 );
                        game.stuckCount = 0;
                        System.out.println( "Both players stuck." );
                    }
                }
                else if ( inputLine.equals( "UNSTUCK" ) )
                {
                    game.stuckCount--;
                }
                else if ( inputLine.startsWith( "HANDREMOVE" ) )
                {
                    game.removeCard( id, inputLine.split( "\\|" )[1] );
                    ArrayList<Card> hand = getHand( id );
                    Stack<Card> deck = getDeck( id );
                    if ( hand.isEmpty() && deck.isEmpty() )
                    {
                        gameOverAction();
                        break;
                    }
                }
                else if ( inputLine.startsWith( "MOVETOPILE" ) )
                {
                    Card c = new Card( inputLine.split( "\\|" )[1] );
                    int pileNum = Integer.parseInt( inputLine.split( "\\|" )[2] );
                    boolean canSet = game.set( id, c, pileNum );
                    if ( canSet )
                    {
                        game.removeCard( id, inputLine.split( "\\|" )[1] );
                        out.println( "HANDREMOVE|" + c );
                        String msg = "SETPILE|" + inputLine.split( "\\|" )[1] + "|" + inputLine.split( "\\|" )[2];
                        out.println( msg );
                        otherPlayer.out.println( msg );

                        ArrayList<Card> hand = getHand( id );
                        Stack<Card> deck = getDeck( id );
                        if ( hand.isEmpty() && deck.isEmpty() )
                        {
                            gameOverAction();
                            break;
                        }
                    }
                }
                else if ( inputLine.startsWith( "REFILL" ) )
                {
                    ArrayList<Card> hand = getHand( id );
                    Stack<Card> deck = getDeck( id );
                    int refillIndex = Integer.parseInt( inputLine.split( "\\|" )[1] );

                    while ( hand.size() < 5 )
                    {
                        if ( !deck.isEmpty() )
                        {
                            Card c = deck.pop();
                            hand.add( c );
                            out.println( "HANDADD|" + c.toString() );
                        }
                        else
                        {
                            out.println( "DEACTCARD" );
                            otherPlayer.out.println( "DECKEMPTY2|" + refillIndex );
                            break;
                        }
                    }
                }
                else if ( inputLine.startsWith( "FGAMEOVER" ) )
                { // TODO testing only!
                    gameOverAction();
                    // break;
                }

                System.out.println( inputLine );
                System.out.println( "Left in deck: " + game.getDeck1().size() + ", " + game.getDeck2().size() );
            }
            socket.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

    }

    /**
     * Helper method to get this client's hand
     * 
     * @param id
     *            first or second player
     * @return ArrayList of the player's hand
     */
    public ArrayList<Card> getHand( int id )
    {
        return id == 1 ? game.getHand1() : game.getHand2();
    }

    /**
     * Helper method to get this client's deck
     * 
     * @param id
     *            first or second player
     * @return stack of the player's deck
     */
    public Stack<Card> getDeck( int id )
    {
        return id == 1 ? game.getDeck1() : game.getDeck2();
    }

    /**
     * Game over output to console and output streams
     */
    public void gameOverAction()
    {
        out.println( "YOUWIN" );
        otherPlayer.out.println( "YOULOSE" );
        System.out.println( "Game over! " + name + " wins! " + otherPlayer.getPlayerName() + " loses!" );
    }

    /**
     * Remove a card from hand
     * 
     * @param id
     *            first or second player
     * @param c
     *            card to remove
     * @return if the removal was successful
     */
    public boolean remove( int id, Card c )
    {
        ArrayList<Card> hand = id == 1 ? game.getHand1() : game.getHand2();
        return hand.remove( c );
    }
}
