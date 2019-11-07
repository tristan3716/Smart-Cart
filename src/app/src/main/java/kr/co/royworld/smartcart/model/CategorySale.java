package kr.co.royworld.smartcart.model;

public class CategorySale {
    private Category cate;
    private int qty;
    private int amt;

    public CategorySale(){}

    public CategorySale(Category cate, int qty, int amt){
        this.cate = cate;
        this.qty = qty;
        this.amt = amt;
    }

    public Category getCate() {
        return cate;
    }

    public void setCate(Category cate) {
        this.cate = cate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }
}
