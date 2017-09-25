package ro.artsoftconsult.myapplication.shoppinglist;

public class Product {
    private String name;
private double price;


    //Constructor

    public Product( String name, double price ) {
        this.price = price;
        this.name = name;

    }

    //Setter, getter


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
