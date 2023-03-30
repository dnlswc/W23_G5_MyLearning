package com.example.mylearning.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.notepad.Database;
import com.example.mylearning.notepad.Note;
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

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    TextView textViewSignIn, textViewName, textViewNumberOfNoteCreated, textViewEmail, textViewRemark;
    Button buttonSignIn, buttonSignOut;
    ImageView imageViewGoogle;

    BottomNavigationView bottomNavigationView;

    GifImageView gifViewGoogle;
   // public static String  displayNameInBar = "Guess";
    public static String  displayNameInBar = "Empty";

    public static String stringInTextViewEmail="Empty";
    public static int numberOfNoteToBePassed = 0;
    List<Note> notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String tempAuthorEmail = sharedPreferences.getString("AUTHOR_EMAIL", "Nothing");
            String tempAuthorName = sharedPreferences.getString("FIRST_NAME", "Nothing");
            int tempNumberOfNote = sharedPreferences.getInt("NUMBER_OF_NOTE", -1);

            if (!(tempAuthorEmail.equals("Nothing")))
            {
                stringInTextViewEmail = tempAuthorEmail;
            }
            if (!(tempAuthorName.equals("Nothing")))
            {
                displayNameInBar = tempAuthorName;
            }
            if (tempNumberOfNote!=-1)
            {
                numberOfNoteToBePassed = tempNumberOfNote;
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }


        getSupportActionBar().setTitle("My Account");

        textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewName = findViewById(R.id.textViewName);
        textViewNumberOfNoteCreated = findViewById(R.id.textViewNumberOfNoteCreated);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewRemark = findViewById(R.id.textViewRemark);
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
            NotePageActivity.author_email = email;
            stringInTextViewEmail = email;

            Database db = new Database(this);
            notes = db.getNotes();
            numberOfNoteToBePassed = notes.size();

            //  textViewName.setText("User: " + displayName);
            textViewName.setText("User name: " + firstName);
            displayNameInBar = firstName;

            textViewEmail.setText("Email: " + email);
            textViewSignIn.setText("Welcome Back");
            // textViewNumberOfQuiz.setText("Number of notes created: xxx");
            //textViewNumberOfNoteCreated.setText("Number of notes created: " + NotePageActivity.numberOfNoteForEachAuthor);
            textViewNumberOfNoteCreated.setText("Number of notes created: " + numberOfNoteToBePassed);

           // textViewRemark.setText("Score obtained: xxx");
          //  textViewRemark.setText("You can read and write your own set of notes!");
            textViewRemark.setText("Let's read and write your own set of notes!");

            buttonSignIn.setText("Sign-out");
            imageViewGoogle.setImageResource(R.drawable.account_photo_2);
            gifViewGoogle.setImageResource(0);

            // Toast.makeText(this, ""+googleSignInAccount.getAccount(), Toast.LENGTH_SHORT).show();

            //  imageViewGoogle.setImageDrawable
            //textViewSignIn.setTextSize(20);
            /*
            buttonSignOut.setVisibility(View.VISIBLE);
            buttonSignIn.setVisibility(View.INVISIBLE);*/

            //Toast.makeText(this, buttonSignIn.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            displayNameInBar = "Guest";
            Database db = new Database(this);
            notes = db.getNotes();
            numberOfNoteToBePassed = notes.size();
        }

        /*
        if(googleSignInAccount!=null){
            directToLoginPage();
        }
        */

        //buttonSignIn.setVisibility(View.INVISIBLE);


/*
       Database db = new Database(this);
        notes = db.getNotes();
        numberOfNoteToBePassed = notes.size();
*/


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((buttonSignIn.getText().toString()).equals("Sign-in")) {
                    signIn();
                }
                else if ((buttonSignIn.getText().toString()).equals("Sign-out")) {
                    NotePageActivity.author_email = "Guest3175@gmail.com";
                    stringInTextViewEmail =  "Guest3175@gmail.com";
                    displayNameInBar = "Guest";
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
                Toast.makeText(getApplicationContext(), "Failed to login Google" + " :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("GOOGLEFAILURE",e.getMessage());
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("AUTHOR_EMAIL", stringInTextViewEmail);
        editor.putString("FIRST_NAME", displayNameInBar);
        editor.putInt("NUMBER_OF_NOTE", numberOfNoteToBePassed);

        editor.commit();

    }

}