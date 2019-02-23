package com.example.umut.baking_app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.umut.baking_app.model.Ingredient;
import com.example.umut.baking_app.model.Recipe;
import com.example.umut.baking_app.model.Step;
import com.example.umut.baking_app.view.DescriptionFragment;
import com.example.umut.baking_app.view.InstructionsFragment;

import java.util.ArrayList;

public class MasterActivity extends AppCompatActivity implements DescriptionFragment.OnFragmentInteractionListener {


    private static final String POSITION = "position";

    public String getmRecipeName() {
        return mRecipeName;
    }

    private String mRecipeName;

    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mRecipeStepList;
    private int mStepsIndex = 0;
    private boolean isTablet = false;

    //private int mCurrentPosition;

    private DescriptionFragment mDescriptionFragment;
    private InstructionsFragment mInstructionsFragment;
    private String mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDescriptionFragment = new DescriptionFragment();
        mInstructionsFragment = new InstructionsFragment();
        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (getIntent().hasExtra(MainActivityFragment.RECIPE_OBJECT)) {
            Recipe recipe = getIntent().getParcelableExtra(MainActivityFragment.RECIPE_OBJECT);
            mRecipeName = recipe.getmName();
            mIngredientList = recipe.getmIngredientList();
            mRecipeStepList = recipe.getmStepList();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.recipe_fragment_container, mDescriptionFragment)
                    .commit();
        }
        getSupportActionBar().setTitle(mRecipeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activty_master);
    }

    public ArrayList<Ingredient> getmIngredientList() {
        return mIngredientList;
    }

    public ArrayList<Step> getmRecipeStepList() {
        return mRecipeStepList;
    }

    @Override
    public void onFragmentInteraction(int position) {
        mStepsIndex = position;
        if (isTablet) {
            if (mInstructionsFragment != null && mInstructionsFragment.isVisible()) {
                mInstructionsFragment.onPositionChanged(position);
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.instruction_fragment_container, mInstructionsFragment)
                        .commit();
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.recipe_fragment_container, mInstructionsFragment).commit();
            mInstructionsFragment.setPosition(position);
        }
    }

    public int getmStepsIndex() {
        return mStepsIndex;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mStepsIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mStepsIndex = savedInstanceState.getInt(POSITION);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
        return true;
    }
}
