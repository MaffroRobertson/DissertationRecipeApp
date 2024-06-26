package com.example.maffro.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

public class Summary extends AppCompatActivity {

    private TextView titleText;
    private TextView yieldText;

    Button editButton;
    Button cookButton;
    Button homeButton;
    Button deleteButton;
    ListView ingredientListView;
    ListView prepListView;

    IngredientAdapter arrayAdapter;
    Recipe recipe;
    List<Ingredient> ingredients;
    List<String> prepSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //setup TabHost
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
        editButton = (Button) findViewById(R.id.editButton);
        cookButton = (Button)findViewById(R.id.cookButton);
        homeButton = (Button) findViewById(R.id.homeButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        titleText = (TextView) findViewById(R.id.titleText);
        yieldText = (TextView) findViewById(R.id.yieldText);
        ingredientListView = (ListView) findViewById(R.id.ingredientListView);
        prepListView = (ListView)findViewById(R.id.prepListView);




        Intent i = getIntent();
        if(i != null){
            i.getExtras();
            recipe = (Recipe) i.getSerializableExtra("recipe");

            //set fields to recipe contents
            titleText.setText(recipe.getTitle());
            titleText.setKeyListener(null);
            String yield = (String.format("%,d",recipe.getYield()).replace(",",""));
            yieldText.setText(yield);
            yieldText.setKeyListener(null);
            ingredients = recipe.getIngredients();

            try {


                //setup listview with IngredientAdapter
                if(ingredients != null) {
                    arrayAdapter = new IngredientAdapter(Summary.this,R.layout.list_items,ingredients);
                    ingredientListView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }else{
                    System.out.print("container empty");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                prepSteps = recipe.getPrep();
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(Summary.this,android.R.layout.simple_list_item_1,prepSteps);
                prepListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                Intent i = new Intent(Summary.this,Menu.class);
                startActivity(i);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent showC = new Intent(Summary.this,EditRecipe.class);
                showC.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recipe", recipe);
                showC.putExtras(bundle);
                startActivity(showC);
                finish();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                Intent i = new Intent(Summary.this,Menu.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
