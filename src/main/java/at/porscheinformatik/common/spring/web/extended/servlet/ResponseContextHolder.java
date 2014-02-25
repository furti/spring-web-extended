package at.porscheinformatik.common.spring.web.extended.servlet;

import javax.servlet.http.HttpServletResponse;

public final class ResponseContextHolder
{

	private static ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();

	private ResponseContextHolder()
	{

	}

	public static HttpServletResponse currentResponse()
	{
		return response.get();
	}

	protected static void setResponse(HttpServletResponse r)
	{
		response.set(r);
	}

	protected static void clear()
	{
		response.remove();
	}
}
