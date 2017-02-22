import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class BankClient 
{
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException
	{
		String host=args[0];
		int port=Integer.parseInt(args[1]);
		int numThreads=Integer.parseInt(args[2]);
		int numOperations=Integer.parseInt(args[3]);
		FileWriter fw=new FileWriter("TCPClientLogfile", true);

		System.out.println("TCP CLIENT STARTED");

		createAccounts(host, port, 100);
		depositAmount(host, port, 100, 100);
		getBalance(host, port, 100);

		// MULTI-THREADED EXECUTION
		ArrayList<Thread> threadList=new ArrayList<Thread>();
		{
			int k=1;
			for(int i=0;i<numThreads;i++)
			{
				ThreadedEchoClientHandler r=new ThreadedEchoClientHandler(numOperations, k, port, fw);
				Thread t=new Thread(r);
				t.start();
				threadList.add(t);
				k++;

			}
		}

		for (Thread thread:threadList) 
		{
            try
            {
                thread.join();
            }
            catch (Exception e) 
            {
                System.out.println(e);
            }
        }

        getBalance(host, port, 100);
	}

	public static void createAccounts(String host, int port, int n) throws UnknownHostException, IOException, ClassNotFoundException
	{
		// CREATE 100 ACCOUNTS ON SERVER
		for(int i=0;i<n;i++)
		{
			Socket s=new Socket(host, port);
			InputStream inStream=s.getInputStream();
			OutputStream outStream=s.getOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(outStream);
			ObjectInputStream oin=new ObjectInputStream(inStream);
			NewAccountRequest newaccountrequest=new NewAccountRequest("NewAccountRequest");
			
			try 
			{
				os.writeObject(newaccountrequest);
				Response response=(Response) oin.readObject();
				NewAccountResponse newaccountresponse=(NewAccountResponse) response;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			s.close();
		}
	}

	public static void depositAmount(String host, int port, int n, int amount) throws UnknownHostException, IOException, ClassNotFoundException
	{
		// DEPOSIT 100$ IN EACH OF 100 ACCOUNTS ON SERVER
		for(int i=0;i<n;i++)
		{
			Socket s=new Socket(host, port);
			InputStream inStream=s.getInputStream();
			OutputStream outStream=s.getOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(outStream);
			ObjectInputStream oin=new ObjectInputStream(inStream);
			DepositRequest depositrequest=new DepositRequest("DepositRequest", (i+1), amount);
			try 
			{
				os.writeObject(depositrequest);
				Response response=(Response) oin.readObject();
				DepositResponse depositresponse=(DepositResponse) response;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			s.close();
		}
	}

	public static void getBalance(String host, int port, int n) throws UnknownHostException, IOException, ClassNotFoundException
	{
		int totBalance=0;
		for(int i=0;i<n;i++)
		{
			Socket s=new Socket(host, port);
			InputStream inStream=s.getInputStream();
			OutputStream outStream=s.getOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(outStream);
			ObjectInputStream oin=new ObjectInputStream(inStream);
			GetBalanceRequest getbalancerequest=new GetBalanceRequest("GetBalanceRequest", (i+1));
			try 
			{
				os.writeObject(getbalancerequest);
				Response response=(Response) oin.readObject();
				GetBalanceResponse getbalanceresponse=(GetBalanceResponse) response;
				totBalance+=getbalanceresponse.getBalance();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			s.close();
		}
		System.out.println("TOTAL BALANCE: "+totBalance);
	}
}