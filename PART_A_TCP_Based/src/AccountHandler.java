import java.util.*;
import java.io.*;
import java.net.*;

import org.omg.CORBA.portable.InputStream;

public class AccountHandler implements Runnable
{
	private Socket incoming;
	private Bank bank;
	private FileWriter fw;

	public AccountHandler(Socket incoming, Bank bank, FileWriter fw)
	{
		this.incoming=incoming;
		this.bank=bank;
		this.fw=fw;
	}

	@Override public void run()
	{
		try
		{
			try
			{
				java.io.InputStream inStream=incoming.getInputStream();
				ObjectInputStream oin=new ObjectInputStream(inStream);
				OutputStream outStream=incoming.getOutputStream();
				ObjectOutputStream os=new ObjectOutputStream(outStream);
				Request request=(Request) oin.readObject();
				String actionName=request.getRequestName();

				switch(actionName)
				{
					case "NewAccountRequest":
						NewAccountRequest newaccountrequest=(NewAccountRequest) request;
						NewAccountResponse newaccountresponse=new NewAccountResponse("NewAccountResponse", bank.CreateAccount());
						fw.append("NewAccountRequest"+" "+newaccountresponse.getUID()+"\n");
						os.writeObject(newaccountresponse);
						break;
					case "DepositRequest":
						DepositRequest depositrequest=(DepositRequest) request;
						DepositResponse depositresponse=new DepositResponse("DepositResponse", depositrequest.getUID(), depositrequest.getAmount(), bank.Deposit(depositrequest.getUID(), depositrequest.getAmount()));
						fw.append("DepositRequest"+" "+depositresponse.getUID()+" "+depositresponse.getAmount()+" "+depositresponse.getDeposited()+"\n");
						os.writeObject(depositresponse);
						break;
					case "GetBalanceRequest":
						GetBalanceRequest getbalancerequest=(GetBalanceRequest) request;
						GetBalanceResponse getbalanceresponse=new GetBalanceResponse("GetBalanceResponse", getbalancerequest.getUID(), bank.GetBalance(getbalancerequest.getUID()));
						fw.append("GetBalanceRequest"+" "+getbalanceresponse.getUID()+" "+getbalanceresponse.getBalance()+"\n");
						os.writeObject(getbalanceresponse);
						break;
					case "TransferRequest":
						TransferRequest transferrequest=(TransferRequest) request;
						TransferResponse transferresponse=new TransferResponse("TransferResponse", transferrequest.getFromUID(), transferrequest.getToUID(), transferrequest.getAmount(), bank.Transfer(transferrequest.getFromUID(), transferrequest.getToUID(), transferrequest.getAmount()));
						fw.append("TransferRequest"+" "+transferresponse.getFromUID()+" "+transferresponse.getToUID()+" "+transferresponse.getAmount()+" "+transferresponse.getTransferred()+"\n");
						os.writeObject(transferresponse);
						break;
					default:
						break;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally 
			{
				incoming.close();
			}
		}
		catch (IOException e)
		{
			System.out.println("Server exception");
			e.printStackTrace();
		}
	}
}
