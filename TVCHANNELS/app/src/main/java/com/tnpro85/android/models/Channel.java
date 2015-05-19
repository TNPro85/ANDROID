package com.tnpro85.android.models;

import java.util.Comparator;

/**
 * Created by TUAN on 20/05/2015.
 */
public abstract class Channel implements Comparable<Channel> {

    protected int cNum;
    protected String cName;
    protected int cLogo;

    public Channel() {
        cName = "";
    }
    public Channel(int num, String name) {
        super();
        this.cNum = num;
        this.cName = name;
    }

    @Override
    public int compareTo(Channel another) {
        return Integer.compare(this.cNum, another.cNum);
    }
}
