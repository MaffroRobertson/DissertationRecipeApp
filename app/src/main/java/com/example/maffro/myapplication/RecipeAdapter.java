package com.example.maffro.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private List<Recipe> orig;
    private List<Recipe> adapterRecipeList;
    RecipeContainer container = new RecipeContainer();
    int resource;
    Context context;


    public RecipeAdapter(Context context, int resource, List<Recipe> RecipeList){
        super(context,resource, RecipeList);
        this.resource=resource;
        this.context = context;
        this.adapterRecipeList = RecipeList;
        orig = container.getRecipes();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LinearLayout RecipeView;
        Recipe re = getItem(position);

        if(convertView == null){
            RecipeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource,RecipeView,true);
        }
        else{
            RecipeView = (LinearLayout) convertView;
        }
        TextView RecipeTitle = (TextView)RecipeView.findViewById(R.id.ListViewTitle);
        TextView RecipeYield = (TextView)RecipeView.findViewById(R.id.ListViewYield);
        ImageView RecipeImage = (ImageView)RecipeView.findViewById(R.id.imageView);

        RecipeTitle.setText(re.getTitle());
        if(re.getYield() == 1){
            RecipeYield.setText("Serves " + Integer.toString(re.getYield()) + " person");
        }else {
            RecipeYield.setText("Serves " + Integer.toString(re.getYield()) + " people");
        }

        ContextWrapper cw = new ContextWrapper(context);

        //RecipeImage.
        File directory = cw.getDir("imageDir",Context.MODE_PRIVATE);
        String path = directory.getAbsolutePath();
        loadImageFromStorage(path,RecipeImage,re);
        return RecipeView;
    }
    public Filter getFilter() {
        final Filter filter =  new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                final ArrayList<Recipe> results = new ArrayList<>();
                if (constraint != null) {
                    if(orig == null)
                        orig = adapterRecipeList;
                    if (orig != null && orig.size() > 0) {
                        for (final Recipe g : orig) {
                            if (g.getTitle().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(constraint != null){
                    clear();
                    for (Recipe item : (List<Recipe>) results.values) {
                        add(item);
                    }
                    RecipeAdapter.this.notifyDataSetChanged();
                }
                else{
                    clear();
                    for(Recipe item : orig){
                        add(item);
                    }
                }
            }
        };
        return filter;
    }
    private void loadImageFromStorage(String path, ImageView image, Recipe recipe)
    {

        try {
            File f=new File(path, recipe.getTitle()+ ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            image.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            image.setImageResource(R.drawable.no_image_available);
            //e.printStackTrace();
        }

    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        // notifyChanged = true;
    }



}
