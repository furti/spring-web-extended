package io.github.furti.spring.web.extended.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class RequestResponseContextHolder
{

    private static ThreadLocal<Holder> holder = new ThreadLocal<>();

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
