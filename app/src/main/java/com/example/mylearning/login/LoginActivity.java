package com.example.mylearning.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.notepad.NotePageActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    TextView textViewSignIn, textViewName, textViewNumberOfQuiz, textViewEmail, textViewScore;
    Button buttonSignIn, buttonSignOut;
    ImageView imageViewGoogle;

    BottomNavigationView bottomNavigationView;

    GifImageView gifViewGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("My Account");

        textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewName = findViewById(R.id.textViewName);
        textViewNumberOfQuiz = findViewById(R.id.textViewNumberOfQuiz);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewScore = findViewById(R.id.textViewScore);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myAccount);
        imageViewGoogle = findViewById(R.id.imageViewGoogle);
        gifViewGoogle = findViewById(R.id.gifViewGoogle);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Toast.makeText(this, "Button Text: " + buttonSignIn.getText().toString(), Toast.LENGTH_SHORT).show();

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
         //   String displayName = googleSignInAccount.getDisplayName();
            String firstName = googleSignInAccount.getGivenName();

            String email = googleSignInAccount.getEmail();
            //  textViewName.setText("User: " + displayName);
            textViewName.setText("User name: " + firstName);

            textViewEmail.setText("Email: " + email);
            textViewSignIn.setText("Welcome");
            textViewNumberOfQuiz.setText("Number of notes created: xxx");
            textViewScore.setText("Score obtained: xxx");

            buttonSignIn.setText("Sign-out");
            imageViewGoogle.setImageResource(R.drawable.account_photo_2);
            gifViewGoogle.setImageResource(R.drawable.account_photo_19);

           // Toast.makeText(this, ""+googleSignInAccount.getAccount(), Toast.LENGTH_SHORT).show();

          //  imageViewGoogle.setImageDrawable
            //textViewSignIn.setTextSize(20);
            /*
            buttonSignOut.setVisibility(View.VISIBLE);
            buttonSignIn.setVisibility(View.INVISIBLE);*/

            //Toast.makeText(this, buttonSignIn.getText().toString(), Toast.LENGTH_SHORT).show();
        }

        /*
        if(googleSignInAccount!=null){
            directToLoginPage();
        }
        */

        //buttonSignIn.setVisibility(View.INVISIBLE);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((buttonSignIn.getText().toString()).equals("Sign-in")) {
                    signIn();
                }
                else if ((buttonSignIn.getText().toString()).equals("Sign-out")) {
                    signOut();
                }
            }

        });



        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) -> {

            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.myQuiz:
                    startActivity(new Intent(getApplicationContext(), QuizCatalogueActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.myNote:
                    startActivity(new Intent(getApplicationContext(), NotePageActivity.class));
                    overridePendingTransition(0, 0);
                    return true;


                case R.id.myNews:
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.myAccount:
                    return true;
            }

            return false;

        });

    }


    private void directToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 999);
    }

    void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                googleSignInAccountTask.getResult(ApiException.class);
                Toast.makeText(getApplicationContext(), "Successful to login Google", Toast.LENGTH_SHORT).show();
                directToLoginPage();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Failed to login Google", Toast.LENGTH_SHORT).show();
            }
        }

    }

}