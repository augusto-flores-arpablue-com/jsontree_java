/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.jsontree;

import com.arpablue.jsontree.JSONtreePrimitive;
import com.arpablue.jsontree.TreeLeaf;
import com.arpablue.jsontree.JSONtreeBase;

/**
 *
 * @author Augusto Flores
 */
public class JSONtreePrinter extends JSONtreePrimitive {

    public JSONtreePrinter() {
        super();
    }
    /**
     * This return an string with tabs character, the number of tabs characters is 
     * related to the deep of the level.
     * @return It is the String with the tabs character corresponding to the level.
     */
    private static String tabs() {
        String res = "";
        for (int i = 0; i < DEEPER; i++) {
            res = res + "   ";
        }

        return res;
    }

    private static void msg(String text) {
        System.out.println(text);
    }
    /**
     * This function return the STRING format for element according its type.
     * @param target Its return a object that represent the object that content 
     */
    protected String objectToString(Object target) {
        if (target == null) {
            return "null";
        }
        String key;
        key = null;
        Object value;
        value = null;
        boolean flag;
        String res;
        res = null;
        if (!JSONtreeBase.isThPath(target)) {
            key = ((TreeLeaf) target).getKey();
            value = ((TreeLeaf) target).get();
            if ((key != null) && (res != null)) {
                res = "\"" + key + "\":" + res;
            }

        }
        if (res == null) {
            res = ((JSONtreeBase) target).toString();
        }
        return res;
    }

    /**
     * It return the tabulation characters n times in an string.
     *
     * @param n it is the number of times that tabulation characters will
     * repeated.
     * @return It is the string with the tabulation characters.
     */
    protected String getTab(int n) {
        String res = "";
        for (int i = 0; i < n; i++) {
            res = res + "    ";
        }
        return res;
    }
    /**
     * It return the structure of the Tree path in
     *
     * @return
     */
    public String getNicely() {
        String res = "";
        String target = this.toString();
        if (target == null) {
            return "{}";
        }
        char c;
        int level = 0;
        target = target + " ";
        boolean flag = true;
        for (int i = 0; i < target.length() - 1; i++) {
            c = target.charAt(i);
            if( c == '"' ){
                flag = !flag;
            }
            if (flag) {
                if ((c == '{') || (c == '[')) {
                    level++;
                    res = res + c + "\n" + getTab(level);
                    continue;
                }
                if ((c == '}') || (c == ']')) {
                    level--;
                    if( target.charAt(i+1) == ',' ){
                        res = res + "\n" + getTab(level) + c + ",\n" + getTab(level);
                        i++;
                        continue;
                    }
                    res = res + "\n" + getTab(level) + c + "\n" + getTab(level);
                    continue;
                }
                if (c == ',') {
                    res = res + c + "\n" + getTab(level);
                    continue;
                }
                if ((c == ' ') || (c == '\t')) {
                    continue;
                }
            }
            res = res + c;
        }
        return res;
    }
    /**
     * It print the structure in the screen.
     */
    public void println() {
        msg(this.toString());
    }

}
