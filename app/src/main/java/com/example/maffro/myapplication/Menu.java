package com.example.maffro.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static RecipeContainer recipeList = new RecipeContainer();
    public static String cleanInput;
    private List<Recipe> rcipes = null;
    static final int READ_BLOCK_SIZE = 100;
    private List<Recipe> tempRList = new ArrayList<>();

    Recipe recipeEdit;
    android.widget.Filter filter;

    ListView mListView;
    Button addRecipeButton;
    Button settingsButton;
    SearchView mSearchView;
    String jsonInput;
    RecipeAdapter arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //user interface setup
        mListView = (ListView)findViewById(R.id.listView);
        assert mListView != null;
        mListView.setTextFilterEnabled(true);
        mSearchView = (SearchView)findViewById(R.id.searchView);
        addRecipeButton = (Button)findViewById(R.id.addRecipeButton);
        settingsButton = (Button)findViewById(R.id.Settings);

        arrayAdapter = new RecipeAdapter(Menu.this, R.layout.list_items, rcipes);

        try {
            File myFile = new File("mytextfile.txt");
            String filename = "mytextfile.txt";
            if(!fileExistence(filename)){

                //read from assets
                //populate file from asset read
                FileOutputStream fOut;
                fOut = openFileOutput(filename,Context.MODE_PRIVATE);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                if (myFile.length() == 0) myOutWriter.append(readAssetsToPopulateFile());
                myOutWriter.close();
                fOut.close();

            }

            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            jsonInput = s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(rcipes == null)//checks if the list is empty(on startup)
                rcipes = parseJsonIn(jsonInput);
            //sets up the recipe adapter for the list view
            tempRList = rcipes;
            arrayAdapter = new RecipeAdapter(Menu.this,R.layout.list_items,tempRList);
            mListView.setAdapter(arrayAdapter);
            mListView.setTextFilterEnabled(true);
            filter = arrayAdapter.getFilter();
            setupSearchView();//searchview setup method
        }catch (Exception e) {
            e.printStackTrace();
        }

        //on list item select
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe;
                recipe = arrayAdapter.getItem(position);
                recipeEdit = arrayAdapter.getItem(position);
                Intent i0 = new Intent(Menu.this, EditRecipe.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recipe", recipe);//sends recipe to edit recipe
                i0.putExtras(bundle);
                startActivityForResult(i0, 0);
            }
        });

        //on add recipe button press
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, AddRecipe.class);
                startActivityForResult(i, 1);
            }
        });

        //on settings button press
        settingsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(Menu.this,settingsActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
           // arrayAdapter = new RecipeAdapter(Menu.this, R.layout.list_items, rcipes);
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }






    @Override
    public boolean onQueryTextSubmit(String newText) {//when search is entered - not used since its done on type update
        return false;
    }

    public  String readAssetsToPopulateFile(){//method to read assets and return a string
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager assetManager = getApplicationContext().getAssets();
            InputStream is = assetManager.open("jsonfiletest.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buff = new byte[bufferSize];

        int ln = 0;
        while ((ln = inputStream.read(buff)) != -1) {
            byteBuffer.write(buff, 0, ln);
        }
        return byteBuffer.toByteArray();
    } //get size of assets in bytes

    public boolean fileExistence(String filename){
        File file = getBaseContext().getFileStreamPath(filename);
        return file.exists();
    }

    public List<Recipe> parseJsonIn(String input){//method to parse json from a string into an object
        try {
        cleanInput = input.replace("\r", "");
        cleanInput = cleanInput.replace("\t","");
        cleanInput = cleanInput.replace("\n","");
        cleanInput = cleanInput.replace("\\","");
       // System.out.println("clean input = " + cleanInput);
        recipeList = new GsonBuilder().serializeNulls().create().fromJson(input, RecipeContainer.class);
        } catch(Exception e){
            e.printStackTrace();
        }
        return(recipeList.getRecipes());
    }

    public String parseJsonOut (RecipeContainer container){//method to parse json from an object into a string
        String output = "";
        try{
            output = new GsonBuilder().serializeNulls().create().toJson(container);
        }catch(Exception e){
            e.printStackTrace();
        }
        return output;
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 0://on return from edit recipe
                if (resultCode == Activity.RESULT_OK) {

                    Recipe recipe;
                    recipe = (Recipe) (data.getExtras().getSerializable("Recipe"));
                    String method = data.getStringExtra("method");
                    Boolean match = false;
                    List<Recipe> temp = rcipes;
                    for (Recipe item : rcipes) {
                        if (recipeEdit.getTitle().equals(item.getTitle())) {
                            temp.remove(item);
                            if(method.equals("EDIT"))//checks that the recipe should be updated and not deleted entirely
                                temp.add(recipe);
                            recipeList.setRecipes(temp);
                            writeRecipes(parseJsonOut(recipeList));
                            match = true;
                        }
                    }
                    if (!match) {
                        recipeList.addRecipe(recipe);
                        writeRecipes(parseJsonOut(recipeList));
                    }

                    try {
                        arrayAdapter = new RecipeAdapter(Menu.this, R.layout.list_items, rcipes);
                        if (recipeList == null) {
                            System.out.print("container empty");
                        }
                        if (rcipes == null) {
                            System.out.print("input empty");
                        }
                        mListView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 1://on return from add recipe
                if (resultCode == Activity.RESULT_OK) {
                    Recipe recipe;
                    recipe = (Recipe) (data.getExtras().getSerializable("Recipe"));
                    recipeList.addRecipe(recipe);
                    writeRecipes(parseJsonOut(recipeList));

                    try {
                        arrayAdapter = new RecipeAdapter(Menu.this, R.layout.list_items, rcipes);
                        if (recipeList == null) {
                            System.out.print("container empty");
                        }
                        if (rcipes == null) {
                            System.out.print("input empty");
                        }
                        mListView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void writeRecipes(String output){//method to write to file
        try {
            FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(output);
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
