package com.bwcoolweather.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.bwcoolweather.android.db.City;
import com.bwcoolweather.android.db.Country;
import com.bwcoolweather.android.db.Province;
import com.bwcoolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/3/8.
 */

public class Utilty {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject =allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
           try{
               JSONArray allCitys=new JSONArray(response);
               for(int i=0;i<allCitys.length();i++){
                   JSONObject cityObject=allCitys.getJSONObject(i);
                   City city=new City();
                   city.setCityName(cityObject.getString("name"));
                   city.setCityCode(cityObject.getInt("id"));
                   city.setProvinceId(provinceId);
                   city.save();
               }
               return true;
           }catch(JSONException e){
               e.printStackTrace();
           }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCountrys=new JSONArray(response);
                for (int i=0;i<allCountrys.length();i++){
                    JSONObject countryObject=allCountrys.getJSONObject(i);
                    Country country=new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContext=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContext,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
