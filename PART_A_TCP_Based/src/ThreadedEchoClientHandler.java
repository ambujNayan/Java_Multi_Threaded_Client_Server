import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

class ThreadedEchoClientHandler implements Runnable
{
	//private Socket incoming;
	private int numOperations;
	private int threadId;
	private int port;
	private FileWriter fw;

	public ThreadedEchoClientHandler(int numOperations, int threadId, int port, FileWriter fw)
	{
		//this.incoming=incoming;
		this.numOperations=numOperations;
		this.threadId=threadId;
		this.port=port;
		this.fw=fw;
	}

	@Override public void run()
	{
		try
		{
			for(int i=0;i<numOperations;i++)
			{
				Socket incoming=new Socket("localhost", port);
				int fromUID = (int )(Math.random() * 100 + 1);
				int toUID = (int )(Math.random() * 100 + 1);
				java.io.InputStream inStream=incoming.getInputStream();
				java.io.OutputStream outStream=incoming.getOutputStream();
				ObjectOutputStream os=new ObjectOutputStream(outStream);
				ObjectInputStream oin=new ObjectInputStream(inStream);

				TransferRequest transferrequest=new TransferRequest("TransferRequest", fromUID, toUID, 10);
				os.writeObject(transferrequest);
				Response response=(Response) oin.readObject();
				TransferResponse transferresponse=(TransferResponse) response;
				if(!transferresponse.getTransferred())
					fw.append(transferresponse.getTransferred()+" "+fromUID+" "+toUID);
				incoming.close();
			}				
		}
		catch (IOException e)
		{
			System.out.println("Client exception");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}