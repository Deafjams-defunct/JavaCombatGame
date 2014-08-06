import javax.swing.JPanel;
import java.util.Scanner;
import java.io.PrintWriter;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Handles player actions and graphical elements of the game.
*/
public class Field extends JPanel
{

    //Constants used to represent players in the game, bounds of the field.
    private static final int MAX_X = 794, MAX_Y = 423;
    
    //Instances of both players on the field
    private PlayerControlledCharacter localPlayer;
    private PlayerControlledCharacter remotePlayer;
    
    //Health bars for both player
    private Healthbar bar1, bar2;
    
    //List of solid objects on the field.
    private Solid[] solids;

    public Field ( PlayerControlledCharacter inOne, PlayerControlledCharacter inTwo )
    {

        //Construct JPanel with absolute positioning
        super( null );

        //Set size of Field
        this.setBounds( 0, 0, 800, 450 );

        this.localPlayer = inOne;
        this.remotePlayer = inTwo;

        //Create health bar for player 1, set size and location
        this.bar1 = new Healthbar( inOne.getMaxHealth(), "Player 1" );
        bar1.setBounds( 7, Field.MAX_Y - 40, 250, 40 );
        
        //Create health bar for player 2, set size and location
        this.bar2 = new Healthbar( inTwo.getMaxHealth(), "Player 2" );
        bar2.setBounds( Field.MAX_X - 250, Field.MAX_Y - 40, 250, 40 );

        //Create and position pillars on field
        Pillar pillar1 = new Pillar();
        pillar1.setBounds( ( Field.MAX_X / 4 ) - 50, ( Field.MAX_Y / 4 ) - 50, 100, 100 );

        Pillar pillar2 = new Pillar();
        pillar2.setBounds( ( Field.MAX_X / 4 ) - 50, 3 * ( Field.MAX_Y / 4 ) - 50, 100, 100 );

        Pillar pillar3 = new Pillar();
        pillar3.setBounds( 3 * ( Field.MAX_X / 4 ) - 50, ( Field.MAX_Y / 4 ) - 50, 100, 100 );

        Pillar pillar4 = new Pillar();
        pillar4.setBounds( 3 * ( Field.MAX_X / 4 ) - 50, 3 * ( Field.MAX_Y / 4 ) - 50, 100, 100 );

        //Add solid objects to list of solids
        this.solids = new Solid[ 6 ];
        this.solids[ 0 ] = this.localPlayer;
        this.solids[ 1 ] = this.remotePlayer;
        this.solids[ 2 ] = pillar1;
        this.solids[ 3 ] = pillar2;
        this.solids[ 4 ] = pillar3;
        this.solids[ 5 ] = pillar4;

        //Add all objects on field to panel
        this.add( bar1 );
        this.add( bar2 );
        this.add( pillar1 );
        this.add( pillar2 );
        this.add( pillar3 );
        this.add( pillar4 );
        this.add( this.localPlayer );
        this.add( this.remotePlayer );
        
        //If local player is player 1
        if ( this.localPlayer.getPlayer() == GameConstants.PLAYER_1 )
        {
            
            //Place player's characters in proper location
            this.localPlayer.setBounds( 233, 180, 64, 64 );
            this.remotePlayer.setBounds( 508, 180, 64, 64 );

        }
        //If local player is player 2
        else
        {

            //Place player's characters in proper location
            this.localPlayer.setBounds( 508, 180, 64, 64 );
            this.remotePlayer.setBounds( 233, 180, 64, 64 );

        }

    }
    
