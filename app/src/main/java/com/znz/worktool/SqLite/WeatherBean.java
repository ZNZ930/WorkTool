package com.znz.worktool.SqLite;

public class WeatherBean {

    /**
     * status : 0
     * city : 上海市
     * aqi : 94
     * pm25 : 94
     * temp : 18 ~ 12℃
     * weather : 晴
     * wind : 东风微风
     * weatherimg : http://www.help.bj.cn/weather/bigicon/1.png
     * tomorrow : {"temp":"20 ~ 14℃","weather":"晴","wind":"东南风微风","weatherimg":"http://www.help.bj.cn/weather/icon/1.png"}
     */

    private String status;
    private String city;
    private String aqi;
    private String pm25;
    private String temp;
    private String weather;
    private String wind;
    private String weatherimg;
    private TomorrowBean tomorrow;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeatherimg() {
        return weatherimg;
    }

    public void setWeatherimg(String weatherimg) {
        this.weatherimg = weatherimg;
    }

    public TomorrowBean getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(TomorrowBean tomorrow) {
        this.tomorrow = tomorrow;
    }

    public static class TomorrowBean {
        /**
         * temp : 20 ~ 14℃
         * weather : 晴
         * wind : 东南风微风
         * weatherimg : https://www.help.bj.cn/weather/icon/1.png
         */


        private String temp;
        private String weather;
        private String wind;
        private String weatherimg;

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public String getWeatherimg() {
            return weatherimg;
        }

        public void setWeatherimg(String weatherimg) {
            this.weatherimg = weatherimg;
        }
    }
}
