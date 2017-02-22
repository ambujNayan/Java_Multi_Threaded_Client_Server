import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	public static void main(String[] args)
	{
		int port=Integer.parseInt(args[0]);
		try
		{
			int i=1;
			ServerSocket s=new ServerSocket(port);
			Bank bank=new Bank();
			FileWriter fw=new FileWriter("TCPServerLogfile", true);
			fw.append("SERVER LOG FILE\n");
			System.out.println("TCP SERVER STARTED");
			while(true)
			{
				Socket incoming=s.accept();
				AccountHandler r=new AccountHandler(incoming, bank, fw);
				Thread t=new Thread(r);
				t.start();
				i++;

			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
