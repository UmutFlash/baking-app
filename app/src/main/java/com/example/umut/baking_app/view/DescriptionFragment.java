package com.example.umut.baking_app.view;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.umut.baking_app.MasterActivity;
import com.example.umut.baking_app.R;
import com.example.umut.baking_app.StepAdapter;
import com.example.umut.baking_app.model.Ingredient;
import com.example.umut.baking_app.model.Step;
import com.example.umut.baking_app.widget.NewAppWidget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DescriptionFragment extends Fragment {

    private static final String LOG_TAG = DescriptionFragment.class.getSimpleName();
    @BindView(R.id.rv_description) RecyclerView mRecyclerView;
    @BindView(R.id.tv_ingredients) TextView mIngredients;

    public ArrayList<Step> mStep;
    RecyclerView.LayoutManager mStepLayoutManager;
    OnFragmentInteractionListener mListener;

    //private OnFragmentInteractionListener mListener;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    public static DescriptionFragment newInstance(String param1, String param2) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_description, container, false);
        ButterKnife.bind(this, rootView);

        mStepLayoutManager = new LinearLayoutManager(getContext());

        mStep = ((MasterActivity) getActivity()).getmRecipeStepList();

        ArrayList<Ingredient> ingredients = ((MasterActivity) getActivity()).getmIngredientList();
        ((MasterActivity) getActivity()).getmRecipeStepList();

        String recipeName = ((MasterActivity) getActivity()).getmRecipeName();

        mIngredients.setText(getIngredientsText(ingredients));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), NewAppWidget.class));
        NewAppWidget.onUpdateNewAppWidget(getContext(), appWidgetManager, getIngredientsText(ingredients), appWidgetIds);

        StepAdapter mStepAdapter = new StepAdapter(getActivity(), mStep);
        mRecyclerView.setAdapter(mStepAdapter);
        mRecyclerView.setLayoutManager(mStepLayoutManager);

        mStepAdapter.setOnItemClickListener(new StepAdapter.RecipeStepClickListener() {
            @Override
            public void onRecipeStepClicked(View view, int position) {
                mListener.onFragmentInteraction(position);
            }
        });

        return rootView;

    }

    public String getIngredientsText(ArrayList<Ingredient> ingredients) {
        String ingredientsText = "";
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient appendIngredient = ingredients.get(i);
            ingredientsText +=
                    "\n " + appendIngredient.getmIngredient() +
                            " - " + appendIngredient.getmQuantity() + " "
                            + appendIngredient.getmMeasure();
        }
        return ingredientsText;
    }


    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position);
    }
}
