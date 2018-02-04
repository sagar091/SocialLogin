package com.droidbyme.sociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //private CallbackManager callbackManager;
    private android.widget.Button btnFb;
    private android.widget.TextView txtFb;
    private android.widget.Button btnGoogle;
    private android.widget.TextView txtGoogle;

    SocialConnect connect;

    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtGoogle = (TextView) findViewById(R.id.txtGoogle);
        this.btnGoogle = (Button) findViewById(R.id.btnGoogle);
        this.txtFb = (TextView) findViewById(R.id.txtFb);
        this.btnFb = (Button) findViewById(R.id.btnFb);

        //FacebookSdk.sdkInitialize(this);

        connect = new SocialConnect(this);

        // callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.connectGoogle();
                connect.setOnGoogleListener(new SocialConnect.onGoogleListener() {
                    @Override
                    public void onSucess(GoogleSignInAccount account) {
                        updateUI(account);
                    }

                    @Override
                    public void onFail(ApiException exc) {
                        Log.e("ERROR", exc.getMessage());
                    }
                });
            }
        });

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.connectFb();
                connect.setOnFacebookListener(new SocialConnect.onFacebookListener() {
                    @Override
                    public void onSuccess(JSONObject profile) {
                        String social_id = null;
                        try {
                            social_id = profile.getString("id");
                            String email = profile.getString("email");
                            String fName = profile.getString("name").split(" ")[0];
                            String lName = profile.getString("name").split(" ")[1];

                            txtFb.setText(String.format("%s\n%s\n%s %s", social_id, email, fName, lName));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancel() {
                        Log.e("CANCEL_FB", "CANCELLED");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("ERROR_FB", e.toString());
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        connect.setResult(requestCode, resultCode, data);
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        String fName = account.getDisplayName().split(" ")[0];
        String lName = account.getDisplayName().split(" ")[1];
        String email = account.getEmail();
        String social_id = account.getId();

        txtGoogle.setText(String.format("%s\n%s\n%s %s", social_id, email, fName, lName));
    }
}
