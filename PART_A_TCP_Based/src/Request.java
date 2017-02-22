import java.io.Serializable;

class Request implements Serializable
{
	private String requestName;

	public Request(String requestName)
	{
		this.requestName=requestName;
	}

	public String getRequestName()
	{
		return requestName;
	}
}