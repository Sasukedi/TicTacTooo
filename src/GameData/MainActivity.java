package GameData;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.io.*;
import java.util.*;
import java.util.Timer;


public class MainActivity extends JFrame {
	GameData gameData = new GameData();

    String serverurl = "http://midas.ktk.bme.hu/poti/";

    int playerNO =0;
    int refresh = 0;
    int upstat;
    
    JButton buttons_global[]=new JButton[9]; //btnMap
    
    JTextArea tvP2 = new JTextArea();
    JTextArea tverror = new JTextArea();
    JTextArea tvplayerid = new JTextArea();
    TextField etP1=new TextField("theLegend27");
    
	public MainActivity() throws Exception {
		 
	    // Create panel p1 for the buttons and set GridLayout
	    JPanel p1 = new JPanel();
	    p1.setLayout(new GridLayout(4, 3));

	    JButton buttons[]=new JButton[9];
	    
	    // Add buttons to the panel
	    for (int i = 0; i <= 8; i++) {
	    	buttons_global[i]=new JButton("");
	        p1.add(buttons_global[i]);
	    }

	    JButton Startbtn= new JButton("START");
	    JButton Refreshbtn= new JButton("REFRESH");
	    JButton Restartbtn= new JButton("RESTART");
	    p1.add(Startbtn);
	    p1.add(Refreshbtn);
	    p1.add(Restartbtn);
	    
	    Refreshbtn.setVisible(false);
	    

	    // Create panel p2 to hold a text field and p1
	    JPanel textLayout=new JPanel();
	    textLayout.setLayout(new GridLayout(1, 4));
	    textLayout.add(tvP2);
	    textLayout.add(tverror);
	    textLayout.add(tvplayerid);
	    textLayout.add(etP1);
	    
	    JPanel p2 = new JPanel(new BorderLayout());
	    p2.add(textLayout,BorderLayout.NORTH);
	    p2.add(p1, BorderLayout.CENTER);

	    // add contents into the frame
	    add(p2, BorderLayout.CENTER);
	    
	    updateData();
	    
	    Startbtn.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	          drawMap(gameData);
	          gameData.setPlayerNO(gameData.getAvaible());
	          etP1.setEnabled(false);
	          
	          if (gameData.getAvaible()==0) {
	        	  tvplayerid.setText("you are: X");
	        	  gameData.setAvaible(1);
	        	  gameData.setP1(etP1.getText().toString());
	        	  playerNO=gameData.getPlayerNO();
	        	  try {
					uploadData();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	  tvP2.setText("waiting for p2");
	        	  refresh=1;
	        	  for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }
	        	  
	          }
	          else if (gameData.getAvaible() == 1 )
              {
                  tvplayerid.setText("you are: O");
                  gameData.setAvaible(2);
                  gameData.setP2(etP1.getText().toString());
                  playerNO = gameData.getPlayerNO();

                  tvP2.setText(gameData.getP1());
                  try {
					uploadData();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                  tverror.setText("waiting for p1");

                  refresh = 1;

                  for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }

              }
	          
	          else {
                  tvplayerid.setText("the server is full");
              }

              Startbtn.setEnabled(false);
	        }
	      });
	    
	    Restartbtn.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {  
	        	if (gameData.getRefresh()==0)
	        	{
	        		gameData.setRefresh(gameData.getPlayerNO());
	        	} else if (gameData.getRefresh()!=gameData.getPlayerNO())
	        	{
	        		gameData.setRefresh(0);
	        		tverror.setText("");
	        	}
	        	restarted();
	        }
	      });
	    
	    for (int i =0 ; i < 9;i++)
        {
            final int finalI = i;
            buttons_global[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (gameData.getPlayerNO() == 1 && gameData.getAvaible()== 2 )
                    {
                        gameData.setCell(finalI, gameData.getPlayerNO() );
                        drawMap(gameData);
                        gameData.setAvaible(3);
                        tverror.setText("waiting for p2");
                        try {
							uploadData();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

                        for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }

                    }

                    if (gameData.getPlayerNO() == 2 && gameData.getAvaible()== 3 )
                    {
                        gameData.setCell(finalI, gameData.getPlayerNO() );
                        drawMap(gameData);
                        gameData.setAvaible(2);
                        tverror.setText("waiting for p1");
                        try {
							uploadData();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}



                        for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }
                    }

                }
            });
        }
	    
	    Timer uploadCheckerTimer = new Timer(true);
		 uploadCheckerTimer.scheduleAtFixedRate(
		     new TimerTask() {
		       public void run() {
		    	   int tempPlayerNO = gameData.getPlayerNO();
		    	   updateAll();
		    	   gameData.setPlayerNO_d(tempPlayerNO);
		    	   if (tvP2.getText().equals("")) {Startbtn.setEnabled(true);}
		    	   if (gameData.getRefresh()!=0)
		    	   {
		    		   if (gameData.getRefresh()!=gameData.getPlayerNO()) {
		    			   tverror.setText("The other player requested a restart!");
		    			   tvplayerid.setText("");
		    			   for (int i = 0; i < 9; i++) {
		    				   buttons_global[i].setEnabled(false);
		    			   }
		    		   } else
		    		   {
		    			   tverror.setText("Wait for other player's restart!");
		    		   }
		    	   } else if (tverror.getText().equals("Wait for other player's restart!"))
		    	   {
		    		   tverror.setText("Ready to start");
		    	   }
		       }
		     }, 0, 500);
	    
	    /*Refreshbtn.addActionListener(new ActionListener() {
	    	 @Override
		     public void actionPerformed(ActionEvent e) {
	    		 try {
					updateData();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		 
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
        });*/
	}
	    
	public void updateAll() {
	    try {
	    	updateData();
	    } catch (Exception e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	    
	    
	    if (gameData.getRefresh()!=0 || gameData.getPlayerNO()!=gameData.getRefresh()) {
	    	//VALAMIS DADASDASDASD
	    }
	    
	    
	    if (gameData.hasSomeoneWon()!=0 || gameData.isGameEnd()) {
	    	switch (gameData.hasSomeoneWon()) 
	    	{
	    	case 1:
	    		tverror.setText("Winner: P1, "+gameData.getP1());
	    		for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }
	    		break;
	    	case 2:
	    		tverror.setText("Winner: P2, "+gameData.getP2());
	    		for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }
	    		break;
	    	default:
	    		tverror.setText("Game Over");
	    		for (int i =0; i <9;i++) { buttons_global[i].setEnabled(false); }
	    		break;
	    	}
	    }else
	    {
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
	}
	    
	    public void restarted() {
	    	tvP2.setText("");
	    	tvplayerid.setText("");
	    	gameData.setAvaible(0);
	    	int[] map = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	    	gameData.setMap(map);
	    	try {
	    		uploadData();
	    	} catch (Exception e1) {
	    		// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    	}
	    	refresh = 1;
	    	for (int i = 0; i < 9; i++) {
	    		buttons_global[i].setEnabled(true);
	    		buttons_global[i].setText("");
	    	}

	    	etP1.setEnabled(true);
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
        gd.setRefresh(Integer.parseInt(parts[12]));


        return gd;
    }
	
	
	
	public void drawMap (GameData gd) {
        for (int i = 0; i<9;i++)
        {
            buttons_global[i].setText( XorO(gd.getMap()[i]) );
            if(! "".equals(XorO(gd.getMap()[i])))
            {
                buttons_global[i].setEnabled(false);
            }

            else {buttons_global[i].setEnabled(true); }
        }
    }
	
	public String XorO (int e)
    {
        String val= "";
        if (e == 1) val = "X";
        if (e == 2) val = "O";
        return val;
    }
	
	
	public void draw (GameData gd)
    {
        for (int i = 0; i<9;i++) {
            buttons_global[i].setText( XorO(gd.getMap()[i]) );

        }

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
	            generated = "http://midas.ktk.bme.hu/poti/?csv=" + gameData.getAvaible() + ";" + gd.getP1() + ";waitingforplayer2;" + MAP + gameData.getRefresh() + ";";
	        }

	        else
	        {
	            generated = "http://midas.ktk.bme.hu/poti/?csv=" + gameData.getAvaible() + ";" + gd.getP1() + ";" + gd.getP2() + ";" + MAP + gameData.getRefresh() + ";";
	        }


	        return generated;
	    }
	 
     //url reader
	 public static class URLConnectionReader {
		    public static String getText(String url) throws Exception {
		        URL website = new URL(url);
		        URLConnection connection = website.openConnection();
		        BufferedReader in = new BufferedReader(
		                                new InputStreamReader(
		                                    connection.getInputStream()));

		        StringBuilder response = new StringBuilder();
		        String inputLine;

		        while ((inputLine = in.readLine()) != null) 
		            response.append(inputLine);

		        in.close();

		        return response.toString();
		    }
		}
	 
	 //update data from url
	 public void updateData() throws Exception {
		 String response = URLConnectionReader.getText(serverurl);
		 gameData=datafromurl(response);
	 }
	 
	 //upload current data to server
	 public void uploadData() throws Exception {
		 String response = URLConnectionReader.getText(generateURL(gameData));
		 if (response.endsWith("OK")) upstat=1;
		 else upstat=0;
	 }
	 
	  /** Main method */
	  public static void main(String[] args) throws Exception {
	    MainActivity frame = new MainActivity();
	    frame.setTitle("TicTacToe");
	    frame.setSize(1000, 1000);
	    frame.setLocationRelativeTo(null); // Center the frame
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	  }
}


