import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Game server. Handles communication between to clients.
*/

public class GameServer
{

    public static void main ( String[] args ) throws IOException
    {
        
        //Clients
        Socket player1 = null, player2 = null;
        
        //Creating server for clients to connect to.
        ServerSocket server = new ServerSocket( GameConstants.OPERATING_PORT );
        System.out.println( "Waiting for players." );

        //Waiting for players to connect.
        while ( true )
        {

            //Getting connection to client
            Socket socket = server.accept();

            //If player 1 has not connected yet
            if ( player1 == null )
            {
                
                //Store connection as player 1's.
                player1 = socket;
                System.out.println( "Player one connected." );

            }
            //If player 1 has connected and player 2 has not connected
            else
            {

                //Store connection as player 2's.
                player2 = socket;
                System.out.println( "Player two connected." );
                
                //Create new thread to handle game
                Thread gameThread = new Thread( new GameHandler( player1, player2 ) );
                
                //Start new game.
                gameThread.start();
                
                //Reset connections in order to wait for more.
                player1 = null;
                player2 = null;

            }

        }

    }

}

/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Sends data between two clients in a threaded manner.
*/

class GameHandler implements Runnable
{

    private Scanner playerOneInput, playerTwoInput;
    private PrintWriter playerOneOutput, playerTwoOutput;
    
    /**
        Constructor.
        @param Socket Player 1
        @param Socket Player 2
    */
    public GameHandler ( Socket inPlayer1, Socket inPlayer2 ) throws IOException
    {

        //Creates inputs from both player.
        this.playerOneInput = new Scanner( inPlayer1.getInputStream() );
        this.playerTwoInput = new Scanner( inPlayer2.getInputStream() );
        
        //Creates outputs to both players.
        this.playerOneOutput = new PrintWriter( inPlayer1.getOutputStream() );
        this.playerTwoOutput = new PrintWriter( inPlayer2.getOutputStream() );

    }

    /**
        Called when thread is started. Starts game.
    */
    public void run ()
    {

        try
        {

            System.out.println( "Starting game." );
            
            //Sends each player their player number.
            this.playerOneOutput.println( GameConstants.PLAYER_1 );
            this.playerTwoOutput.println( GameConstants.PLAYER_2 );
            this.playerOneOutput.flush();
            this.playerTwoOutput.flush();
            
            //While game is running
            while ( true )
            {
                
                //Send input from player one to player two.
                this.playerTwoOutput.println( playerOneInput.nextLine() );
                this.playerTwoOutput.flush();
                
                //Send input from player two to player one.
                this.playerOneOutput.println( playerTwoInput.nextLine() );
                this.playerOneOutput.flush();

            }

        }
        catch ( NoSuchElementException e )
        {
            
            //Some player disconnected. Game is over.
            System.out.println( "Game disconnected." );

        }

    }

}