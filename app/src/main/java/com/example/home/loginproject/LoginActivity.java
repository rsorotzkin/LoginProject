package com.example.home.loginproject;

/**
 * Created by Home on 7/14/2016.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DatabaseOperations databaseOperations;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    AutoCompleteTextView _emailText;

    String email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // send activity reference to Util class
        Util.setReference(this);
        initializeViews();
        registerListeners();

    }

    public void initializeViews() {
        databaseOperations = new DatabaseOperations();
        _emailText = (AutoCompleteTextView) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);


    }

    public void registerListeners() {
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "test click", Toast.LENGTH_LONG).show();

                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    public void login() {
        // if email or password are valid
        if (validate()) {
            registerUser(email, password);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }


    public boolean validate() {
        boolean valid = true;

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            Toast.makeText(Util.getContext(),"email not valid",Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            Toast.makeText(Util.getContext(),"password not valid",Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    /**
     * Function to run a query in database
     */
    public void registerUser(String email, String password) {
            // call makeJsonArrayRequest and send url, tag, errorTextView and instantiate a callBack
            databaseOperations.postSearch("http://162.243.100.186/login_request.php", email, password,
                    new DatabaseOperations.VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            // if result contains "sqlError" - query didn't run
                            if (result.contains("sqlError")) {
                                Toast.makeText(Util.getContext(),"a sql error occurred", Toast.LENGTH_LONG).show();
                               // setTextOfErrorTextView(getResources().getString(R.string.sql_error));
                            }
                            // if query ran
                            else {
                                // if query returned no results
                                if (result.contains("noUserFound")) {
                                   // setTextOfErrorTextView(getResources().getString(R.string.no_matches));
                                    Toast.makeText(Util.getContext(),"no user found", Toast.LENGTH_LONG).show();
                                }
                                // if query is missing required fields param
                                else if (result.contains("noFields")) {
                                    //setTextOfErrorTextView(getResources().getString(R.string.search_empty));
                                    Toast.makeText(Util.getContext(),"missing required fields", Toast.LENGTH_LONG).show();
                                }
                                // if query returned results
                                else {
                                // dismiss dialog and log user in
                                    Toast.makeText(Util.getContext(),"Login success", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                    });
        }
    }
