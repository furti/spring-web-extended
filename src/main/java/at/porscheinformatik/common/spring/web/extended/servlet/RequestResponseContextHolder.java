package at.porscheinformatik.common.spring.web.extended.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class RequestResponseContextHolder
{

    private static ThreadLocal<Holder> holder = new ThreadLocal<Holder>();

    private RequestResponseContextHolder()
    {

    }

    public static HttpServletResponse currentResponse()
    {
        Holder h = holder.get();

        return h != null ? h.response : null;
    }

    protected static void setResponse(HttpServletResponse r)
    {
        if (holder.get() == null)
        {
            holder.set(new Holder());
        }

        holder.get().response = r;
    }

    public static HttpServletRequest currentRequest()
    {
        Holder h = holder.get();

        return h != null ? h.request : null;
    }

    protected static void setRequest(HttpServletRequest r)
    {
        if (holder.get() == null)
        {
            holder.set(new Holder());
        }

        holder.get().request = r;
    }

    protected static void clear()
    {
        holder.remove();
    }

    private static class Holder
    {
        private HttpServletResponse response;
        private HttpServletRequest request;
    }
}
