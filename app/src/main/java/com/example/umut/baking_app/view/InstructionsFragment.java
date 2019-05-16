package com.example.umut.baking_app.view;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.umut.baking_app.MasterActivity;
import com.example.umut.baking_app.R;
import com.example.umut.baking_app.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InstructionsFragment extends Fragment {
    private static final String STEPS = "stepList";
    private static final String POSITION = "position";
    private static final String FULLSCREEN = "fullscreen";
    private static final String PLAY_POSITION = "play_position";
    private static final String PLAY_STATE = "play_state";

    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.pv_video)
    PlayerView mPlayerView;
    @BindView(R.id.tv_instruction_text)
    TextView mInstruction;
    @BindView(R.id.bt_next)
    ImageButton mNextBtn;
    @BindView(R.id.bt_back)
    ImageButton mBackBtn;

    public ArrayList<Step> mStepList;
    public Step mStep;
    private int mStepPosition = 0;

    private long mPlayerPosition;
    private boolean mPlayState;
    private Unbinder mUnbinder;


    public InstructionsFragment() {
        // Required empty public constructor
    }


    public static InstructionsFragment newInstance(ArrayList<Step> sList, int position) {
        InstructionsFragment fragment = new InstructionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mStepList = ((MasterActivity) getActivity()).getmRecipeStepList();
        mStep = mStepList.get(((MasterActivity) getActivity()).getmStepsIndex());

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepPosition < mStepList.size() - 1) {
                    mStepPosition++;
                    onPositionChanged(mStepPosition);
                }
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStepPosition > 0) {
                    mStepPosition--;
                    onPositionChanged(mStepPosition);
                }
            }
        });

        mInstruction.setText(mStep.getmDescription());
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mStepPosition);
        outState.putBoolean(FULLSCREEN, isFullScreen());
        if (mExoPlayer != null) {
            outState.putLong(PLAY_POSITION, mExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAY_STATE, mExoPlayer.getPlayWhenReady());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mStepPosition = savedInstanceState.getInt(POSITION);
            mPlayerPosition = savedInstanceState.getLong(PLAY_POSITION);
            mPlayState = savedInstanceState.
                    getBoolean(PLAY_STATE);
           updateView();
        }
    }

    public Boolean isFullScreen() {
        Configuration newConfig = new Configuration();
        return newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
            initializePlayer(mStepList.get(mStepPosition).getmVideoURL());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayState = mExoPlayer.getPlayWhenReady();
            releaseExoPlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
        } mExoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseExoPlayer();
        mUnbinder.unbind();
    }

    public void onPositionChanged(int position) {
        resetPlayerState();
        mExoPlayer = null;
        updateView();
    }

    private void updateView() {
        mInstruction.setText(mStepList.get(mStepPosition).getmDescription());
        releaseExoPlayer();
        initializePlayer(mStepList.get(mStepPosition).getmVideoURL());
    }


    public void setPosition(int position) {
        mStepPosition = position;
    }

    public void initializePlayer(String videoUrl) {
        if (isNetworkAvailable()) {
            if (TextUtils.isEmpty(videoUrl)) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                if (mExoPlayer == null)
                    mPlayerView.setVisibility(View.VISIBLE);
                Uri videoUri = Uri.parse(videoUrl);
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer =
                        ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mPlayerView.setPlayer(mExoPlayer);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(videoUri);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(mPlayState);
                if (mPlayerPosition != 0 ){
                    mExoPlayer.seekTo(mPlayerPosition);

                }
            }
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle(getString(R.string.network_error));
            alertDialog.setMessage(getString(R.string.no_network));
            alertDialog.show();
        }

    }

    public void releaseExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
        }
    }

    public void resetPlayerState() {
        mPlayerPosition = 0;
        mPlayState = false;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
