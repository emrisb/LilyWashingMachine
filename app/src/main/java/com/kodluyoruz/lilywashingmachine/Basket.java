package com.kodluyoruz.lilywashingmachine;

/**
 * Created by emre on 8/16/2017.
 */

public class Basket {
    //private String basket;
    private int distance, humidity, temperature;

    public Basket() {
    }

    public Basket(int distance, int humidity, int temperature) {

        //this.basket = basket;
        this.distance = distance;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    /*public String getBasket() {

        return basket;
    }*/

    /*public void setBasket(String basket) {
        this.basket = basket;
    }*/

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
