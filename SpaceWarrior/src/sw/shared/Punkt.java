package sw.shared;

import java.awt.Point;
/**
 * Stellt ein Paar aus zwei ganzzahligen Werten dar
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Punkt extends Point
{
    // Bezugsobjekte

    // Attribute

    // Konstruktor
    /**
     * Erzeugt einen neuen Punkt
     */
    public Punkt()
    {
        super();
    }
    /**
     * Erzeugt einen neuen Punkt
     * 
     * @param x Die x-Koordinate
     * @param y Die y-Koordinate
     */
    public Punkt(int x, int y)
    {
        super(x, y);
    }
    /**
     * Erzeugt einen neuen Punkt
     * 
     * @param x Die x-Koordinate
     * @param y Die y-Koordinate
     */
    public Punkt(double x, double y)
    {
        this((int)x, (int)y);
    }
    /**
     * Erzeugt einen neuen Punkt
     * 
     * @param p Ein Punkt, dessen Koordinaten uebernommen werden
     */
    public Punkt(Point p)
    {
        super(p);
    }
}
