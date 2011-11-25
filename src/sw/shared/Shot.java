/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.shared;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

/**
 * Datenstruktur zur Repräsentation eines Schusses
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Shot extends java.awt.geom.Line2D.Double
{
    // Bezugsobjekte

    // Attribute
    private boolean _istMaster;
    private int _richtung;
    
    // Konstruktor

    /**
     * Erstellt einen neuen Schuss
     *
     * @param startPunkt Der Startpunkt des Schusses
     * @param richtung Die Richtung des Schusses
     */
    public Shot(Point startPunkt, int richtung)
    {
        this(startPunkt, richtung, false);
    }
    
    /**
     * Erstellt einen neuen Schuss
     *
     * @param startPunkt Der Startpunkt des Schusses
     * @param richtung Die Richtung des Schusses
     * @param master true, wenn ein Masterschuss vorliegt
     */
    public Shot(Point startPunkt, int richtung, boolean master)
    {
        super(startPunkt, new Point(0,0));
        _istMaster = master;
        setzeRichtung(richtung);
    }

    // Dienste
    /**
     * @return der Startpunkt
     */
    public Point startPunkt()
    {
        return new Point((int)this.getX1(), (int)this.getY1());
    }
    /**
     * @return der Endpunkt
     */
    public Point endPunkt()
    {
        return new Point((int)this.getX2(), (int)this.getY2());
    }
    
    /**
     * Weist dem Schuss eine neue Richtung zu
     * 
     * @param richtung Die neue Richtung im Gradmaß
     */
    public void setzeRichtung(int richtung)
    {
        _richtung = richtung;
        double reichweite = _istMaster ? Spielkonstanten.MAX_MASTER_REICHWEITE : Spielkonstanten.MAX_REICHWEITE;
        this.setLine(startPunkt(), new Point(
            (int)(startPunkt().getX() + reichweite * Math.sin(Math.toRadians(richtung))),
            (int)(startPunkt().getY() + reichweite * Math.cos(Math.toRadians(richtung)))));
    }
    
    /**
     * @return Die Richtung im Gradmaß
     */
    public int richtung()
    {
        return _richtung;
    }
    
    /**
     * @return true, wenn es sich um einen Masterschuss handelt
     */
    public boolean istMaster()
    {
        return _istMaster;
    }
    
    /**
     * Berechnet den Anstand zum angegebenen Punkt
     * 
     * @param p Der Punkt
     * @return Der Abstand
     */
    public double abstandZu(Point p)
    {
        return this.ptLineDist(p.getX(), p.getY());
    }
    
	public double abstandZu(Point.Double p)
	{
		return this.ptLineDist(p.getX(), p.getY());
	}
    
    /**
     * @return Der Schaden, den der Schuss zufügt
     */
    public int schaden()
    {
        return this.istMaster() ? Spielkonstanten.MAX_MASTER_SCHADEN : Spielkonstanten.MAX_SCHADEN;
    }
    
    /**
     * Schreibt den Schuss in ein Paket und gibt dieses zurueck
     * 
     * @return Das Paket
     */
    public Paket pack()
    {
        Paket p = new Paket(Pakettype.SV_SCHUSS);
        p.fuegeZahlAn((int)startPunkt().getX());
        p.fuegeZahlAn((int)startPunkt().getY());
        p.fuegeZahlAn(this.richtung());
        p.fuegeBooleanAn(this.istMaster());
        return p;
    }
    
    /**
     * Erstellt einen neuen Datensatz aus dem übergebenen Paket
     * 
     * @param p Das Paket
     * @return Eine neue Schuss-Instanz
     * @throws IllegalArgumentException wenn Pakettyp falsch ist
     */
    public static Shot hole(Paket p)
    {
        if (p.Typ() != Pakettype.SV_SCHUSS)
            throw new IllegalArgumentException();
        return new Shot(
            new Point(p.holeZahl(), p.holeZahl()),
            p.holeZahl(),
            p.holeBoolean());
    }
}
