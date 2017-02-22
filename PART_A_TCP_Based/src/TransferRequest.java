class TransferRequest extends Request
{
	private int fromUID;
	private int toUID;
	private int amount;

	public TransferRequest(String requestName, int fromUID, int toUID, int amount)
	{
		super(requestName);
		this.fromUID=fromUID;
		this.toUID=toUID;
		this.amount=amount;
	}

	public int getFromUID()
	{
		return fromUID;
	}

	public int getToUID()
	{
		return toUID;
	}

	public int getAmount()
	{
		return amount;
	}
}