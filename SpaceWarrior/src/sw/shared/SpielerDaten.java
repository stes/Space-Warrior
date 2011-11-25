package sw.shared;

import sum.werkzeuge.Rechner;

import sw.shared.Spielkonstanten;

/**
 * Klasse zur Verwaltung von Spielerdaten
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class SpielerDaten implements Comparable<SpielerDaten>
{
    // Attribute
    private String _name;
    private Punkt _position;
    private int _leben;
    private int _munition;
    private double _richtung;
    private double _geschwindigkeit;
    private int _punkte;
    private boolean _istLokal;

    // Bezugsobjekte
    private Rechner _rechner;
    
    // Konstruktor
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     */
    public SpielerDaten()
    {
        _leben = Spielkonstanten.MAX_LEBEN;
        _munition = Spielkonstanten.MAX_MUNITION;
        _punkte = 0;
        _position = new Punkt(0, 0);
    }
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     * 
     * @param name Der Name
     * @param position Die Position
     * @param lokal true, wenn es sich um den lokalen Spieler handelt
     */
    public SpielerDaten(String name, Punkt position, boolean lokal) 
    {
        this(name, lokal);
        _position = position;
    }
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     * 
     * @param name Der Name
     * @param lokal true, wenn es sich um den lokalen Spieler handelt
     */
    public SpielerDaten(String name, boolean lokal)
    {
        this();
        _rechner = new Rechner();
        _name = name;
        _istLokal = lokal;
    }
    /**
     * Erzeugt eine Instanz der Klasse SpielerDaten
     * 
     * @param daten Der Datensatz, der kopiert werden soll
     */
    public SpielerDaten(SpielerDaten daten)
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
    public int compareTo(SpielerDaten spieler)
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
    public Punkt position()
    {
        return _position;
    }
    /**
     * Weisst der Position einen neuen Wert zu
     * 
     * @param wert Die neue Position
     */
    protected void setzePosition(Punkt wert)
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

        _position = new Punkt(x, y);
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
        int x = _rechner.ganzeZufallsZahl(rand, Spielkonstanten.SPIELFELD_BREITE - rand);
        int y = _rechner.ganzeZufallsZahl(rand, Spielkonstanten.SPIELFELD_HOEHE - rand);
        _position = new Punkt(x, y);
        setzeGeschwindigkeit(0);
        setzeRichtung(_rechner.ganzeZufallsZahl(rand, 360));
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
        this.setzePosition(new Punkt(this.xPosition() + x, this.yPosition() + y));
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
    public Schuss schiesse() 
    {
        return this.schiesse(false);
    }
    /**
     * Verringert die Munition und gibt ein neues Schussobjekt zurück
     * 
     * @param master true, falls ein Masterschuss abgegeben werde soll
     */
    public Schuss schiesse(boolean master)
    {
        int noetigeMunition = master ? Spielkonstanten.MUNITION_PRO_MSCHUSS : Spielkonstanten.MUNITION_PRO_SCHUSS;
        if (_munition >= noetigeMunition)
        {
            _munition -= noetigeMunition;
            
            double zeit = Spielkonstanten.SCHUSS_LEBENSDAUER / 2 /
                            ((double)Spielkonstanten.SPIELER_AKTUALISIERUNGS_INTERVALL);
            return new Schuss(this.positionNach(zeit), (int)_richtung, master);
        }
        return null;
    }
    private Punkt positionNach(double zeitintervall)
    {
        double weg = zeitintervall * this.geschwindigkeit();
        return new Punkt(
                this.position().getX() + weg * Math.sin(Math.toRadians(this.richtung())),
                this.position().getY() + weg * Math.cos(Math.toRadians(this.richtung())));
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
        Paket p = new Paket(Nachrichtentyp.SNAP_SPIELERDATEN);
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
    public static SpielerDaten hole(Paket p)
    {
        if (p.Typ() != Nachrichtentyp.SNAP_SPIELERDATEN)
            throw new IllegalArgumentException();
        String name = p.holeString();
        boolean lokal = p.holeBoolean();
        
        SpielerDaten daten = new SpielerDaten(name, lokal);
        
        daten.setzePosition(new Punkt(p.holeZahl(), p.holeZahl()));
        daten.setzeRichtung(p.holeZahl());
        daten.setzePunkte(p.holeZahl());
        
        daten.setzeLeben(p.holeZahl());
        daten.setzeMunition(p.holeZahl());
        //daten.setzeGeschwindigkeit(p.holeZahl());

        return daten;
    }
}
