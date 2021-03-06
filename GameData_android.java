package com.example.dani.tictactooo;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Dani on 2017. 04. 02..
 */

public class GameData {

    private int Avaible;
    protected int PlayerNO;
    private String P1;
    private String P2;
    private int[] Map;  //0 üres 1 x 2 O
    private  String ErrorStr;
    private String serverurl = "http://midas.ktk.bme.hu/poti/";

    /*
    map:

    [0  1   2
     3  4   5
     6  7   8]

     */

    public int getAvaible() {
        return Avaible;
    }

    public void setAvaible(int avaible) {
        Avaible = avaible;
    }

    public String getP1() {
        return P1;
    }

    public void setP1(String p1) {
        P1 = p1;
    }

    public String getP2() {
        return P2;
    }

    public void setP2(String p2) {
        P2 = p2;
    }

    public int[] getMap() {
        return Map;
    }

    public void setMap(int[] map) {
        Map = map;
    }

    public  void setCell(int cell, int val) { Map[cell] = val; }

    public String getErrorStr() { return ErrorStr; }

    public void setErrorStr(String errorStr) { ErrorStr = errorStr; }

    public int getPlayerNO() {
        return PlayerNO;
    }

    public void setPlayerNO(int avaible) {
        if (avaible == 0 ) { PlayerNO = 1; }
        else if (avaible == 1 ) { PlayerNO = 2;}
        else {PlayerNO = 0; }
    }

    public void setPlayerNO_d(int playerNO) {
        PlayerNO = playerNO;
    }

    public GameData datafromurl(String str)
    {
        GameData gd = new GameData();

        String[] parts = str.split(";");

        gd.setAvaible(Integer.parseInt(parts[0]));
        gd.setP1(parts[1]);
        gd.setP2(parts[2]);
        int[] asd = new int[9];
        for (int i = 0; i < 9; i++)
        {
            asd[i] = Integer.parseInt(parts[i+3]);
        }
        gd.setMap(asd);

        return gd;
    }



}
