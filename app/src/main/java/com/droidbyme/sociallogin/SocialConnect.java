package com.droidbyme.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Sagar on 04-02-2018.
 */

public class SocialConnect {

    private Context context;
    private onFacebookListener onFacebookListener;

    private onGoogleListener onGoogleListener;

    public void setOnGoogleListener(SocialConnect.onGoogleListener onGoogleListener) {
        this.onGoogleListener = onGoogleListener;
    }

    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 007;

    public void setOnFacebookListener(SocialConnect.onFacebookListener onFacebookListener) {
        this.onFacebookListener = onFacebookListener;
    }

    public SocialConnect(Context context) {
        this.context = context;
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void connectFb() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile, email, user_birthday, user_friends"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.e("fb_reponse", response.getJSONObject().toString());
                                JSONObject profile = response.getJSONObject();

                                if (onFacebookListener != null) {
                                    onFacebookListener.onSuccess(profile);
                                }

                                LoginManager.getInstance().logOut();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (onFacebookListener != null) {
                    onFacebookListener.onCancel();
                }
                Log.e("CANCEL_FB", "CANCELLED");
            }

            @Override
            public void onError(FacebookException e) {
                if (onFacebookListener != null) {
                    onFacebookListener.onError(e);
                }
                Log.e("ERROR_FB", e.toString());
            }
        });
    }

    public void connectGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public interface onFacebookListener {

        public void onSuccess(JSONObject profile);

        public void onCancel();

        public void onError(Exception e);
    }

    public void setResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (onGoogleListener != null) {
                onGoogleListener.onSucess(account);
            }
        } catch (ApiException e) {
            if (onGoogleListener != null) {
                onGoogleListener.onFail(e);
            }
        }
    }

    public interface onGoogleListener {

        public void onSucess(GoogleSignInAccount account);

        public void onFail(ApiException exc);
    }
}
