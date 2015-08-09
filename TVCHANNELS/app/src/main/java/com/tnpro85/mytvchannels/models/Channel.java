package com.tnpro85.mytvchannels.models;

/**
 * Created by TUAN on 20/05/2015.
 */
public abstract class Channel implements Comparable<Channel> {

    public int cNum;
    public String cName;
    public String cDesc;
    public int cLogo;

    public Channel() {
        cName = "";
        cDesc = "";
    }
    public Channel(int num, String name) {
        super();
        this.cNum = num;
        this.cName = name;
        cDesc = "";
    }

    @Override
    public int compareTo(Channel another) {
        return ((Integer)this.cNum).compareTo(another.cNum);
    }
}
