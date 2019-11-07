package kr.co.royworld.smartcart.model;

public class BeaconData {
    private String beaconName;
    private String uuid;
    private int major;
    private int minor;
    private Category cate;

    public BeaconData(){}

    public BeaconData(String beaconName, String uuid, int major, int minor, Category cate){
        this.beaconName = beaconName;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.cate = cate;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public Category getCate() {
        return cate;
    }

    public void setCate(Category cate) {
        this.cate = cate;
    }
}
