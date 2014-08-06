import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.Point;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Handles communication with server and user input. Stores Field, which handles graphical elements of game.
*/

public class GameClient extends JPanel implements ActionListener, KeyListener, MouseListener, DeathListener
{

    //Time in milliseconds between each frame update
    private static final int SIXTY_FRAMES_PER_SECOND = 16;

    //Timer, each tick updates actions of players
    private Timer frameRate;
    
    //Game field, responsible for handling player's actions and graphics elements.
    private Field gameField;
    
    //Each player in the game.
    private PlayerControlledCharacter localPlayer;
    private PlayerControlledCharacter remotePlayer;
    
    //Server IO
    private Scanner serverInput;
    private PrintWriter serverOutput;

    public GameClient ( PlayerControlledCharacter inLocalPlayer, PlayerControlledCharacter inRemotePlayer, Socket inServerConnection ) throws IOException
    {
        
        //Construct panel with absolute positioning
        super( null );

        //Set size of panel, make it focusable ( for input ).
        this.setBounds( 0, 0, 800, 450 );
        this.setFocusable( true );

        this.localPlayer = inLocalPlayer;
        this.remotePlayer = inRemotePlayer;

        //Set this object as each player's death listener.
        //This allows players to send a message to this class when their health reaches 0, implying the end of the game.
        this.localPlayer.setDeathListener( this );
        this.remotePlayer.setDeathListener( this );

        //Create connection with server. Will not construct until both players are connected to server.
        System.out.println( "Waiting for other player..." );

        this.serverOutput = new PrintWriter( inServerConnection.getOutputStream() );
        this.serverInput = new Scanner( inServerConnection.getInputStream() );

        while ( !this.serverInput.hasNext() ) {}

        //Get which player this client is from the server.
        int player = this.serverInput.nextInt();

        //If this client is player 1
        if ( player == GameConstants.PLAYER_1 )
        {

            //Set local player to player 1
            this.localPlayer.setPlayer( GameConstants.PLAYER_1 );
            
            //Set remote player to player 2
            this.remotePlayer.setPlayer( GameConstants.PLAYER_2 );

        }
        //If this client is player 2
        else
        {
            
            //Set local player to player 2
            this.localPlayer.setPlayer( GameConstants.PLAYER_2 );
            
            //Set remote player to player 2
            this.remotePlayer.setPlayer( GameConstants.PLAYER_1 );

        }

        //Construct the game field to handle graphical elements of game
        this.gameField = new Field( inLocalPlayer, inRemotePlayer );

        //Construct and start the frame rate for the game.
        this.frameRate = new Timer( SIXTY_FRAMES_PER_SECOND, this );
        this.frameRate.start();
        
        //Set the cursor to the custom crosshair cursor.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.setCursor( toolkit.createCustomCursor( toolkit.getImage( "crosshair.png" ), new Point( 7, 7 ), "crosshair" ) );

        //Set this class as it's down key and mouse listener
        this.addKeyListener( this );
        this.addMouseListener( this );
        
        //Add the game field to the panel.
        this.add( this.gameField );

    }
    
    /**
        Called when a player's health reaches 0. Ends the game.
        @param int Player that died
    */
    public void deathOccured ( int inPlayer )
    {

        //If local player died
		if ( inPlayer == this.localPlayer.getPlayer() )
		{
		    
		    //Stop processing user input
		    this.frameRate.stop();
		    
			//Display defeat graphic
			JLabel image = new JLabel( new ImageIcon( "loser.png" ) );
            image.setBounds( 200, 96, 400, 200 );
            this.gameField.setVisible( false );
            this.add( image );
			
		}
		//If remote player died
		else
		{
			
			//Stop processing user input
			this.frameRate.stop();

			//Display victory graphic
			JLabel image = new JLabel( new ImageIcon( "winner.png" ) );
            image.setBounds( 200, 96, 400, 200 );
            this.gameField.setVisible( false );
            this.add( image );

		}

	}

    /**
        Called when user clicks the mouse within the game window. Implies attacking.
        @param MouseEvent Details of the mouse click
    */
    public void mousePressed ( MouseEvent e )
    {
        
        //Store locations of player and mouse
        int playerX = this.localPlayer.getX() + ( this.localPlayer.getWidth() / 2 ),
                playerY = this.localPlayer.getY() + ( this.localPlayer.getWidth() / 2 ),
                mouseX = e.getX(),
                mouseY = e.getY();

        //Get difference between player and mouse coordinates
        int deltaX = mouseX - playerX,
            deltaY = mouseY - playerY;

        //Determine angle between mouse and player
        double theta = -Math.atan( deltaY / ( double ) deltaX );

        //Adjust angle, because the atan function is nasty.
        if ( deltaX < 0 ) { theta += Math.PI; }

        //Create a melee attack based on information above
        MeleeAttack attack = new MeleeAttack( this.localPlayer.getPlayer(), theta, playerX, playerY, 35 );

        //Set local player's attack to this attack.
        this.localPlayer.setCurrentAttack( attack );
        
        //Send attack to remote player
        this.serverOutput.println( attack );
        this.serverOutput.flush();

    }

