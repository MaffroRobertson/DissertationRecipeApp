package com.example.maffro.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maffro on 14/03/2016.
 */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private final List<Ingredient> orig;
    private List<Ingredient> items;
    int resource;
    String response;
    Context context;

    public IngredientAdapter(Context context, int resource, List<Ingredient> items){
        super(context,resource,items);
        this.resource = resource;
        this.items = items;
        orig = items;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LinearLayout IngredientView;
        Ingredient ing = getItem(position);

        if(convertView == null){
            IngredientView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource,IngredientView,true);
        }
        else{
            IngredientView = (LinearLayout) convertView;
        }
        TextView IngredientName = (TextView)IngredientView.findViewById(R.id.ListViewTitle);
        TextView IngredientAmount = (TextView)IngredientView.findViewById(R.id.ListViewYield);

        String ingredientData = String.format("%d %s", ing.getQuantity(), ing.getMeasure());

        IngredientName.setText(ing.getFood());
        IngredientAmount.setText(ingredientData);


        return IngredientView;
    }
}
