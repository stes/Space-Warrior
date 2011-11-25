package sw.client;

import java.util.ArrayList;
import java.awt.Graphics;

import sw.shared.Schuss;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class SchussPool
{
    // Bezugsobjekte
    private static ArrayList<SchussAnzeige> _schuesse;
    private static Spielfeld _spielfeld;
    
    // Attribute
 
    // Dienste    
    /**
     * Zeigt die Schüsse an
     */
    public static void paint(Graphics g)
    {
        if (_schuesse.size() > 0)
        {
            for (int i = 0; i < _schuesse.size(); i++)
            {
                if (_schuesse.get(i).istVeraltet())
                {
                    _schuesse.remove(i);
                    _spielfeld.repaint();
                }
                else
                {
                    _schuesse.get(i).paint(g);
                }
            }
        }
    }
    
    /**
     * Fügt einen neuen Schuss hinzu
     * 
     * @param schuss Der Schuss
     */
    public static void fuegeSchussHinzu(Schuss schuss)
    {
        _schuesse.add(new SchussAnzeige(schuss));
    }
    
    /**
     * Initialisiert den Schusspool
     * 
     * @param spielfeld Das Spielfeld, auf dem gezeichnet werden soll
     */
    public static void init(Spielfeld spielfeld)
    {
        _schuesse = new ArrayList<SchussAnzeige>(100);
        _spielfeld = spielfeld;
    }
}
