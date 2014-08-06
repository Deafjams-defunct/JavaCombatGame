import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;

/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Graphical object that is controlled by a user.
*/
public class PlayerControlledCharacter extends JComponent implements Solid
{

    
    private int maxHealth, currentHealth, numOfKeysPressed, player;
    
    //Queue of actions for this player to perform
    private MaxHeap queuedActions;
    
    //Attack currently occuring for this player.
    private MeleeAttack currentAttack;
    
    //Object to send notifcation to when this player dies.
    private DeathListener deathListener;

    /**
        Constructor
        @param int Maximum health
        @param int LOCAL_PLAYER or REMOTE_PLAYER
    */
    public PlayerControlledCharacter ( int inMaxHealth, int playerType )
    {

        //Setting current and max health to the given maximum health.
        this.maxHealth = this.currentHealth = inMaxHealth;

        //Player is unknown, setting player to invalid value.
        this.player = -1;

        //Creating new action queue.
        this.queuedActions = new MaxHeap();

        //Setting player to not currently have an attack.
        this.currentAttack = null;
        
        //If given player is the local player
        if ( playerType == GameConstants.LOCAL_PLAYER )
        {
            
            //Set player's character to the local player image.
            JLabel image = new JLabel( new ImageIcon( "localPlayer.png" ) );
            image.setBounds( 16, 16, 32, 32 );
            
            //Add player image to the component.
            this.add( image );
            
        }
        //If give player is the remote player
        else
        {
            
            //Set player's character to the remote player image.
            JLabel image = new JLabel( new ImageIcon( "remotePlayer.png" ) );
            image.setBounds( 16, 16, 32, 32 );
            
            //Add player image to component.
            this.add( image );
            
        }
        
    }
    
    /**
        Checks if an object with the given dimensions will collide with this one.
        @param int x
        @param int y
        @param int width
        @param int height
        @return boolean collision occured
    */
    public boolean willCollide ( int inX, int inY, int inWidth, int inHeight )
    {

        //Creates ellipse representing this pillar.
		Ellipse2D.Double thisPlayer = new Ellipse2D.Double( this.getX() + 16, this.getY() + 16, 32, 32 );

        //Returns if this pillar collides with the given object.
		return thisPlayer.intersects( inX, inY, inWidth, inHeight );

	}

    /**
        Sets this player's death listener
        @param DeathListener Death listener for this object
    */
    public void setDeathListener ( DeathListener inDeathListener )
    {

        //Sets this player's death listener to the given
		this.deathListener = inDeathListener;

	}

    /**
        Draws this character's attacks
        @param Graphics -
    */
    public void paintComponent ( Graphics g )
    {

        super.paintComponent( g );

        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setColor( Color.GRAY );

        //If this player currently has anattack
        if ( this.currentAttack != null )
        {

            //Draw this player's attack.
            g2d.fill( this.currentAttack.getArc() );
            
            //Tell the attack it has been displayed for one frame.
            this.currentAttack.displayedForOneFrame();
            
            //If the attack has been displayed for its maximum number of frames, remove this character's attack.
            if ( this.currentAttack.getFramesToBeDisplayed() == 0 ) { this.removeCurrentAttack(); }

        }

    }
    
    /**
        Get this player's current Action
        @return Action This player's current action
    */
    public Action getCurrentAction () { return this.queuedActions.peek(); }

    /**
        Adds an Action to this character's queue
        @param Action Action to add
    */
    public void addAction ( Action inAction )
    {

        //If this Action is a Move
        if ( inAction instanceof Move )
        {

            //If this character does not already have this movement in its queue
            if ( !this.queuedActions.contains( inAction ) )
            {

                Move inMove = ( Move ) inAction;

                //Set this move's priority
                inMove.setPriority( ++this.numOfKeysPressed );
                
                //Add this movement to this character's action queue
                this.queuedActions.add( inAction );

            }

        }
        //if this Action is a HealthChange
        else if ( inAction instanceof HealthChange )
        {

            //If the queue does not already contain this Action
            if ( !this.queuedActions.contains( inAction ) )
            {
                
                //Add Action to queue
                this.queuedActions.add( inAction );

            }

        }

    }

    /**
        Removes an Action from this character's queue
        @param Action Action to remove
    */
    public void removeAction ( Action inAction )
    {

        //If given Action is a Move and the queue contains that Action, decrease number of keys pressed
        if ( inAction instanceof Move && this.queuedActions.contains( inAction ) ) { this.numOfKeysPressed--; }

        //Remove Action from queue.
        this.queuedActions.remove( inAction );

    }

    /**
        Sets this player's current attack
        @param MeleeAttack Attack to place as current attack
    */
    public void setCurrentAttack ( MeleeAttack inAttack ) { this.currentAttack = inAttack; }
    
    /**
        Gets this player's current attack
        @return MeleeAttack This player's current attack
    */
    public MeleeAttack getCurrentAttack () { return this.currentAttack; }
    
    /**
        Removes this player's current attack
    */
    public void removeCurrentAttack() { this.currentAttack = null; }
    
    /**
        Sets the player number of this character
        @param int Player number
    */
    public void setPlayer( int inPlayer ) { this.player = inPlayer; }
    
    /**
        Gets the player number of this character
        @return int Player number
    */
    public int getPlayer () { return this.player; }

    /**
        Gets the current health of this player.
        @return int Current health
    */
    public int getCurrentHealth () { return this.currentHealth; }
    
    /**
        Gets the max health of this player.
        @return int Max health
    */
    public int getMaxHealth () { return this.maxHealth; }

    /**
        Changes the current health of this player by given amount
        @param int Health change
    */
    public void adjustHealth ( int inChange )
    {

        //If health increase would put current health over this player's maximum health, set the health to its maximum
        if ( this.currentHealth + inChange > this.maxHealth ) { this.currentHealth = this.maxHealth; }
        //If health decrease would put current health below 0
        else if ( this.currentHealth + inChange < 0 )
        {
            
            //Inform the death listener that this character has died
			this.deathListener.deathOccured( this.player );

		}
		//Otherwise, just adjust health by given amount
        else { this.currentHealth += inChange; }

    }

}