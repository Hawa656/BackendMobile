package com.gardenbk.mygarden.sec.web;

public class JwtUtil {
    public static final String SECRET="mySecret1234";
    public static final String Auth_Header="Authorization";
    public static final String PREFIX="Bearer ";
    public static final long EXPIRE_ACCESS_TOKEN=2*60*1000;
    public static final long EXPIRE_REFRESH_TOKEN=5*60*1000;
}
