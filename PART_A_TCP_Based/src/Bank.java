import java.util.Hashtable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank
{
	private Hashtable<Integer, Account> hashtable;
	private static int nextAccountNo=1;
	private Lock bankLock;
	private Condition sufficientFunds;

	public Bank()
	{
		hashtable=new Hashtable<Integer, Account>();
		bankLock=new ReentrantLock();
		sufficientFunds=bankLock.newCondition();
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
				return hashtable.get(UID).GetBalance();	
			else 
				return 0;
		}
		finally
		{
			bankLock.unlock();
		}
	}

	public boolean Transfer(int sourceUID, int targetUID, int amount) throws InterruptedException
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