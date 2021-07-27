package com.siemens.ct.task.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.siemens.ct.task.exception.GeneralException;
import com.siemens.ct.task.util.RetryUtil;
import com.siemens.ct.task.util.Util;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl {

    private static final String PROVINCE_URL = "http://www.weather.com.cn/data/city3jdata/china.html";
    private static final String CITY_URL = "http://www.weather.com.cn/data/city3jdata/provshi/";
    private static final String COUNTY_URL = "http://www.weather.com.cn/data/city3jdata/station/";
    private static final String TEMPERATURE_URL = "http://www.weather.com.cn/data/sk/";

    /**
     * 获取温度
     * @param province
     * @param city
     * @param county
     */
    public String getTemperature(String province, String city, String county) {

        if("".equals(province)){
            throw new GeneralException("province is null !", 10000);
        }

        if("".equals(city)){
            throw new GeneralException("city is null !", 10000);
        }

        if("".equals(county)){
            throw new GeneralException("county is null !", 10000);
        }
        // get province code
        String provinceCode = getProvinceCode(province);
        // get city code
        String cityCode = getCityCode(provinceCode, city);
        // get county code
        String countyCode = getCountyCode(provinceCode, cityCode, county);
        // get temperature json
        String temperatureJson = getTemperatureJson(provinceCode, cityCode, countyCode);
        return getTemperatureValue(temperatureJson);
    }

    /**
     * get temperature
     * @param TemperatureJson
     */
    public String getTemperatureValue(String TemperatureJson) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(TemperatureJson);
        return jsonElement.getAsJsonObject().get("weatherinfo").getAsJsonObject().get("temp").toString()
                .replace("\"","");
    }

    /**
     * get province code
     * @param province
     */
    public String getProvinceCode(String province){
        String provinceJson = null;
        try {
            provinceJson = Util.getJson(PROVINCE_URL);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (provinceJson == null){
            throw new GeneralException("parse province error", 10000);
        }
        String provinceCode = Util.getCode(provinceJson, province);
        if(provinceCode == null){
            throw new GeneralException("invalid province", 10000);
        }
        return provinceCode;
    }

    /**
     * get city code
     * @param provinceCode
     * @param city
     */
    public String getCityCode(String provinceCode, String city){
        String cityJson = null;
        try {
            cityJson = Util.getJson(CITY_URL+ provinceCode + ".html");
        } catch (Exception e){
            e.printStackTrace();
        }
        if (cityJson == null){
            throw new GeneralException("parse city error", 10000);
        }
        String cityCode = Util.getCode(cityJson, city);
        if(cityCode == null){
            throw new GeneralException("invalid city", 10000);
        }
        return cityCode;
    }

    /**
     * get city code
     * @param provinceCode
     * @param cityCode
     * @param county
     */
    public String getCountyCode(String provinceCode, String cityCode, String county){
        String countyJson = null;
        try {
            countyJson = Util.getJson(COUNTY_URL+ provinceCode + cityCode + ".html");
        } catch (Exception e){
            e.printStackTrace();
        }
        if (countyJson == null){
            throw new GeneralException("parse county error", 10000);
        }
        String countyCode = Util.getCode(countyJson, county);
        if(countyCode == null){
            throw new GeneralException("invalid county", 10000);
        }
        return countyCode;
    }

    /**
     * get temperature json and retry only for the weather API
     * @param provinceCode
     * @param cityCode
     * @param countyCode
     */
    public String getTemperatureJson(String provinceCode, String cityCode, String countyCode){
        String temperatureJson;
        try {
            temperatureJson = Util.getJson(TEMPERATURE_URL + provinceCode + cityCode + countyCode + ".html");
        }catch (Exception e){
            e.printStackTrace();
            // retry
            temperatureJson = RetryUtil.setRetryTimes(10).retry(provinceCode, cityCode, countyCode).toString();
        }
        if (temperatureJson == null){
            throw new GeneralException("parse temperature error", 10000);
        }
        return temperatureJson;
    }
}
