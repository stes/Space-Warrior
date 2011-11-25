package sw.shared;

/**
 * TODO: Dokumentation
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class SpielerEingabe
{
    // Bezugsobjekte

    // Attribute
    private int _bewegung;
    private int _drehung;
    private int _schuss;
    
    // Konstruktor
    /**
     * SpielerEingabe Instanz wird erzeugt
     */
    public SpielerEingabe()
    {
    }
    
    /**
     * SpielerEingabe Instanz wird erzeugt
     *
     * @param bewegung Bewegung
     * @param drehung Drehung
     * @param schuss Schuss
     */
    public SpielerEingabe(
        int bewegung,
        int drehung,
        int schuss)
    {
        this();
        _bewegung = bewegung;
        _drehung = drehung;
        _schuss = schuss;
    }
    
    /**
     * Erstellt eine Eingabe-Instanz aus einem Paket
     *
     * @param eingabe Eingabe-Paket
     */
    public SpielerEingabe(Paket eingabe)
    {
        this(eingabe.holeZahl(), eingabe.holeZahl(),
            eingabe.holeZahl());
    }

    /**
     * ERzeugt eine Eingabe-Instanz aus der übergebenen Eingabe
     * 
     * @param eingabe zu kopierende Eingabe-Instanz
     */
    public SpielerEingabe(SpielerEingabe eingabe)
    {
        this(eingabe.bewegung(), eingabe.drehung(), eingabe.schuss());
    }
    // Dienste
    
    /**
     * Schreibt die Eingabe in ein Paket und gibt dieses zurueck
     *
     * @return das Paket
     */
    public Paket pack()
    {
        Paket paket = new Paket(Nachrichtentyp.CL_EINGABE);
        paket.fuegeZahlAn(_bewegung);
        paket.fuegeZahlAn(_drehung);
        paket.fuegeZahlAn(_schuss);
        return paket;
    }
    
    /**
     * @return die Bewegung
     */
    public int bewegung()
    {
        return _bewegung;
    }
    
    /**
     * @return die Drehung
     */
    public int drehung()
    {
        return _drehung;
    }
    
    /**
     * @return der Schuss
     */
    public int schuss()
    {
        return _schuss;
    }
    
    /**
     * setzt die Bewegung
     *
     * @param wert die Bewegung
     */
    public void setzeBewegung(int wert)
    {
        if (Math.abs(wert) > 1)
            throw new IllegalArgumentException();
        _bewegung = wert;
    }
    
    /**
     * setzt die Drehung
     *
     * @param wert die Drehung
     */
    public void setzeDrehung(int wert)
    {
        if (Math.abs(wert) > 1)
            throw new IllegalArgumentException();
        _drehung = wert;
    }
    
    /**
     * setzt den Schuss
     *
     * @param wert der Schuss
     */
    public void setzeSchuss(int wert)
    {
        _schuss = wert;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof SpielerEingabe))
            return false;
        SpielerEingabe e = (SpielerEingabe) other;
        if(
            e.bewegung() == this.bewegung() &&
            e.drehung() == this.drehung() &&
            e.schuss() == this.schuss())
            return true;
        return false;
    }
}
