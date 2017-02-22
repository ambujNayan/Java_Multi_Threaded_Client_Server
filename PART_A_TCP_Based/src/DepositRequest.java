class DepositRequest extends Request
{
	private int UID;
	private int amount;

	public DepositRequest(String requestName, int UID, int amount)
	{
		super(requestName);
		this.UID=UID;
		this.amount=amount;
	}

	public int getUID()
	{
		return UID;
	}

	public int getAmount()
	{
		return amount;
	}
}