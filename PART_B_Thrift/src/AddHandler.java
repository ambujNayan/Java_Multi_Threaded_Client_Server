import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.thrift.TException;

public class AddHandler implements AddService.Iface {

	/*
        @Override
        public void ping() throws TException {
            System.out.println("Server got a ping"); 
        } 

	@Override
	public int add(int n1, int n2) throws TException {
	    System.out.println("Add(" + n1 + "," + n2 + ")");
	    return n1 + n2;
	}
        
        @Override
        public void zip() throws TException {
            System.out.println("zip method called on Server");
        }
*/
	private Hashtable<Integer, Account> hashtable;
	private static int nextAccountNo=1;
	private Lock bankLock;
	private Condition sufficientFunds;
	private FileWriter fw;

	public AddHandler()
	{
		hashtable=new Hashtable<Integer, Account>();
		bankLock=new ReentrantLock();
		sufficientFunds=bankLock.newCondition();
		try {
			fw=new FileWriter("ThriftServerLogfile", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int CreateAccount()
	{
		bankLock.lock();
		try
		{
			int accountNo=nextAccountNo++;
			Account newAccount=new Account();
			newAccount.CreateAccount(accountNo, 0);
			hashtable.put(accountNo, newAccount);
			try {
				fw.append("NewAccountRequest"+" "+accountNo+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return accountNo;	
		} 
		finally
		{
			bankLock.unlock();
		}
		
	}

	public boolean Deposit(int UID, int amount)
	{
		bankLock.lock();
		try
		{
			if(hashtable.containsKey(UID) && amount>0)
			{
				hashtable.get(UID).SetBalance(hashtable.get(UID).GetBalance()+amount);
				try {
					fw.append("DepositRequest"+" "+UID+" "+amount+" "+"true"+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
			else
				return false;	
		}
		finally
		{
			bankLock.unlock();
		}
	}

	public int GetBalance(int UID)
	{
		bankLock.lock();
		try
		{
			
			if(hashtable.containsKey(UID))
			{
				try {
					fw.append("GetBalanceRequest"+" "+UID+" "+hashtable.get(UID).GetBalance()+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return hashtable.get(UID).GetBalance();	
			}
				
			else 
				return 0;
		}
		finally
		{
			bankLock.unlock();
		}
	}

	public boolean Transfer(int sourceUID, int targetUID, int amount) 
	{
		bankLock.lock();
		try
		{
			if(hashtable.containsKey(sourceUID) && hashtable.containsKey(targetUID) && amount>0)
			{
				//while(hashtable.get(sourceUID).GetBalance()<amount)
				//	sufficientFunds.await();
				if(hashtable.get(sourceUID).GetBalance()<amount)
					return false;
				int sourceAmount=hashtable.get(sourceUID).GetBalance()-amount;
				hashtable.get(sourceUID).SetBalance(sourceAmount);
				int targetAmount=hashtable.get(targetUID).GetBalance()+amount;
				hashtable.get(targetUID).SetBalance(targetAmount);
				try {
					fw.append("TransferRequest"+" "+sourceUID+" "+targetUID+" "+amount+" "+"true"+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				//sufficientFunds.signalAll();
				return true;
			}	
			else
				return false;
		}
		finally
		{
			bankLock.unlock();
		}
	}
}
