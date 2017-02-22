import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class BankClient 
{
  public static void main(String [] args) throws IOException 
  {
	  String host=args[0];
	  int port=Integer.parseInt(args[1]);
	  int numThreads=Integer.parseInt(args[2]);
	  int numOperations=Integer.parseInt(args[3]);
	  FileWriter fw=new FileWriter("ThriftClientLogfile", true);
	  System.out.println("THRIFT CLIENT STARTED");

	  try 
	  {
	       TTransport transport; 
	       transport = new TSocket(host, port);
	       transport.open();
	       TProtocol protocol = new  TBinaryProtocol(transport);
	       AddService.Client client = new AddService.Client(protocol);
	       perform(client);
	       transport.close();
	  } 
	  catch (TException x) 
	  {
		   x.printStackTrace();
	  } 

	  try 
	  {
  			ArrayList<Thread> threadList=new ArrayList<Thread>();
  			int k=1;
  			for(int i=0;i<numThreads;i++)
  			{
  		        TTransport transport;
  		        transport = new TSocket(host, port);
  		        transport.open();
  		        TProtocol protocol = new  TBinaryProtocol(transport);
  				AddService.Client client = new AddService.Client(protocol);
  				ThreadedEchoClientHandler r=new ThreadedEchoClientHandler(transport, client, numOperations, k, fw);
  				Thread t=new Thread(r);
  				t.start();
  				threadList.add(t);
  				k++;
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
      } 
	  catch (TException x) 
	  {
		  x.printStackTrace();
      }
    
	  try 
	  {
		  TTransport transport;     
		  transport = new TSocket(host, port);
		  transport.open();
		  TProtocol protocol = new  TBinaryProtocol(transport);
		  AddService.Client client = new AddService.Client(protocol);
		  int totBalance=0;
		  for(int i=0;i<100;i++)
		  {
	  		  int balance=client.GetBalance(i+1);
	  		  totBalance+=balance;
		  }
		  System.out.println("TOTAL BALANCE-2: "+totBalance);

		  transport.close();
      } 
	  catch (TException x) 
	  {
        x.printStackTrace();
      } 
  }

  private static void perform(AddService.Client client) throws TException
  {
	  for(int i=0;i<100;i++)
	  {
		  int UID=client.CreateAccount();  
	  }

	  for(int i=0;i<100;i++)
	  {
		boolean deposit=client.Deposit((i+1), 100);
	  }
	
	  int totBalance=0;
	  for(int i=0;i<100;i++)
	  {
		  int balance=client.GetBalance(i+1);
		  totBalance+=balance;
	  }
	  System.out.println("TOTAL BALANCE-1: "+totBalance);
  }
}
