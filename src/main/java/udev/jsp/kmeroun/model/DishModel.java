package udev.jsp.kmeroun.model;

public class DishModel {

    private int identifiant;
    private String nom;
    private double prix;

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant = identifiant;
    }

    public double getPrix() {
        return prix;
    }

    public String getNom() {
        return nom;
    }

    public int getIdentifiant() {
        return identifiant;
    }
}
