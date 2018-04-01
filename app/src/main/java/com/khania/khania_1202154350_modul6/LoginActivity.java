package com.khania.khania_1202154350_modul6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText emailLogin, passwordLogin;
    Button btnLogin, btnSignUp;

    FirebaseAuth auth;

    private static final String TAG = "EmailPassword";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // jika user berhasil register dan akan langsung login
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void login(View view) {
        signIn(emailLogin.getText().toString(),passwordLogin.getText().toString());

    }

    private void signIn(String email,String password) {
        Log.d(TAG,"signIn :" + email);

        if (!validateForm()){
            return;
        }

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:" +task.isSuccessful());
                        if (task.isSuccessful()) {


//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            Intent a = new Intent(MainActivity.this,Home.class);
//                            startActivity(a);

                            onAuthSuccess(task.getResult().getUser());

//                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication failed.",

                                    Toast.LENGTH_SHORT).show();

//                            updateUI(null);
                        }
                    }
                });

    }



    public void signUp(View view) {
        createAccount(emailLogin.getText().toString(),passwordLogin.getText().toString());
    }

    private void createAccount(String email,String password) {
        Log.d(TAG,"createAccount" + email);

        if (!validateForm()){
            return;
        }


        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG,"createUserWithEmail:" + task.isSuccessful());
                        if (task.isSuccessful()){
//                            Log.d(TAG,"createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(MainActivity.this,"Authentication Success.",Toast.LENGTH_SHORT).show();
                            onAuthSuccess(task.getResult().getUser());

//                            updateUI(user);
                        } else{
                            Log.w(TAG,"createUserWithEmail:failure",task.getException());
                            Toast.makeText(LoginActivity.this,"Sign Up Failed.",Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        writeNewUser(user.getUid(),username,user.getEmail());

        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        finish();
    }

    private void writeNewUser(String uid, String username, String email) {
        UserModel user = new UserModel(username,email);

        mDatabase.child("users").child(uid).setValue(user);
    }

    private String usernameFromEmail(String email) {

        if (email.contains("@")){
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void signOut() {
        auth.signOut();
//        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){

        }
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailLogin.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailLogin.setError("Required");
            valid = false;
        }
        else {
            emailLogin.setError(null);
        }

        String password = passwordLogin.getText().toString();
        if (TextUtils.isEmpty(password)){
            passwordLogin.setError("Required");
            valid = false;
        } else{
            passwordLogin.setError(null);
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.optionLogout) {

            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
