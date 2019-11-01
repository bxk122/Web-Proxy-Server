import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ProxyServer
{
  
   public static void main(String[] args) throws IOException
   {
   	PrintWriter writer1 = new PrintWriter("checklist.txt", "UTF-8");
   	ServerSocket server = new ServerSocket(7777);
		System.out.println("This is Proxy, connecting....");
		boolean val = true;
		boolean found = false;
		int count  = 1;
		String urlhtml = null;
		
		//updating the database.
		Timer timer = new Timer();    
		TimerTask task = new TimerTask()
       {        
			// periodically updates the database after 5 min 
			public void run()
           {
				System.out.println("Updating information in the database");
				BufferedReader reader7;
				try
				{
					reader7 = new BufferedReader(new FileReader(
						"C:\\Users\\BadalKarki\\College\\ CS 470\\Project 2 V2\\"
							+ "checkList.txt"));
					String line7 = reader7.readLine();
					while (line7 != null)
					{
						String [] lineBreak7 = line7.split(" ");
						String url7 = lineBreak7[0];
						String urlCheck7 = lineBreak7[1];
						
						URL urlhtml7 = new URL(url7);
					    String urlhtml1 = new String( urlCheck7 + ".html");
					    System.out.println("Updating: " + urlhtml1 + " \n" );
					    /**
					    * end citation: https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
					    */
					    PrintWriter writer7 = new PrintWriter(urlhtml1, "UTF-8");
					    URLConnection con7 = urlhtml7.openConnection();
					    InputStream is7 =con7.getInputStream();
					    BufferedReader br7 = new BufferedReader(new InputStreamReader(is7));
				
					    String line71 = null;
					   
					    while ((line71 = br7.readLine()) != null)
				        {
				            //System.out.println(line);
				        	writer7.println(line71);
				        }
				        writer7.flush();				    	
				    	
						line7 = reader7.readLine();
					}

				}
				catch (FileNotFoundException e)
				{
					System.out.println("File not Found \n");
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				};  				
           }
       };
       long delay = 5 * 60 * 1000;
       long intevalPeriod = 5 * 60 * 1000 ;
       timer.scheduleAtFixedRate(task, delay, intevalPeriod);
      
		while(val)
		{
          
			Socket clientSocket = server.accept();
			InputStreamReader clientServer = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader sendClient = new BufferedReader(clientServer);
			
			//Breaks into the URL form
			String sendString = sendClient.readLine();
			String [] infoString = sendString.split(" ");
			String url = infoString[1];
			System.out.println("CLient is requesting " + url);
			
	       
	        //Scans to see if there is the url in the database
			System.out.println("Checking to see if our database has the file...");
	        BufferedReader reader = new BufferedReader(new FileReader(
					"C:\\Users\\BadalKarki\\College\\ CS 470\\Project 2 V2\\checklist.txt"));;
			String line1 = reader.readLine();
			while (line1 != null)
			{
				String [] lineBreak = line1.split(" ");
				String urlCheck = lineBreak[0];
				if (urlCheck.equals(url))
		        {
					found = true;
					urlhtml = lineBreak[1];
		            System.out.println("We do have the file being asked.");
		            System.out.println("Sending the file to the client...");
		            break;
		        }
				line1 = reader.readLine();
			}
			reader.close();
			
			if(found == true)
			{
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		    	String fileLocate = new String(
		    			"C:\\Users\\BadalKarki\\College\\ CS 470\\Project 2 V2\\" + urlhtml+".html");
		    	File file1 = new File(fileLocate);
	
		    	Scanner scan = new Scanner(file1);
	
		    	while (scan.hasNextLine())
		    	{
		    		out.println(scan.nextLine());
		    	}
		    	scan.close();
		    	out.flush();
		    	out.close();
		    	found = false;
		    	System.out.println("File Sent \n");
			}
	    	
	    	else //contacts the server
	        {
	        	System.out.println("We do not have the file.");
	        	System.out.println("Contacting the server.....");
	        	//writing in the checklist
				writer1.write( url + " " + count + "\r\n");
				writer1.flush();
		        URL serverSite = new URL(url);
		        /**
		         * This part of the code deals with HTTP get and HTTP response code
		         * beginning citation: https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
		         */
		        HttpURLConnection con = (HttpURLConnection) serverSite.openConnection();
	
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", "Mozilla/5.0");
				int responseCode = con.getResponseCode();
				System.out.println("Sending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);
				
		        URL url1 = new URL(url);
		        urlhtml = new String(count + ".html");
		        /**
		         * end citation: https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
		         */
		    	PrintWriter writer = new PrintWriter(urlhtml, "UTF-8");
		        URLConnection con1 = url1.openConnection();
		        InputStream is =con1.getInputStream();
		        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
		        String line = null;
		       
		        //writing in the html file
		        while ((line = br.readLine()) != null)
		        {
		            //System.out.println(line);
		        	writer.println(line);
		        }
		        writer.flush();
		    	writer.close();
		    	count++;

		        //Send the html to the client
		    	System.out.println("We have received the file.");
	            System.out.println("Sending the file to the client...");
		    	PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		    	String fileLocate = new String(
		    			"C:\\Users\\BadalKarki\\College\\ CS 470\\Project 2  V2\\" + urlhtml);
		    	File file1 = new File(fileLocate);

		    	Scanner scan = new Scanner(file1);

		    	while (scan.hasNextLine())
		    	{
		    		out.println(scan.nextLine());
		    	}
		    	scan.close();
		    	out.flush();
		    	out.close();
		    	System.out.println("File Sent \n");
	        }
		}
       writer1.close();
       server.close();
   }
}
