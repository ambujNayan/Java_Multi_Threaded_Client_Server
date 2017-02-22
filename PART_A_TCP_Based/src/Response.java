import java.io.Serializable;

class Response implements Serializable
{
	private String responseName;

	public Response(String responseName)
	{
		this.responseName=responseName;
	}

	public String getResponseName()
	{
		return responseName;
	}
}