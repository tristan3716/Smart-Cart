package kr.co.royworld.smartcart.model;

public class Cart {
    private Product product;
    private int qty;
    private boolean cartYn;

    public Cart(){}

    public Cart(Product product, int qty, boolean cartYn){
        this.product = product;
        this.qty = qty;
        this.cartYn = cartYn;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isCartYn() {
        return cartYn;
    }

    public void setCartYn(boolean cartYn) {
        this.cartYn = cartYn;
    }
}
