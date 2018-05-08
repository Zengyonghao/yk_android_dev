package com.zplh.zplh_android_yk.httpcallback;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Fan on 2017/3/9.
 * post带参数的请求
 */

public class GosnPostRequest<T> extends Request<T> {
    private Gson mGson;
    private Response.Listener<T> mListener;
    private Class<T> mClass;
    private Map<String,String> mMap;

    public GosnPostRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    public GosnPostRequest(String url, Class<T> tClass, Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String,String> map) {
        super(Method.POST,url,errorListener);
        mGson=new Gson();
        mListener=listener;
        mClass=tClass;
        mMap=map;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String gsonString=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(mGson.fromJson(gsonString,mClass),HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(T t) {
        mListener.onResponse(t);

    }
}
