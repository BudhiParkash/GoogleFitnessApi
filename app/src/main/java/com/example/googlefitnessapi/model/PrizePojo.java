package com.example.googlefitnessapi.model;

public class PrizePojo {
    String PrizeImage,PrizeTitle,PrizeDiscription;
    float DocCoin;
    int SharePoint;

    public PrizePojo() {
    }

    public PrizePojo(String prizeImage, String prizeTitle, String prizeDiscription, int sharePoint) {
        PrizeImage = prizeImage;
        PrizeTitle = prizeTitle;
        PrizeDiscription = prizeDiscription;
        SharePoint = sharePoint;
    }

    public PrizePojo(String prizeImage, String prizeTitle, String prizeDiscription, float docCoin) {
        PrizeImage = prizeImage;
        PrizeTitle = prizeTitle;
        PrizeDiscription = prizeDiscription;
        DocCoin = docCoin;
    }

    public String getPrizeImage() {
        return PrizeImage;
    }

    public void setPrizeImage(String prizeImage) {
        PrizeImage = prizeImage;
    }

    public String getPrizeTitle() {
        return PrizeTitle;
    }

    public void setPrizeTitle(String prizeTitle) {
        PrizeTitle = prizeTitle;
    }

    public String getPrizeDiscription() {
        return PrizeDiscription;
    }

    public void setPrizeDiscription(String prizeDiscription) {
        PrizeDiscription = prizeDiscription;
    }

    public float getDocCoin() {
        return DocCoin;
    }

    public void setDocCoin(float docCoin) {
        DocCoin = docCoin;
    }

    public int getSharePoint() {
        return SharePoint;
    }

    public void setSharePoint(int sharePoint) {
        SharePoint = sharePoint;
    }
}
