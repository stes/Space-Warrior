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
import java.awt.geom.Point2D;
import java.util.Random;

import sw.shared.Spielkonstanten;

/**
 * Klasse zur Verwaltung von Spielerdaten
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class PlayerDataSet implements Comparable<PlayerDataSet>
{
	private static Random _random = new Random();
	
    // Attribute
    private String _name;
    private Point.Double _position;
    private int _leben;
    private int _munition;
    private double _richtung;
    private double _geschwindigkeit;
    private int _punkte;
    private boolean _istLokal;

    // Bezugsobjekte
    
    // Konstruktor
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     */
    public PlayerDataSet()
    {
        _leben = Spielkonstanten.MAX_LEBEN;
        _munition = Spielkonstanten.MAX_MUNITION;
        _punkte = 0;
        _position = new Point.Double(0, 0);
    }
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     * 
     * @param name Der Name
     * @param position Die Position
     * @param lokal true, wenn es sich um den lokalen Spieler handelt
     */
    public PlayerDataSet(String name, Point position, boolean lokal) 
    {
        this(name, lokal);
        _position = new Point.Double(position.x, position.y);
    }
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     * 
     * @param name Der Name
     * @param lokal true, wenn es sich um den lokalen Spieler handelt
     */
    public PlayerDataSet(String name, boolean lokal)
    {
        this();
        _name = name;
        _istLokal = lokal;
    }
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     * 
     * @param daten Der Datensatz, der kopiert werden soll
     */
    public PlayerDataSet(PlayerDataSet daten)
    {
        _name = daten.name();
        _istLokal = daten.lokal();
        _position = daten.position();
        _richtung = daten.richtung();
        _punkte = daten.punkte();
        
        if (_istLokal)
        {
            _leben = daten.leben();
            _munition = daten.munition();
            _geschwindigkeit = daten.geschwindigkeit();
        }
    }
    
    @Override
    public int compareTo(PlayerDataSet spieler)
    {
        if(_punkte < spieler.punkte())
            return -1;
        if(_punkte > spieler.punkte())
            return 1;
        return 0;
    }
   
    // Dienste
    /**
     * Gibt die aktuelle Gesundheit zurueck
     * 
     * @return Die aktuelle Gesundheit
     * @throws IllegalStateException Falls der Spieler nicht lokal ist
     */
    public int leben() throws IllegalStateException
    {
        if (_istLokal)
            return _leben;
        else
            throw new IllegalStateException("Spieler ist nicht lokal");
    }
    /**
     * Weist der Gesundheit einen neuen Wert zu
     * 
     * @param wert Die neue Gesundheit
     */
    public void setzeLeben(int wert)
    {
        _leben = wert;
    }
    /**
     * Gibt die aktuelle Munition zurueck
     * 
     * @return Die aktuelle Munition
     * @throws IllegalStateException Falls der Spieler nicht lokal ist
     */
    public int munition() throws IllegalStateException
    {
        if (_istLokal)
            return _munition;
        else
            throw new IllegalStateException("Spieler ist nicht lokal");
    }
    /**
     * Weist der Munition einen neuen Wert zu
     * 
     * @param wert Der neue Wert für die Munition
     */
    protected void setzeMunition(int wert)
    {
        if (wert >= 0 && wert <= Spielkonstanten.MAX_MUNITION)
        {
            _munition = wert;
        }
        else
        {
            throw new IllegalArgumentException("Munition zwischen 0 und " + Spielkonstanten.MAX_MUNITION + " waehlen");
        }
    }
    /**
     * @return Die horizontale Position des Spielers
     */
    public int xPosition()
    {
        return (int)position().getX();
    }
    /**
     * @return Die vertikale Position des Spielers
     */
    public int yPosition()
    {
        return (int)position().getY();
    }
    /**
     * @return Die Position des Spielers
     */
    public Point.Double position()
    {
        return _position;
    }
    /**
     * Weisst der Position einen neuen Wert zu
     * 
     * @param wert Die neue Position
     */
    protected void setzePosition(Point wert)
    {
        double x = wert.getX();
        double y = wert.getY();

        if (x + Spielkonstanten.SPIELERGROESSE/2 > Spielkonstanten.SPIELFELD_BREITE)
            x = Spielkonstanten.SPIELFELD_BREITE - Spielkonstanten.SPIELERGROESSE/2;
        else if (x - Spielkonstanten.SPIELERGROESSE/2 < 0)
            x = Spielkonstanten.SPIELERGROESSE/2;
        if (y + Spielkonstanten.SPIELERGROESSE/2 > Spielkonstanten.SPIELFELD_HOEHE)
            y = Spielkonstanten.SPIELFELD_HOEHE - Spielkonstanten.SPIELERGROESSE/2;
        else if (y - Spielkonstanten.SPIELERGROESSE/2 < 0)
            y = Spielkonstanten.SPIELERGROESSE/2;

        _position = new Point.Double(x, y);
    }
    /**
     * Weist eine neue Geschwindigkeit zu
     * 
     * @param geschwindigkeit Die neue Geschwindigkeit
     */
    protected void setzeGeschwindigkeit(double geschwindigkeit)
    {
        _geschwindigkeit = geschwindigkeit;
        if (_geschwindigkeit < 0)
            _geschwindigkeit = 0;
        else if (_geschwindigkeit > Spielkonstanten.MAX_GESCHWINDIGKEIT)
            _geschwindigkeit = Spielkonstanten.MAX_GESCHWINDIGKEIT;
    }
    /**
     * Gibt die aktuelle Geschwindigkeit zurueck
     * 
     * @return Die aktuelle Geschwindigkeit
     * @throws IllegalStateException Falls der Spieler nicht als lokal gekennzeichnet ist
     */
    public double geschwindigkeit() throws IllegalStateException
    {
        if (_istLokal)
            return _geschwindigkeit;
        else
            throw new IllegalStateException("Spieler ist nicht lokal");
    }
    /**
     * Gibt die aktuelle Richtung zurueck
     * 
     * @returns Die aktuelle Richtung im Gradmaß
     */
    public double richtung()
    {
        return _richtung;
    }
    /**
     * Weist eine neue Richtung zu
     * 
     * @param geschwindigkeit Die neue Richtung im Gradmaß
     */
    public void setzeRichtung(int value)
    {
        _richtung = value % 360;
    }
    /**
     * Weist eine neue Richtung zu
     * 
     * @param geschwindigkeit Die neue Richtung im Gradmaß
     */
    public void setzeRichtung(double value)
    {
        _richtung = value % 360;
    }
    /**
     * Gibt den Namen des Spielers zurück
     * 
     * @return Der Name
     */
    public String name()
    {
        return _name;
    }
    /**
     * Gibt die aktuelle Punktzahl zurueck
     * 
     * @return Die aktuelle Punktzahl
     */
    public int punkte()
    {
        return _punkte;
    }
    /**
     * Weist der Punktzahl einen neuen Wert zu
     * 
     * @param wert Die neue Punktzahl
     */
    public void setzePunkte(int wert)
    {
        _punkte = wert;
    }
    /**
     * @return true, wenn der Spieler lokal ist
     */
    public boolean lokal()
    {
        return _istLokal;
    }
    //**********************************************************************************************
    /**
     * Initialisiert die Spielerdaten mit Standartwerten
     */
    public void init() 
    {
        int rand = Spielkonstanten.SPIELERGROESSE/2+1;
        int x = rand + _random.nextInt(Spielkonstanten.SPIELFELD_BREITE - rand);
        int y = rand + _random.nextInt(Spielkonstanten.SPIELFELD_HOEHE - rand);
        _position = new Point.Double(x, y);
        setzeGeschwindigkeit(0);
        setzeRichtung(rand + _random.nextInt(360));
        setzeLeben(Spielkonstanten.MAX_LEBEN);
        setzeMunition(Spielkonstanten.MAX_MUNITION);
    }
    /**
     * Erhoeht die Geschwindigkeit um den festgelegten Wert
     */
    public void beschleunige(double betrag) 
    {
        setzeGeschwindigkeit(geschwindigkeit() + betrag);
    }
    /**
     * Bewegt die Spielfigur um die akutelle Geschwindkeit
     */
    public void bewege() 
    {
        double b = Math.toRadians(richtung());
        int x = (int)(geschwindigkeit() * Math.sin(b));
        int y = (int)(geschwindigkeit() * Math.cos(b));
        this.setzePosition(new Point(this.xPosition() + x, this.yPosition() + y));
    }
    /**
     * Dreht den Spieler um den angegebenen Winkel gegen den Uhrzeigersinn
     * 
     * @param winkel Der Drehwinkel im Gradmaß
     */
    public void dreheUm(double winkel) 
    {
        setzeRichtung(richtung() + winkel);
    }
    /**
     * Verringert die Munition und gibt ein neues Schussobjekt zurück
     */
    public Shot schiesse() 
    {
        return this.schiesse(false);
    }
    /**
     * Verringert die Munition und gibt ein neues Schussobjekt zurück
     * 
     * @param master true, falls ein Masterschuss abgegeben werde soll
     */
    public Shot schiesse(boolean master)
    {
        int noetigeMunition = master ? Spielkonstanten.MUNITION_PRO_MSCHUSS : Spielkonstanten.MUNITION_PRO_SCHUSS;
        if (_munition >= noetigeMunition)
        {
            _munition -= noetigeMunition;
            
            double zeit = Spielkonstanten.SCHUSS_LEBENSDAUER / 2 /
                            ((double)Spielkonstanten.SPIELER_AKTUALISIERUNGS_INTERVALL);
            return new Shot(this.positionNach(zeit), (int)_richtung, master);
        }
        return null;
    }
    private Point positionNach(double zeitintervall)
    {
        double weg = zeitintervall * this.geschwindigkeit();
        return new Point(
                (int)(this.position().getX() + weg * Math.sin(Math.toRadians(this.richtung()))),
                (int)(this.position().getY() + weg * Math.cos(Math.toRadians(this.richtung()))));
    }
    /**
     * Erhoeht die Munition
     */
    public void ladeNach() 
    {
        _munition += (_munition < Spielkonstanten.MAX_MUNITION-1 ? 1 : 0);
    }
    /**
     * Schreibt die Spielerdaten in ein Paket und gibt dieses zurueck
     * 
     * @param lokal true, wenn auch lokale Werte übernommen werden sollen
     * @return Das Paket
     */
    public Paket pack(boolean lokal)
    {
        Paket p = new Paket(Pakettype.SNAP_SPIELERDATEN);
        p.fuegeStringAn(this.name());
        p.fuegeBooleanAn(lokal);
        p.fuegeZahlAn((int)this.position().getX());
        p.fuegeZahlAn((int)this.position().getY());
        p.fuegeZahlAn((int)this.richtung());
        p.fuegeZahlAn(this.punkte());
        
        int x = lokal ? 1 : 0;
        
        p.fuegeZahlAn(this.leben() * x);
        p.fuegeZahlAn(this.munition() * x);
        //p.fuegeZahlAn(this.geschwindigkeit() * x);
        
        return p;
    }
    /**
     * Erstellt einen neuen Datensatz aus dem übergebenen Paket
     * 
     * @param p Das Paket
     * @return Eine neue SpielerDaten-Instanz
     * @throws IllegalArgumentException wenn Pakettyp falsch ist
     */
    public static PlayerDataSet hole(Paket p)
    {
        if (p.Typ() != Pakettype.SNAP_SPIELERDATEN)
            throw new IllegalArgumentException();
        String name = p.holeString();
        boolean lokal = p.holeBoolean();
        
        PlayerDataSet daten = new PlayerDataSet(name, lokal);
        
        daten.setzePosition(new Point(p.holeZahl(), p.holeZahl()));
        daten.setzeRichtung(p.holeZahl());
        daten.setzePunkte(p.holeZahl());
        
        daten.setzeLeben(p.holeZahl());
        daten.setzeMunition(p.holeZahl());
        //daten.setzeGeschwindigkeit(p.holeZahl());

        return daten;
    }
}
