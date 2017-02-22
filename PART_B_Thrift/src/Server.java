import java.io.FileWriter;
import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;


@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
public class Server 
{

  public static AddHandler handler;
  public static AddService.Processor processor;
  public static void main(String [] args)
  {
	int port=Integer.parseInt(args[0]);
    try 
    {
      handler = new AddHandler();
      processor = new AddService.Processor(handler);

      Runnable simple = new Runnable() 
      {
        public void run() 
        {
          someMethod(processor, port);
        }
      };      

      new Thread(simple).start();
    } 
    catch (Exception x) 
    {
      x.printStackTrace();
    }
  }

  public static void someMethod(AddService.Processor processor, int port) 
  {
    try 
    {
      TServerTransport serverTransport = new TServerSocket(port);
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
      System.out.println("THRIFT SERVER STARTED");
      server.serve();
    } 
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
}
