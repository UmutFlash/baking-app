package com.example.umut.baking_app;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.umut.baking_app.model.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {


    private Recipe[] recipeList;
    private Context context;
    private RecipeCardClickListener listener;


    /**
     * RecipeRecyclerAdapter constructor
     *
     * @param context    Context
     * @param recipeList List of recipe retrieved
     */
    public RecipeAdapter(Context context, Recipe[] recipeList) {
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
        //String recipeImage = recipeList[position].getmId();
        int servings = recipeList[position].getmStepList().size();
        //int recipeId = recipeList[position].getmId();

        holder.mName.setText(name);
        holder.mServings.setText(context.getString(R.string.servings, servings));

        Picasso.get().load(R.drawable.ic_cake)
                .placeholder(R.drawable.ic_cake)
                .error(R.drawable.ic_cake).into(holder.mCakeImage);


    /*
        if (Utility.recipeExist(context, recipeId)) {
            holder.frameLayout.setVisibility(View.VISIBLE);
        } else {
            holder.frameLayout.setVisibility(View.INVISIBLE);
        }
         */
    }

    @Override
    public int getItemCount() {
        return recipeList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        @BindView(R.id.tv_recipe_name) TextView mName;
        @BindView(R.id.tv_recipe_servings) TextView mServings;
        //@BindView(R.id.fl_recipe) FrameLayout mFrameLayout;
        @BindView(R.id.iv_recipe)
        ImageView mCakeImage;




        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View view) {
            listener.onRecipeCardClicked(view, getAdapterPosition());
        }
    }


    public interface RecipeCardClickListener {

        void onRecipeCardClicked(View view, int position);
    }

    public void setOnItemClickListener(RecipeCardClickListener listener) {
        this.listener = listener;
    }
}
