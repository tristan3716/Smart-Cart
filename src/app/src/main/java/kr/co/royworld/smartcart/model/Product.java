package kr.co.royworld.smartcart.model;

public class Product {
    private String prdtCode;
    private String prdtName;
    private int sellPrice;
    private int dscntPrice;
    private String imgUrl;
    private Category cate;
    private Position pos;

    public Product(){}

    public Product(String prdtCode, String prdtName, int sellPrice, int dscntPrice, String imgUrl, Category cate, Position pos){
        this.prdtCode = prdtCode;
        this.prdtName = prdtName;
        this.sellPrice = sellPrice;
        this.dscntPrice = dscntPrice;
        this.imgUrl = imgUrl;
        this.cate = cate;
        this.pos = pos;
    }

    public String getPrdtCode() {
        return prdtCode;
    }

    public void setPrdtCode(String prdtCode) {
        this.prdtCode = prdtCode;
    }

    public String getPrdtName() {
        return prdtName;
    }

    public void setPrdtName(String prdtName) {
        this.prdtName = prdtName;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getDscntPrice() {
        return dscntPrice;
    }

    public void setDscntPrice(int dscntPrice) {
        this.dscntPrice = dscntPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Category getCate() {
        return cate;
    }

    public void setCate(Category cate) {
        this.cate = cate;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
