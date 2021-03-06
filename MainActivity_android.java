package com.example.dani.tictactooo;

import android.net.http.RequestQueue;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    EditText etP1;
    TextView tvP2;
    TextView tverror;
    TextView tvplayerid;
    Button startbtn;
    Button refreshbtn;
    Button btnMap[] = new  Button[9];

    GameData gameData = new GameData();

    String serverurl = "http://midas.ktk.bme.hu/poti/";

    int playerNO =0;

    int refresh = 0;

    int upstat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startbtn = (Button)findViewById(R.id.startbtn);
        tvP2 = (TextView)findViewById(R.id.p2id);
        tverror = (TextView)findViewById(R.id.error1);
        tvplayerid =(TextView)findViewById(R.id.playerid);
        etP1 = (EditText) findViewById(R.id.p1id);
        refreshbtn = (Button)findViewById(R.id.refreshbtn);
        btnMap[0] = (Button) findViewById(R.id.btn0);
        btnMap[1] = (Button) findViewById(R.id.btn1);
        btnMap[2] = (Button) findViewById(R.id.btn2);
        btnMap[3] = (Button) findViewById(R.id.btn3);
        btnMap[4] = (Button) findViewById(R.id.btn4);
        btnMap[5] = (Button) findViewById(R.id.btn5);
        btnMap[6] = (Button) findViewById(R.id.btn6);
        btnMap[7] = (Button) findViewById(R.id.btn7);
        btnMap[8] = (Button) findViewById(R.id.btn8);

        updateData();

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawMap(gameData);
                gameData.setPlayerNO(gameData.getAvaible());
                //tverror.setText("NO"+Integer.toString(gameData.getPlayerNO())+ "avala" + Integer.toString(gameData.getAvaible()));

                if (gameData.getAvaible()== 0)
                {
                    tvplayerid.setText("you are: X");
                    gameData.setAvaible(1);
                    gameData.setP1(etP1.getText().toString());
                    playerNO = gameData.getPlayerNO();
                    uploadData();
                    tvP2.setText("waitign for p2");

                    refresh = 1;

                    for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }


                }

                else if (gameData.getAvaible() == 1 )
                {
                    tvplayerid.setText("you are: O");
                    gameData.setAvaible(2);
                    gameData.setP2(etP1.getText().toString());
                    playerNO = gameData.getPlayerNO();

                    tvP2.setText(gameData.getP1());
                    uploadData();
                    tverror.setText("waiting for p1");

                    refresh = 1;

                    for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }

                }

                else {
                    tvplayerid.setText("the server is full");
                }

                startbtn.setClickable(false);

            }
        });

        for (int i =0 ; i < 9;i++)
        {
            final int finalI = i;
            btnMap[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //tverror.setText("you hit " + Integer.toString(finalI));

                    if (gameData.getPlayerNO() == 1 && gameData.getAvaible()== 2 )
                    {
                        gameData.setCell(finalI, gameData.getPlayerNO() );
                        drawMap(gameData);
                        gameData.setAvaible(3);
                        tverror.setText("waiting for p2");
                        uploadData();

                        for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }

                    }

                    if (gameData.getPlayerNO() == 2 && gameData.getAvaible()== 3 )
                    {
                        gameData.setCell(finalI, gameData.getPlayerNO() );
                        drawMap(gameData);
                        gameData.setAvaible(2);
                        tverror.setText("waiting for p1");
                        uploadData();



                        for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }
                    }

                }
            });
        }

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

                if (gameData.getPlayerNO() == 1 && gameData.getAvaible()== 2)
                {
                    tvP2.setText(gameData.getP2());
                    drawMap(gameData);
                    tverror.setText("your turn P1");

                }

                if (gameData.getPlayerNO() == 2 && gameData.getAvaible()== 3)
                {
                    drawMap(gameData);
                    tverror.setText("your turn P2");
                }

            }
        });


      /*  new CountDownTimer(Integer.MAX_VALUE, 500)
        {
            public void onTick(long millisUntilFinished)
            {
                // do something every 5 seconds...
                updateData();
                //draw(gameData);

                if (gameData.getPlayerNO() == 1 && gameData.getAvaible()== 2)
                {
                    tvP2.setText(gameData.getP2());
                    drawMap(gameData);
                    tverror.setText("your turn P1");

                }

                if (gameData.getPlayerNO() == 2 && gameData.getAvaible()== 3)
                {
                    drawMap(gameData);
                    tverror.setText("your turn P2");
                }

            }

            public void onFinish()
            {
                // finish off when we're all dead !
            }
        }.start();*/



    }


    public GameData datafromurl(String str)
    {
        GameData gd = new GameData();

        String[] parts = str.split(";");

        gd.setAvaible(Integer.parseInt(parts[0]));
        gd.setP1(parts[1]);
        gd.setP2(parts[2]);
        gd.setPlayerNO_d(playerNO);
        int[] asd = new int[9];
        for (int i = 0; i < 9; i++)
        {
            asd[i] = Integer.parseInt(parts[i+3]);
        }
        gd.setMap(asd);


        return gd;
    }

    public void drawMap (GameData gd)
    {

        for (int i = 0; i<9;i++)
        {
            btnMap[i].setText( XorO(gd.getMap()[i]) );
            if(! "".equals(XorO(gd.getMap()[i])))
            {
                btnMap[i].setClickable(false);
            }

            else { btnMap[i].setClickable(true); }

        }


    }

    public void draw (GameData gd)
    {

        for (int i = 0; i<9;i++)
        {
            btnMap[i].setText( XorO(gd.getMap()[i]) );

        }


    }

    public String XorO (int e)
    {
        String val= "";
        if (e == 1) val = "X";
        if (e == 2) val = "O";
        return val;
    }

    public void uploadData()
    {
        upstat = 0;

        final com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, generateURL(gameData),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.endsWith("OK") )
                        {
                            upstat =1;
                            //tverror.setText("upload sucsess");
                        }

                        else upstat=0;

                        requestQueue.stop();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                upstat = 0;
                tverror.setText("upload failed");
                error.printStackTrace();
                //downloaded ="";
                requestQueue.stop();

            }
        });

        requestQueue.add(stringRequest);


    }

    public String generateURL(GameData gd)
    {
        String generated;
        String MAP ="";
        for(int i = 0; i <9;i++)
        {

            MAP = MAP+ Integer.toHexString(gd.getMap()[i])+ ";" ;
        }

        if (gameData.getAvaible() == 0) {
            generated = "http://midas.ktk.bme.hu/poti/?csv=" + gameData.getAvaible() + ";" + gd.getP1() + ";waitingforplayer2;" + MAP;
        }

        else
        {
            generated = "http://midas.ktk.bme.hu/poti/?csv=" + gameData.getAvaible() + ";" + gd.getP1() + ";" + gd.getP2() + ";" + MAP;
        }


        return generated;
    }

    public void updateData()
    {

        final com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //tverror.setText(response);
                        gameData  = datafromurl(response);
                        //tverror.setText(gameData.getAvaible());
                        requestQueue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                tverror.setText("fail");
                error.printStackTrace();
                requestQueue.stop();

            }
        });
        requestQueue.add(stringRequest);

    }

}

