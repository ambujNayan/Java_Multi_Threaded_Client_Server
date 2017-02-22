import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

class ThreadedEchoClientHandler implements Runnable
{
	//private Socket incoming;
	private int numOperations;
	private int threadId;
	private AddService.Client client;
	private TTransport transport;
	private FileWriter fw;

	public ThreadedEchoClientHandler(TTransport transport, AddService.Client client, int numOperations, int threadId, FileWriter fw)
	{
		this.numOperations=numOperations;
		this.threadId=threadId;
		this.client=client;
		this.transport=transport;
		this.fw=fw;
	}

	@Override public void run()
	{
		for(int i=0;i<numOperations;i++)
		{
			int fromUID = (int )(Math.random() * 100 + 1);
			int toUID = (int )(Math.random() * 100 + 1);
			try 
			{
				boolean status =client.Transfer(fromUID, toUID, 10);
				if(!status)
					fw.append(status+" "+fromUID+" "+toUID);
			} 
			catch (TException e) 
			{
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		transport.close();
	}
}