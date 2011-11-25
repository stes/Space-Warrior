package sw.shared;

/**
 * Datenstruktur zur Repräsentation eines Schusses
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Schuss extends java.awt.geom.Line2D.Double
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
    public Schuss(Punkt startPunkt, int richtung)
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
    public Schuss(Punkt startPunkt, int richtung, boolean master)
    {
        super(startPunkt, new Punkt(0,0));
        _istMaster = master;
        setzeRichtung(richtung);
    }

    // Dienste
    /**
     * @return der Startpunkt
     */
    public Punkt startPunkt()
    {
        return new Punkt(this.getX1(), this.getY1());
    }
    /**
     * @return der Endpunkt
     */
    public Punkt endPunkt()
    {
        return new Punkt(this.getX2(), this.getY2());
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
        this.setLine(startPunkt(), new Punkt(
            startPunkt().getX() + reichweite * Math.sin(Math.toRadians(richtung)),
            startPunkt().getY() + reichweite * Math.cos(Math.toRadians(richtung))));
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
    public double abstandZu(Punkt p)
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
        Paket p = new Paket(Nachrichtentyp.SV_SCHUSS);
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
    public static Schuss hole(Paket p)
    {
        if (p.Typ() != Nachrichtentyp.SV_SCHUSS)
            throw new IllegalArgumentException();
        return new Schuss(
            new Punkt(p.holeZahl(), p.holeZahl()),
            p.holeZahl(),
            p.holeBoolean());
    }
}
