import javax.swing.JFrame;
import java.net.Socket;
import java.io.IOException;
import java.net.ConnectException;

/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Creates a game client
*/
public class GameTester
{
    
    public static void main ( String[] args ) throws IOException
    {

        JFrame window = new JFrame();
        window.setSize( 800, 450 );
        window.setResizable( false );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        try
        {
            
            //Creating connecton to server
            Socket serverConnection = new Socket( "localhost", GameConstants.OPERATING_PORT );
            
            //Creating players
            PlayerControlledCharacter localPlayer = new PlayerControlledCharacter( 150, GameConstants.LOCAL_PLAYER );
            PlayerControlledCharacter remotePlayer = new PlayerControlledCharacter( 150, GameConstants.REMOTE_PLAYER );

            //Creating game client
            GameClient game = new GameClient( localPlayer, remotePlayer, serverConnection );

            //Adding game to frame
            window.add( game );
            game.requestFocusInWindow();

            //Setting frame to visible
            window.setVisible( true );
            
        }
        catch ( ConnectException e ) { System.out.println( "Server is unavailable." ); }

    }
    
}