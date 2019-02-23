package com.example.umut.baking_app.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Recipe implements Parcelable {

    public Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredientList = in.createTypedArrayList(Ingredient.CREATOR);
        mStepList = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe() {

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ArrayList<Ingredient> getmIngredientList() {
        return mIngredientList;
    }

    public void setmIngredientList(ArrayList<Ingredient> mIngredientList) {
        this.mIngredientList = mIngredientList;
    }

    public ArrayList<Step> getmStepList() {
        return mStepList;
    }

    public void setmStepList(ArrayList<Step> mStepList) {
        this.mStepList = mStepList;
    }

    private int mId;
    private String mName;
    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeTypedList(mIngredientList);
        parcel.writeTypedList(mStepList);
    }
}
