package com.example.maffro.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AddRecipe extends AppCompatActivity {
    public static Recipe IngredientList = new Recipe();
    IngredientAdapter arrayAdapter;
    ArrayAdapter<String> adapter;

    Recipe recipe = new Recipe();
    private Button Addingredient;
    private Button addPrep;
    private Button saveButton;
    private Button homeButton;
    EditText titleEdit;
    EditText prepEdit;
    EditText yieldEdit;

    EditText nameEdit;
    EditText quantityEdit;
    EditText measureEdit;

    ListView ingredientListView;
    ListView prepListView;

    String title;
    int yield;
    List<Ingredient> ingrs = null;
    List<String> prep = new ArrayList<>();

    String name;
    int quantity;
    String measure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //tabhost setup
        TabHost th = (TabHost)findViewById(R.id.tabHost);
        th.setup();
        TabHost.TabSpec specs;

        specs = th.newTabSpec("tag1");
        specs.setContent(R.id.tab1);
        specs.setIndicator("Recipe Info");
        th.addTab(specs);

        specs = th.newTabSpec("tag2");
        specs.setContent(R.id.tab2);
        specs.setIndicator("Ingredient Info");
        th.addTab(specs);

        specs = th.newTabSpec("tag3");
        specs.setContent(R.id.tab3);
        specs.setIndicator("Cooking Steps");
        th.addTab(specs);


        //setup UI
        Addingredient = (Button) findViewById(R.id.button2);
        addPrep = (Button)findViewById(R.id.addPrepButton);
        saveButton = (Button) findViewById(R.id.saveRecipeButton);
        homeButton = (Button)findViewById(R.id.homeToMenu);

        ingredientListView = (ListView) findViewById(R.id.ingredientListView);
        //ingredientListView.setTextFilterEnabled(true);
        prepListView = (ListView)findViewById(R.id.prepListView);
        //prepListView.setTextFilterEnabled(true);
        adapter = new ArrayAdapter<>(AddRecipe.this,android.R.layout.simple_list_item_1,prep);

        titleEdit = (EditText)findViewById(R.id.titleText);
        yieldEdit = (EditText)findViewById(R.id.yieldText);


        Addingredient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(AddRecipe.this);
                LayoutInflater inflater = AddRecipe.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_ingredient_item, null);
                dialogbuilder.setView(dialogView);


                final TextView addName = (TextView)dialogView.findViewById(R.id.DialogAddNameText);
                addName.setText("");
                final TextView addQuantity = (TextView)dialogView.findViewById(R.id.DialogAddQuantityText);

                final TextView addMeasure = (TextView)dialogView.findViewById(R.id.DialogAddMeasureText);
                addMeasure.setText("");

                dialogbuilder.setPositiveButton("Save",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Ingredient ingredient = new Ingredient();

                        if(addName.getText().toString() != "") {
                            final String tempNameText = addName.getText().toString();
                            ingredient.setFood(tempNameText);
                        }else{
                            Toast.makeText(getApplicationContext(),"fill in the name field (type of food)",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final String tempQuantityText = addQuantity.getText().toString();
                        if(tempQuantityText.length() != 0) {
                            final int tempQuantityNumber = Integer.parseInt(tempQuantityText);
                            ingredient.setQuantity(tempQuantityNumber);
                        }else{
                            Toast.makeText(getApplicationContext(),"fill in the quantity field (number of measures)",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(addMeasure.getText().toString() != "") {
                            final String tempMeasureText = addMeasure.getText().toString();
                            ingredient.setMeasure(tempMeasureText);
                        }else{
                            Toast.makeText(getApplicationContext(),"fill in the measure field (measure of quantity",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        IngredientList.addIngredients(ingredient);
                        ingrs = IngredientList.getIngredients();

                        arrayAdapter = new IngredientAdapter(AddRecipe.this, R.layout.list_items, ingrs);
                        ingredientListView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialogbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = dialogbuilder.create();
                alertDialog.show();
            }
        });
        addPrep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(AddRecipe.this);
                LayoutInflater inflater = AddRecipe.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_prep_item,null);
                dialogbuilder.setView(dialogView);


                final TextView addPrep = (TextView)dialogView.findViewById(R.id.DialogAddStepText);
                addPrep.setText("");

                dialogbuilder.setPositiveButton("Save",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String tempText = addPrep.getText().toString();
                        prep.add(tempText);
                        prepListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialogbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });



                AlertDialog alertDialog = dialogbuilder.create();
                alertDialog.show();


            }
        });
        prepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String tempPrep = adapter.getItem(position);


                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(AddRecipe.this);
                LayoutInflater inflater = AddRecipe.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_prep_item, null);
                dialogbuilder.setView(dialogView);


                final TextView addPrep = (TextView)dialogView.findViewById(R.id.DialogAddStepText);
                addPrep.setText(tempPrep);

                dialogbuilder.setPositiveButton("Save",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String tempText = addPrep.getText().toString();

                        updatePrep(tempPrep,tempText);
                        prepListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialogbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogbuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //send the info for the ingr to be deleted;
                        prep.remove(tempPrep);
                        if(adapter == null)
                            adapter = new ArrayAdapter<>(AddRecipe.this,android.R.layout.simple_list_item_1,prep);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Cooking step removed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = dialogbuilder.create();
                alertDialog.show();
            }
        });
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Ingredient tempIngredient = arrayAdapter.getItem(position);

                final String tempName = tempIngredient.getFood();
                final int tempQuantity = tempIngredient.getQuantity();
                final String tempMeasure = tempIngredient.getMeasure();

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(AddRecipe.this);
                LayoutInflater inflater = AddRecipe.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_ingredient_item, null);
                dialogbuilder.setView(dialogView);

                final TextView addName = (TextView)dialogView.findViewById(R.id.DialogAddNameText);
                addName.setText(tempName);
                final TextView addQuantity = (TextView)dialogView.findViewById(R.id.DialogAddQuantityText);
                addQuantity.setText("" +tempQuantity);
                final TextView addMeasure = (TextView)dialogView.findViewById(R.id.DialogAddMeasureText);
                addMeasure.setText(tempMeasure);


                dialogbuilder.setPositiveButton("Save",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Ingredient ingredient = new Ingredient();

                        if(addName.getText().toString() != "") {
                            final String tempNameText = addName.getText().toString();
                            ingredient.setFood(tempNameText);
                        }
                        if(addQuantity.getText().toString() != "") {
                            final String tempQuantityText = addQuantity.getText().toString();
                            final int tempQuantityNumber = Integer.parseInt(tempQuantityText);
                            ingredient.setQuantity(tempQuantityNumber);
                        }
                        if(addMeasure.getText().toString() != "") {
                            final String tempMeasureText = addMeasure.getText().toString();
                            ingredient.setMeasure(tempMeasureText);
                        }

                                //IngredientList.addIngredients(ingredient);

                        updateIngredient(tempIngredient,ingredient);


                        if(arrayAdapter == null)
                            arrayAdapter = new IngredientAdapter(AddRecipe.this, R.layout.list_items, ingrs);
                        ingredientListView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialogbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogbuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //send the info for the ingr to be deleted;
                        ingrs.remove(tempIngredient);
                        if(arrayAdapter == null)
                            arrayAdapter = new IngredientAdapter(AddRecipe.this, R.layout.list_items, ingrs);
                        ingredientListView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Ingredient removed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });






                AlertDialog alertDialog = dialogbuilder.create();
                alertDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(titleEdit.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Please enter a title",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    title = titleEdit.getText().toString();
                    recipe.setTitle(title);
                }

                if(yieldEdit.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "Please enter yield (# of servings)",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    yield = Integer.parseInt(yieldEdit.getText().toString());
                    recipe.setYield(yield);
                }

                if(prep.size() == 0){
                    Toast.makeText(getBaseContext(), "Please enter 1 or more cooking step/s",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    recipe.setPrep(prep);
                }
                if(ingrs != null){
                    if(ingrs.size() == 0){
                        Toast.makeText(getBaseContext(), "Please enter 1 or more ingredient/s",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        recipe.setIngredients(ingrs);
                    }
                }
                else{
                    Toast.makeText(getBaseContext(), "Please enter 1 or more ingredient/s",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = getIntent();
                resultIntent.putExtra("Recipe", recipe);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                Intent i = new Intent(AddRecipe.this,Menu.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
    public void updateIngredient(Ingredient ingredient1, Ingredient ingredient2){
        ingrs.remove(ingredient1);
        ingrs.add(ingredient2);
    }
    public void updatePrep(String prep1, String prep2){
        prep.remove(prep1);
        prep.add(prep2);
    }
}
