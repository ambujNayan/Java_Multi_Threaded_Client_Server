class DepositResponse extends Response
{
	private int UID;
	private int amount;
	private boolean deposited;

	public DepositResponse(String responseName, int UID, int amount, boolean deposited)
	{
		super(responseName);
		this.UID=UID;
		this.amount=amount;
		this.deposited=deposited;
	}

	public int getUID()
	{
		return UID;
	}

	public int getAmount()
	{
		return amount;
	}

	public boolean getDeposited()
	{
		return deposited;
	}
}