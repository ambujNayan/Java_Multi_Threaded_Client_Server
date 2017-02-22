class GetBalanceResponse extends Response
{
	private int UID;
	private int balance;

	public GetBalanceResponse(String responseName, int UID, int balance)
	{
		super(responseName);
		this.UID=UID;
		this.balance=balance;
	}

	public int getUID()
	{
		return UID;
	}

	public int getBalance()
	{
		return balance;
	}
}