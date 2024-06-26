package com.example.maffro.myapplication;

/**
 * Created by Maffro on 14/03/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Ingredient implements Serializable{

    @SerializedName("food")
    @Expose
    private String food;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("measure")
    @Expose
    private String measure;


    /**
     *
     * @return
     * The Food
     */
    public String getFood() {
        return food;
    }

    /**
     *
     * @param food
     * The Food
     */
    public void setFood(String food) {
        this.food = food;
    }
    /**
     *
     * @return
     * The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     * The quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     * The measure
     */
    public String getMeasure() {
        return measure;
    }

    /**
     *
     * @param measure
     * The measure
     */
    public void setMeasure(String measure) {
        this.measure = measure;
    }

}
