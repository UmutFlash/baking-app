package com.example.umut.baking_app;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.umut.baking_app.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {


    private ArrayList<Step> stepList;
    private Context context;
    private RecipeStepClickListener listener;


    public StepAdapter(Context context, ArrayList<Step> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String name = stepList.get(position).getmShortDescription();
        //String recipeImage = recipeList[position].getmId();
        //int servings = stepList[position].getmStepList().size();
        //int recipeId = recipeList[position].getmId();

        holder.mName.setText(name);
        //holder.mServings.setText(context.getString(R.string.servings, servings));

        Picasso.get()
                .load(Uri.parse(stepList.get(position).getmThumbnailURL()))
                .placeholder(R.drawable.ic_leanpub)
                .error(R.drawable.ic_leanpub)
                .into(holder.mImageView);

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
        return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View mView;
        @BindView(R.id.tv_step_name)TextView mName;
        @BindView(R.id.imageView)ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);

        }

        @Override
        public void onClick(View view) {
            listener.onRecipeStepClicked(view, getAdapterPosition());
        }
    }

    /**
     * Interface for when recipe in recycler view is clicked
     */
    public interface RecipeStepClickListener {

        /**
         * Method for when recipe is clicked
         *
         * @param view     View of ViewHolder
         * @param position Position of item clicked in recycler view
         */
        void onRecipeStepClicked(View view, int position);
    }

    /**
     * Custom click listener
     */
    public void setOnItemClickListener(RecipeStepClickListener listener) {
        this.listener = listener;
    }
}
