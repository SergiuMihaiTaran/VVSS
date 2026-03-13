package drinkshop.domain;

public class Product {

    private int id;
    private String nume;
    private double pret;
    private Categorie categorie;
    private Tip tip;

    public Product(int id, String nume, double pret,
                  Categorie categorie,
                  Tip tip) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.categorie = categorie;
        this.tip = tip;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public double getPret() { return pret; }
    public Categorie getCategorie() { return categorie; }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Tip getTip() { return tip; }

    public void setTip(Tip tip) {
        this.tip = tip;
    }
    public void setNume(String nume) { this.nume = nume; }
    public void setPret(double pret) { this.pret = pret; }

    @Override
    public String toString() {
        return nume + " (" + categorie + ", " + tip + ") - " + pret + " lei";
    }
}