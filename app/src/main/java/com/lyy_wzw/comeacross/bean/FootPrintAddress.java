package com.lyy_wzw.comeacross.bean;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yidong9 on 17/7/4.
 */

public class FootPrintAddress {
    private String formattedAddress;
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String streetNumber;

    public static FootPrintAddress jsonStrToFootPrintAddress(String jsonStr){
        FootPrintAddress ret = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            ret = new FootPrintAddress();
            ret.setFormattedAddress(jsonObject.optString("formatted_address", ""));
            JSONObject addressObj = jsonObject.getJSONObject("addressComponent");
            ret.setCountry(addressObj.optString("country",""));
            ret.setProvince(addressObj.optString("province",""));
            ret.setCity(addressObj.optString("city",""));
            ret.setDistrict(addressObj.optString("district",""));
            ret.setStreet(addressObj.optString("street",""));
            ret.setStreetNumber(addressObj.optString("street_number",""));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return ret;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }



    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Override
    public String toString() {
        return "FootPrintAddress{" +
                "formattedAddress='" + formattedAddress + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                '}';
    }
}
