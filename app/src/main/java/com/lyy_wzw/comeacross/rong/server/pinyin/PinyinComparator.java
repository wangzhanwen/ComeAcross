package com.lyy_wzw.comeacross.rong.server.pinyin;


import com.lyy_wzw.comeacross.addressbook.Friend;

import java.util.Comparator;



/**
 *
 * @author
 *
 */
public class PinyinComparator implements Comparator<Friend> {


    public static PinyinComparator instance = null;

    public static PinyinComparator getInstance() {
        if (instance == null) {
            instance = new PinyinComparator();
        }
        return instance;
    }

    public int compare(Friend o1, Friend o2) {
        if (String.valueOf(o1.getNameSpelling().charAt(0)).equals("@")
                || String.valueOf(o2.getNameSpelling().charAt(0)).equals("#")) {
            return -1;
        } else if (String.valueOf(o1.getNameSpelling().charAt(0)).equals("#")
                   || String.valueOf(o2.getNameSpelling().charAt(0)).equals("@")) {
            return 1;
        } else {
            return String.valueOf(o1.getNameSpelling().charAt(0)).compareTo(String.valueOf(o2.getNameSpelling().charAt(0)));
        }
    }

}