    /**
        Interprets and processes Actions
        @param Action Action to process.
    */
    public void playerAction ( Action inAction )
    {
        
        //If Action is represents a Movement
        if ( inAction instanceof Move )
        {

            Move move = ( Move ) inAction;
            int direction = move.getDirection();
            
            //If movement is for the local player
            if ( move.getPlayer() == this.localPlayer.getPlayer() )
            {

                int localPlayerX = this.localPlayer.getX(), localPlayerY = this.localPlayer.getY();
                int deltaX = 0, deltaY = 0;
                
                //If the direction is NORTH (up), and player won't exceed upper bounds of the field
                if ( direction == GameConstants.NORTH && localPlayerY > 5 )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = 0;
                    deltaY = -5;

                }
                //If the direction is EAST (right),and player won't exceed outer bounds of the field
                else if ( direction == GameConstants.EAST && localPlayerX < ( MAX_X - 69 ) )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = 5;
                    deltaY = 0;

                }
                //If the direction is SOUTH (down),and player won't exceed lower bounds of the field
                else if ( direction == GameConstants.SOUTH && localPlayerY < ( MAX_Y - 110 ) )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = 0;
                    deltaY = 5;

                }
                //If the direction is WEST (left),and player won't exceed outer bounds of the field
                else if ( direction == GameConstants.WEST && localPlayerX > 5 )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = -5;
                    deltaY = 0;

                }
                
                boolean willCollide = false;
                int i = 0;
                
                //Cycling through all solid objects until a collision is detected
                while ( i < this.solids.length && !willCollide )
                {
                    
                    //Checking collision, as long as solid does not represent the local player
                    if ( !this.solids[ i ].equals( this.localPlayer ) ) { willCollide = this.solids[ i ].willCollide( localPlayerX + 16 + deltaX, localPlayerY + 16 + deltaY, 32, 32 ); }
                    i++;
                    
                }

                //If player will not collide with anything on the field
                if ( !willCollide ) { localPlayer.setLocation( localPlayerX + deltaX, localPlayerY + deltaY ); }

            }
            else
            {

                int remotePlayerX = this.remotePlayer.getX(), remotePlayerY = this.remotePlayer.getY();
                int deltaX = 0, deltaY = 0;

                //If the direction is NORTH (up), and player won't exceed upper bounds of the field
                if ( direction == GameConstants.NORTH && remotePlayerY > 5 )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = 0;
                    deltaY = -5;

                }
                //If the direction is EAST (right),and player won't exceed outer bounds of the field
                else if ( direction == GameConstants.EAST && remotePlayerX < ( MAX_X - 69 ) )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = 5;
                    deltaY = 0;

                }
                //If the direction is SOUTH (down),and player won't exceed lower bounds of the field
                else if ( direction == GameConstants.SOUTH && remotePlayerY < ( MAX_Y - 110 ) )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = 0;
                    deltaY = 5;

                }
                //If the direction is WEST (left),and player won't exceed outer bounds of the field
                else if ( direction == GameConstants.WEST && remotePlayerX > 5 )
                {
                    
                    //Set number of pixels to change in each direction
                    deltaX = -5;
                    deltaY = 0;

                }
                
                boolean willCollide = false;
                int i = 0;
            
                //Cycling through all solid objects until a collision is detected
                while ( i < this.solids.length && !willCollide )
                {
                    
                    //Checking collision, as long as solid does not represent the local player
                    if ( !this.solids[ i ].equals( this.remotePlayer ) ) { willCollide = this.solids[ i ].willCollide( remotePlayerX + 16 + deltaX, remotePlayerY + 16 + deltaY, 32, 32 ); }
                    i++;
                    
                }

                //If player will not collide with anything on the field
                if ( !willCollide ) { remotePlayer.setLocation( remotePlayerX + deltaX, remotePlayerY + deltaY ); }

            }
            
            //Redraws field
            this.repaint();

        }
        else if ( inAction instanceof HealthChange )
        {

            HealthChange healthChange = ( HealthChange ) inAction;
            int healthChangePlayer = healthChange.getPlayer(), localPlayer = this.localPlayer.getPlayer();
            
            //Removing health change from each player to avoid removing health more than once
            this.localPlayer.removeAction( healthChange );
            this.remotePlayer.removeAction( healthChange );
            
            //Updating health in players and health bars
            this.updateHealth( healthChangePlayer, healthChange.getHealthChange() );

        }

    }
    
    /**
        Helper method, updates health in players and health bars.
        @param int Player to change health of
        @param int amount of health to change
    */
    private void updateHealth ( int inPlayer, int inHealthChange )
    {
        
        //If change is for player 1 and player 1 is local player
        if ( inPlayer == GameConstants.PLAYER_1 && this.localPlayer.getPlayer() == GameConstants.PLAYER_1 )
        {
            
            //Change health for player
            this.localPlayer.adjustHealth( inHealthChange );
            
            //Change health for health bar
            this.bar1.setHealth( this.localPlayer.getCurrentHealth() );
            
            //Redraw proper health bar
            bar1.repaint();

        }
        //If change is for player 1 and player 2 is local player
        else if ( inPlayer == GameConstants.PLAYER_1 && this.localPlayer.getPlayer() == GameConstants.PLAYER_2 )
        {
            
            //Change health for player
            this.remotePlayer.adjustHealth( inHealthChange );
            
            //Change health for health bar
            this.bar1.setHealth( this.remotePlayer.getCurrentHealth() );
            
            //Redraw proper health bar
            bar1.repaint();

        }
        //If change is for player 2 and player 1 is local player
        else if ( inPlayer == GameConstants.PLAYER_2 && this.localPlayer.getPlayer() == GameConstants.PLAYER_1 )
        {
            
            //Change health for player
            this.remotePlayer.adjustHealth( inHealthChange );
            
            //Change health for health bar
            this.bar2.setHealth( this.remotePlayer.getCurrentHealth() );
            
            //Redraw proper health bar
            bar2.repaint();

        }
        //Otherwise, change is for player 2 and player 2 is local player
        else
        {
            
            //Change health for player
            this.localPlayer.adjustHealth( inHealthChange );
            
            //Change health for health bar
            this.bar2.setHealth( this.localPlayer.getCurrentHealth() );
            
            //Redraw proper health bar
            bar2.repaint();

        }

    }

}