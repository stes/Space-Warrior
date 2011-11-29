package sw.client.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JTextArea;

class TransparentTextArea extends JTextArea
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7617582110497995855L;


	public TransparentTextArea ()
    {
        this.setOpaque(false);
    }
 
 
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        this.setBackground(Color.white);
        Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f);
        g2d.setComposite(alphaComp);
        g2d.setColor(getBackground());
        Rectangle tBounds = g2d.getClip().getBounds();
        g2d.fillRect((int) tBounds.getX(),(int)tBounds.getY(),(int)tBounds.getWidth(),(int)tBounds.getHeight());
        super.paintComponent(g2d);
        this.setForeground(Color.black);
    }   
    
}
