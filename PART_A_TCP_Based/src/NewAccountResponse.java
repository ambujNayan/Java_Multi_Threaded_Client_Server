class NewAccountResponse extends Response 
{
	private int UID;

	public NewAccountResponse(String responseName, int UID)
	{
		super(responseName);
		this.UID=UID;
	}

	public int getUID()
	{
		return UID;
	}
}