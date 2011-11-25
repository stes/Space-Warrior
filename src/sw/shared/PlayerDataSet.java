/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes Abbadonn
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
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerDataSet implements Comparable<PlayerDataSet>
{
	private static Random _random = new Random();
	
    // Attribute
    private String _name;
    private Point.Double _location;
    private int _lifepoints;
    private int _ammo;
    private double _direction;
    private double _speed;
    private int _points;
    private boolean _isLocal;

    // Bezugsobjekte
    
    // Konstruktor
    /**
     * Creates a new Player Dataset
     */
    public PlayerDataSet()
    {
        _lifepoints = Spielkonstanten.MAX_LEBEN;
        _ammo = Spielkonstanten.MAX_MUNITION;
        _points = 0;
        _location = new Point.Double(0, 0);
    }
    /**
     * Creates a new Player Dataset
     * 
     * @param name The player's name
     * @param position the initial location
     * @param lokal true, if player should be local
     */
    public PlayerDataSet(String name, Point position, boolean local) 
    {
        this(name, local);
        _location = new Point.Double(position.x, position.y);
    }
    /**
     * Creates a new Player Dataset
     * 
     * @param name The player's Name
     * @param local true, if player should be local
     */
    public PlayerDataSet(String name, boolean local)
    {
        this();
        _name = name;
        _isLocal = local;
    }
    /**
     * Creates a new Player Dataset out of an existing
     * 
     * @param dataset The source dataset which should be copied
     */
    public PlayerDataSet(PlayerDataSet dataset)
    {
        _name = dataset.name();
        _isLocal = dataset.lokal();
        _location = dataset.position();
        _direction = dataset.richtung();
        _points = dataset.punkte();
        
        if (_isLocal)
        {
            _lifepoints = dataset.leben();
            _ammo = dataset.munition();
            _speed = dataset.geschwindigkeit();
        }
    }
    
    @Override
    public int compareTo(PlayerDataSet spieler)
    {
        if(_points < spieler.punkte())
            return -1;
        if(_points > spieler.punkte())
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
        if (_isLocal)
            return _lifepoints;
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
        _lifepoints = wert;
    }
    /**
     * Gibt die aktuelle Munition zurueck
     * 
     * @return Die aktuelle Munition
     * @throws IllegalStateException Falls der Spieler nicht lokal ist
     */
    public int munition() throws IllegalStateException
    {
        if (_isLocal)
            return _ammo;
        else
            throw new IllegalStateException("Spieler ist nicht lokal");
    }
    /**
     * Weist der Munition einen neuen Wert zu
     * 
     * @param wert Der neue Wert f�r die Munition
     */
    protected void setzeMunition(int wert)
    {
        if (wert >= 0 && wert <= Spielkonstanten.MAX_MUNITION)
        {
            _ammo = wert;
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
        return _location;
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

        _location = new Point.Double(x, y);
    }
    /**
     * Weist eine neue Geschwindigkeit zu
     * 
     * @param geschwindigkeit Die neue Geschwindigkeit
     */
    protected void setzeGeschwindigkeit(double geschwindigkeit)
    {
        _speed = geschwindigkeit;
        if (_speed < 0)
            _speed = 0;
        else if (_speed > Spielkonstanten.MAX_GESCHWINDIGKEIT)
            _speed = Spielkonstanten.MAX_GESCHWINDIGKEIT;
    }
    /**
     * Gibt die aktuelle Geschwindigkeit zurueck
     * 
     * @return Die aktuelle Geschwindigkeit
     * @throws IllegalStateException Falls der Spieler nicht als lokal gekennzeichnet ist
     */
    public double geschwindigkeit() throws IllegalStateException
    {
        if (_isLocal)
            return _speed;
        else
            throw new IllegalStateException("Spieler ist nicht lokal");
    }
    /**
     * Gibt die aktuelle Richtung zurueck
     * 
     * @returns Die aktuelle Richtung im Gradma�
     */
    public double richtung()
    {
        return _direction;
    }
    /**
     * Weist eine neue Richtung zu
     * 
     * @param geschwindigkeit Die neue Richtung im Gradma�
     */
    public void setzeRichtung(int value)
    {
        _direction = value % 360;
    }
    /**
     * Weist eine neue Richtung zu
     * 
     * @param geschwindigkeit Die neue Richtung im Gradma�
     */
    public void setzeRichtung(double value)
    {
        _direction = value % 360;
    }
    /**
     * Gibt den Namen des Spielers zur�ck
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
        return _points;
    }
    /**
     * Weist der Punktzahl einen neuen Wert zu
     * 
     * @param wert Die neue Punktzahl
     */
    public void setzePunkte(int wert)
    {
        _points = wert;
    }
    /**
     * @return true, wenn der Spieler lokal ist
     */
    public boolean lokal()
    {
        return _isLocal;
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
        _location = new Point.Double(x, y);
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
     * @param winkel Der Drehwinkel im Gradma�
     */
    public void dreheUm(double winkel) 
    {
        setzeRichtung(richtung() + winkel);
    }
    /**
     * Verringert die Munition und gibt ein neues Schussobjekt zur�ck
     */
    public Shot schiesse() 
    {
        return this.schiesse(false);
    }
    /**
     * Verringert die Munition und gibt ein neues Schussobjekt zur�ck
     * 
     * @param master true, falls ein Masterschuss abgegeben werde soll
     */
    public Shot schiesse(boolean master)
    {
        int noetigeMunition = master ? Spielkonstanten.MUNITION_PRO_MSCHUSS : Spielkonstanten.MUNITION_PRO_SCHUSS;
        if (_ammo >= noetigeMunition)
        {
            _ammo -= noetigeMunition;
            
            double zeit = Spielkonstanten.SCHUSS_LEBENSDAUER / 2 /
                            ((double)Spielkonstanten.SPIELER_AKTUALISIERUNGS_INTERVALL);
            return new Shot(this.positionNach(zeit), (int)_direction, master);
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
        _ammo += (_ammo < Spielkonstanten.MAX_MUNITION-1 ? 1 : 0);
    }
    /**
     * Schreibt die Spielerdaten in ein Paket und gibt dieses zurueck
     * 
     * @param lokal true, wenn auch lokale Werte �bernommen werden sollen
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
     * Erstellt einen neuen Datensatz aus dem �bergebenen Paket
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
