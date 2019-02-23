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

        holder.mName.setText(name);
     
        Picasso.get()
                .load(Uri.parse(stepList.get(position).getmThumbnailURL()))
                .placeholder(R.drawable.ic_leanpub)
                .error(R.drawable.ic_leanpub)
                .into(holder.mImageView);
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

    public interface RecipeStepClickListener {

        void onRecipeStepClicked(View view, int position);
    }

    public void setOnItemClickListener(RecipeStepClickListener listener) {
        this.listener = listener;
    }
}
