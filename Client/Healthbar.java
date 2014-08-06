import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

/**
@author: Dylan Foster
@version: 12/14/12
@desciption: Handles graphical health representation
*/

public class Healthbar extends JPanel
{

    //Graphical health bar
    private Rectangle health;
    
    //Text display for player (Ex: Player 1)
    private JLabel playerText;
    
    //Text of current health vs max health
    private JLabel healthText;
    
    //Quantity of current health and maximum health.
    private int currentHealth, maxHealth;

    /**
        Constructor
        @param int Maximum health for this bar.
        @param String Player's text
    */
    public Healthbar ( int inMaxHealth, String inPlayerText )
    {

        this.maxHealth = inMaxHealth;
        this.currentHealth = inMaxHealth;

        //Creates health bar of proper size
        this.health = new Rectangle( 0, 24, 250, 12 );

        //Sets player's text and player's text size
        this.playerText = new JLabel( inPlayerText );
        this.playerText.setBounds( 0, 0, 100, 8 );

        //Sets health size and its size
        this.healthText = new JLabel( inMaxHealth + "/" + inMaxHealth );
        this.healthText.setBounds( 0, 0, 100, 8 );

        //Adds player's text and health to the panel.
        this.add( this.playerText );
        this.add( this.healthText );

    }
    
    /**
        Draws components of this health bar
        @param Graphics -
    */
    public void paintComponent ( Graphics g )
    {
        
        //Updates labels
        super.paintComponent( g );
        
        Graphics2D g2d = ( Graphics2D ) g;
        
        g2d.setColor( Color.RED );
        
        //Draws graphical health bar
        g2d.fill( this.health );
        
    }

    /**
        Updates numeric and graphical health elements of this bar.
        @param int Amount of health to set to
    */
    public void setHealth ( int inHealth )
    {

        //Sets current amount of health to given amount
        this.currentHealth = inHealth;
        
        //Updates text representation of health
        this.healthText.setText( inHealth + "/" + this.maxHealth );
        
        //Resizes graphical health bar
        this.health.setSize( ( int ) ( ( currentHealth / ( double ) maxHealth ) * 250 ), 12 );
        
        //Redraws this health bar.
        this.repaint();

    }
    
    /**
        Returns current health contained in this health bar.
        @return int Current amount of health
    */
    public int gethealth () { return this.currentHealth; }

}