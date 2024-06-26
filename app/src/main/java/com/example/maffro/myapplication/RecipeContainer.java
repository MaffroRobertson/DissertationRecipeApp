package com.example.maffro.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maffro on 15/03/2016.
 */
public  class RecipeContainer {

    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes = new ArrayList<>();

    /**
     *
     * @return
     * The recipes
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     *
     * @param recipes
     * The recipes
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

}
