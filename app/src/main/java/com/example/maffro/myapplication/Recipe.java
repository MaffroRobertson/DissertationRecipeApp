package com.example.maffro.myapplication;

/**
 * Created by Maffro on 14/03/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    @SerializedName("title")
    @Expose
    private String Title;
    @SerializedName("prep")
    @Expose
    private List<String> prep;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("yield")
    @Expose
    private int yield;
    @SerializedName("calories")
    @Expose
    private Float calories;
    @SerializedName("Ingredients")
    @Expose
    private List<Ingredient> Ingredients = new ArrayList<>();

    /**
     *
     * @return
     * The Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     *
     * @param Title
     * The Title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     *
     * @return
     * The prep
     */
    public List<String> getPrep() {
        return prep;
    }

    /**
     *
     * @param prep
     * The prep
     */
    public void setPrep(List<String> prep) {
        this.prep = prep;
    }

    public void addPrep(String prep){
        this.prep.add(prep);
    }
    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The yield
     */
    public int getYield() {
        return yield;
    }

    /**
     *
     * @param yield
     * The yield
     */
    public void setYield(int yield) {
        this.yield = yield;
    }

    /**
     *
     * @return
     * The Calories
     */
    public Float getCalories() {
        return calories;
    }

    /**
     *
     * @param calories
     * The Calories
     */
    public void setCalories(Float calories) {
        this.calories = calories;
    }

    /**
     *
     * @return
     * The Ingredients
     */
    public List<Ingredient> getIngredients() {
        return Ingredients;
    }

    /**
     *
     * @param Ingredients
     * The Ingredients
     */
    public void setIngredients(List<Ingredient> Ingredients) {
        this.Ingredients = Ingredients;
    }

    public void addIngredients(Ingredient ingredient){
        this.Ingredients.add(ingredient);
    }



    public void writeRecipe()
    {
        System.out.print("\n");
        System.out.println("Title = " + getTitle());
        System.out.println("Yield = " + getYield());
        if(getCalories() != 0)
        {
            System.out.println("Calories = " + getCalories());
        }
        if(getUrl() != null)
        {
            System.out.println("Url = " + getUrl());
        }
        System.out.println("Ingredients: ");


		/*for(int i = 0; i < getIngredients().iterator();i++)
		{
			System.out.println("Food = " + getIngredients(i).getFood());
		}*/
        for(Ingredient item : getIngredients())
        {
            System.out.print("\n");
            System.out.println("Food = " + item.getFood());
            if(item.getMeasure()!= null && item.getQuantity() != 0)
            {
                System.out.println("amount = " + item.getQuantity() + item.getMeasure());
            }
        }
    }

}

