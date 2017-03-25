package com.blogspot.huyhungdinh.v_coin;

/**
 * Created by HUNGDH on 1/6/2016.
 */
public class MyApps {
    private String name;
    private String link;
    private int image;
    private int coin;
    private boolean installed;
    private String question;
    private String answer;
    private boolean finished;

    public MyApps() {

    }

    public MyApps(String name, String link, int image, int coin, boolean installed, String question, String answer, boolean finished) {
        this.name = name;
        this.link = link;
        this.image = image;
        this.coin = coin;
        this.installed = installed;
        this.question = question;
        this.answer = answer;
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
