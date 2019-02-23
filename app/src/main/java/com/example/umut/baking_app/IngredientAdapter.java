package com.example.umut.baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.umut.baking_app.model.Recipe;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {


    private Recipe[] recipeList;
    private Context context;
    private RecipeClickListener listener;


    /**
     * RecipeRecyclerAdapter constructor
     *
     * @param context    Context
     * @param recipeList List of recipe retrieved
     */
    public IngredientAdapter(Context context, Recipe[] recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String name = recipeList[position].getmName();
        int servings = recipeList[position].getmStepList().size();

        holder.mName.setText(name);
        holder.mServings.setText(context.getString(R.string.servings, servings));

    }

    @Override
    public int getItemCount() {
        return recipeList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View mView;
        final TextView mName;
        final TextView mServings;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);
            mName = itemView.findViewById(R.id.tv_recipe_name);
            mServings = itemView.findViewById(R.id.tv_recipe_servings);
        }

        @Override
        public void onClick(View view) {
            listener.onRecipeClicked(view, getAdapterPosition());
        }
    }


    public interface RecipeClickListener {

        void onRecipeClicked(View view, int position);
    }

    public void setOnItemClickListener(RecipeClickListener listener) {
        this.listener = listener;
    }
}
