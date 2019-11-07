package kr.co.royworld.smartcart.model;

public class Category {
    private String ctgrId;
    private String ctgrName;
    private Position pos;

    public Category(){}

    public Category(String ctgrId, String ctgrName, Position pos){
        this.ctgrId = ctgrId;
        this.ctgrName = ctgrName;
        this.pos = pos;
    }

    public String getCtgrId() {
        return ctgrId;
    }

    public void setCtgrId(String ctgrId) {
        this.ctgrId = ctgrId;
    }

    public String getCtgrName() {
        return ctgrName;
    }

    public void setCtgrName(String ctgrName) {
        this.ctgrName = ctgrName;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
