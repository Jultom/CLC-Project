package ro.artsoftconsult.myapplication.shoppinglist;

public class Product {
    private String name;
private int price;


    //Constructor

    public Product( String name, int price ) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
