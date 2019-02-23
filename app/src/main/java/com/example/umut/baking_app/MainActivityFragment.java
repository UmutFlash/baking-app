package com.example.umut.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umut.baking_app.model.Recipe;
import com.example.umut.baking_app.widget.NewAppWidget;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements FetchRecipe.CallbackPostExecute {


    private RecipeAdapter mRecipeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.recipe_recyclerView) RecyclerView mRecyclerView;

    public static final String RECIPE_OBJECT = "recipe_object";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable()) {
            getRecipe();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle(getString(R.string.network_error));
            alertDialog.setMessage(getString(R.string.no_network));
            alertDialog.show();
        }
    }

    private void getRecipe() {
        FetchRecipe task = new FetchRecipe(this);
        task.execute("");
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onFetchRecipeTask(final Recipe[] recipes) {
        mRecipeAdapter = new RecipeAdapter(getActivity(), recipes);
        if(getActivity().getResources().getBoolean(R.bool.isTablet)) {
            mLayoutManager = new GridLayoutManager(getContext(), 3,
                    LinearLayoutManager.VERTICAL, false);
        }else {
            mLayoutManager = new LinearLayoutManager(getActivity());

        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.setOnItemClickListener(new RecipeAdapter.RecipeCardClickListener() {
            @Override
            public void onRecipeCardClicked(View view, int position) {
                Intent intent = new Intent(getActivity(), MasterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(RECIPE_OBJECT, recipes[position]);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

            }
        });
    }
}