    /**
        Called by tick of the frame rate timer
        @param ActionEvent details of event
    */
    public void actionPerformed ( ActionEvent e )
    {
        
        //Get current attack of the local player
        MeleeAttack currentAttack = this.localPlayer.getCurrentAttack();

        //Check if attack hits opposing player
        if ( currentAttack != null && !currentAttack.hasDoneDamage() && currentAttack.intersects( this.remotePlayer.getX() + 16, this.remotePlayer.getY() + 16, 32, 32 ) )
        {

            //Attack hit, attacks can only do damage once, so, turn off attack's damaging property.
            currentAttack.damageDone();

            //Create a new health change for opposing player for the amount of damage the attack does.
            HealthChange change = new HealthChange( this.remotePlayer.getPlayer(), -currentAttack.getDamage() );

            //Add health change to local player's actions.
            //This ensures both players will receive the health change ( see next few lines )
            this.localPlayer.addAction( change );

        }

        //Get local player's current Action
        Action localAction = this.localPlayer.getCurrentAction();

        //Sends local player's current action to remote player.
        this.serverOutput.println( localAction );
        this.serverOutput.flush();

        //Processes local player's current action locally.
        this.gameField.playerAction( localAction );

        //Gets remote player's current action from server
        String command = this.serverInput.nextLine();

        //If the command is not empty or null
        if ( !command.equals( "null" ) && !command.equals( "" ) )
        {

            //Check if command is to remove an action
            if ( command.substring( 0, 6 ).equals( "REMOVE" ) )
            {
                
                //Remote action encoded in server's command
                this.remotePlayer.removeAction( Action.convertStringToAction( command.substring( 7 ) ) );

            }
            //Check if command is an attack
            else if ( command.substring( 0, 6 ).equals( "ATTACK" ) )
            {
                
                //Set remote player's attack to server's command
                this.remotePlayer.setCurrentAttack( ( MeleeAttack ) Action.convertStringToAction( command ) );

            }
            //Otherwise, command is a movement or health change
            else
            {
                
                //Add to remote player's action queue
                this.remotePlayer.addAction( Action.convertStringToAction( command ) );

            }

        }

        //Locally process remote player's action
        this.gameField.playerAction( this.remotePlayer.getCurrentAction() );
        
        //Repaint player's characters ( to draw attacking )
        this.localPlayer.repaint();
        this.remotePlayer.repaint();

    }

    /**
        Called when user presses a key on keyboard. Implies movement.
        @param KeyEvent details of key press
    */
    public void keyPressed ( KeyEvent e )
    {

        //Creates movement object for the local player
        Move move = new Move( this.localPlayer.getPlayer() );

        //Get character representation of the key that was pressed.
        char keyPressed = Character.toLowerCase( e.getKeyChar() );

        //Checking if the key that was pressed was a key that corresponds to movement keys
        //If it does, setting the proper direction of motion based on that key
        if ( keyPressed == 'w' ) { move.setDirection( GameConstants.NORTH ); }
        if ( keyPressed == 'e' ) { move.setDirection( GameConstants.EAST ); }
        if ( keyPressed == 'q' ) { move.setDirection( GameConstants.WEST ); }
        if ( keyPressed == 'a' ) { move.setDirection( GameConstants.WEST ); }
        if ( keyPressed == 's' ) { move.setDirection( GameConstants.SOUTH ); }
        if ( keyPressed == 'd' ) { move.setDirection( GameConstants.EAST ); }

        //Adding movement to local player's action queue
        this.localPlayer.addAction( move );

    }

    /**
        Called when user releases a key on the keyboard. Implies stopping movement.
        @param KeyEvent details of key release
    */
    public void keyReleased ( KeyEvent e )
    {
        
        //Creates movement object for the local player
        Move move = new Move( this.localPlayer.getPlayer() );
        
        //Gets character representation of the key that was released
        char keyPressed = Character.toLowerCase( e.getKeyChar() );

        if ( keyPressed == 'w' ) { move.setDirection( GameConstants.NORTH ); }
        if ( keyPressed == 'e' ) { move.setDirection( GameConstants.EAST ); }
        if ( keyPressed == 'q' ) { move.setDirection( GameConstants.WEST ); }
        if ( keyPressed == 'a' ) { move.setDirection( GameConstants.WEST ); }
        if ( keyPressed == 's' ) { move.setDirection( GameConstants.SOUTH ); }
        if ( keyPressed == 'd' ) { move.setDirection( GameConstants.EAST ); }

        //Remove movement from local player's action queue
        this.localPlayer.removeAction( move );
        
        //Send remove command to server.
        this.serverOutput.println( "REMOVE " + move );
        this.serverOutput.flush();


    }
    
    //Requred for KeyListener interface, but are not used.
    public void keyTyped ( KeyEvent e ) {}
    
    //Methods required by MouseListener interface, but are not used.
    public void mouseClicked ( MouseEvent e ) {}
    public void mouseEntered ( MouseEvent e ) {}
    public void mouseExited ( MouseEvent e ) {}
    public void mouseReleased( MouseEvent e ) {}

}