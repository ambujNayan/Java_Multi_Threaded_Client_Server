class GetBalanceRequest extends Request
{
	private int UID;
	public GetBalanceRequest(String requestName, int UID)
	{
		super(requestName);
		this.UID=UID;
	}

	public int getUID()
	{
		return UID;
	}
}