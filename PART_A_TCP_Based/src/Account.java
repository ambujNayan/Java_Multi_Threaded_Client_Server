import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account
{
	private int UID;
	private int balance;
	private Lock accountLock;

	public Account()
	{
		accountLock=new ReentrantLock();
	}

	public void CreateAccount(int UID, int balance)
	{
		accountLock.lock();
		try
		{
			this.UID=UID;
			this.balance=balance;
		}
		finally
		{
			accountLock.unlock();
		}
	}

	public int GetBalance()
	{
		accountLock.lock();
		try
		{
			return balance;	
		}
		finally
		{
			accountLock.unlock();
		}
		
	}

	public void SetBalance(int amount)
	{
		accountLock.lock();
		try
		{
			balance=amount;
		}
		finally
		{
			accountLock.unlock();
		}
	}
}