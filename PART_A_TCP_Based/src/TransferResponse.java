class TransferResponse extends Response
{
	private int fromUID;
	private int toUID;
	private int amount;
	private boolean transferred;

	public TransferResponse(String responseName, int fromUID, int toUID, int amount, boolean transferred)
	{
		super(responseName);
		this.fromUID=fromUID;
		this.toUID=toUID;
		this.amount=amount;
		this.transferred=transferred;
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

	public boolean getTransferred()
	{
		return transferred;
	}
}