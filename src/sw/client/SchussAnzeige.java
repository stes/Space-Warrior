package sw.client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;

import sum.werkzeuge.Uhr;
import sum.ereignis.Buntstift;

import sw.shared.Schuss;
import sw.shared.Spielkonstanten;
import sw.shared.Punkt;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class SchussAnzeige extends Schuss
{
    private static Uhr _uhr = new Uhr();
    private static boolean _istBereit;
    
    // Bezugsobjekte
    private Color _basisFarbe;
    
    // Attribute
    private double _startZeit;
    
    // Konstruktor
    public SchussAnzeige(Schuss s)
    {
        super(s.startPunkt(), s.richtung(), s.istMaster());
        if (!_istBereit)
        {
            _uhr.starte();
            _istBereit = true;
        }
        _basisFarbe = Color.BLUE;
        _startZeit = _uhr.verstricheneZeit();
    }

    // Dienste
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(new Color(_basisFarbe.getRed(), _basisFarbe.getGreen(), _basisFarbe.getBlue(), (int)this.alpha(this.alter())));
        g2d.setStroke(new BasicStroke(3));
        g.drawLine((int)this.startPunkt().getX(), (int)this.startPunkt().getY(), (int)this.endPunkt().getX(), (int)this.endPunkt().getY());
    }
    
    public boolean istVeraltet()
    {
        return (this.alter() > Spielkonstanten.SCHUSS_LEBENSDAUER);
    }
    
    private double alter()
    {
        return _uhr.verstricheneZeit() - _startZeit;
    }
    
    private double alpha(double zeit)
    {
        double a = -1020 / (double)(Spielkonstanten.SCHUSS_LEBENSDAUER*Spielkonstanten.SCHUSS_LEBENSDAUER);
        double d = (double)Spielkonstanten.SCHUSS_LEBENSDAUER / 2;
        double f = 255;
        return a * (zeit - d) * (zeit - d) + f;
    }
}
