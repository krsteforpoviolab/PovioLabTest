package com.poviolab.poviolabtest.openweather;


public class OpenWeatherAPI {
    //this should be encrypted and placed elsewhere
    //with apk decompile this key can be easily read (not safe)
    public static final String MY_API_KEY="89ca5442648161217728548e937747d0";

    public static final String OPEN_WEATHER_URL="http://api.openweathermap.org/data/2.5/weather?APPID="+MY_API_KEY;

    public static String addCityNameToUrl(String url, String cityName){
        return url+"&q="+cityName;
    }


}
