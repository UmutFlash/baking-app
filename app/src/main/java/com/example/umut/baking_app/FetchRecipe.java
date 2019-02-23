package com.example.umut.baking_app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.umut.baking_app.model.Ingredient;
import com.example.umut.baking_app.model.Recipe;
import com.example.umut.baking_app.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchRecipe extends AsyncTask<String, Void, Recipe[]> {

    private final static String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private final String LOG_TAG = FetchRecipe.class.getSimpleName();


    private CallbackPostExecute mCallbackPostExecute;

    FetchRecipe(CallbackPostExecute callbackPostExecute) {
        super();
        this.mCallbackPostExecute = callbackPostExecute;
    }

    @Override
    protected Recipe[] doInBackground(String... params) {

        String reviewJsonString;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = getApiUrl(params);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            if (builder.length() == 0) {
                return null;
            }

            reviewJsonString = builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "closing reader", e);
                }
            }
        }

        try {
            return getReviewFromJson(reviewJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private Recipe[] getReviewFromJson(String reviewJsonString) throws JSONException {

        //JSONArray reviewJson = new JSONArray(reviewJsonString);
        //JSONArray resultsArray = reviewJson.getJSONArray("results");
        JSONArray resultsArray = new JSONArray(reviewJsonString);

        Recipe[] recipt = new Recipe[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {

            recipt[i] = new Recipe();
            JSONObject reciptData = resultsArray.getJSONObject(i);

            recipt[i].setmId(reciptData.getInt("id"));
            recipt[i].setmName(reciptData.getString("name"));

            JSONArray ingredients = resultsArray.getJSONObject(i)
                    .getJSONArray("ingredients");

            recipt[i].setmIngredientList(getIngredientFromJson(ingredients));

            JSONArray steps = resultsArray.getJSONObject(i)
                    .getJSONArray("steps");

            recipt[i].setmStepList(getStepFromJson(steps));
        }
        return recipt;
    }

    private ArrayList<Ingredient> getIngredientFromJson(JSONArray IngredientJsonArray) throws JSONException {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        for (int j = 0; j < IngredientJsonArray.length(); j++) {

            Ingredient ingredient = new Ingredient();
            JSONObject ingredientsObject = IngredientJsonArray.getJSONObject(j);

            ingredient.setmQuantity(ingredientsObject.getInt("quantity"));
            ingredient.setmMeasure(ingredientsObject.getString("measure"));
            ingredient.setmIngredient(ingredientsObject.getString("ingredient"));


            ingredientList.add(ingredient);
        }
        return ingredientList;
    }

    private ArrayList<Step> getStepFromJson(JSONArray stepJsonArray) throws JSONException {
        ArrayList<Step> StepList = new ArrayList<>();
        for (int j = 0; j < stepJsonArray.length(); j++) {

            Step step = new Step();
            JSONObject stepObject = stepJsonArray.getJSONObject(j);

            step.setmId(stepObject.getInt("id"));
            step.setmShortDescription(stepObject.getString("shortDescription"));
            step.setmDescription(stepObject.getString("description"));
            step.setmVideoURL(stepObject.getString("videoURL"));
            step.setmThumbnailURL(stepObject.getString("thumbnailURL"));


            StepList.add(step);
        }
        return StepList;
    }

    private URL getApiUrl(String[] parameters) throws MalformedURLException {

        Uri builtUri = Uri.parse(RECIPE_URL);

        return new URL(builtUri.toString());
    }

    @Override
    protected void onPostExecute(Recipe[] recipes) {
        super.onPostExecute(recipes);
        mCallbackPostExecute.onFetchRecipeTask(recipes);
    }

    interface CallbackPostExecute {
        void onFetchRecipeTask(Recipe[] recipes);
    }

}
