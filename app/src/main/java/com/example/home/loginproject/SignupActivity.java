package com.example.home.loginproject;

/**
 * Created by Home on 7/14/2016.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _nameText, _emailText, _passwordText;
    Button _signupButton;
    TextView _loginLink;
    DatabaseOperations databaseOperations;

    String name, email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        registerListeners();
        databaseOperations = new DatabaseOperations();
    }

    public void initializeViews() {
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _nameText = (EditText) findViewById(R.id.input_name);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
    }

    public void registerListeners() {
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        }

    public void signup() {

        if (validate()) {
            registerUser(name, email, password);
        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString().trim();
        email = _emailText.getText().toString().trim();
        password = _passwordText.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }





    /**
     * Function to run a query in database
     */
    public void registerUser(String name, String email, String password) {
        // call makeJsonArrayRequest and send url, tag, errorTextView and instantiate a callBack
        databaseOperations.postSearch("http://162.243.100.186/signup_request.php", name, email, password,
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
                            // if email exists in the system
                            if (result.contains("emailExists")) {
                                // setTextOfErrorTextView(getResources().getString(R.string.no_matches));
                                Toast.makeText(Util.getContext(),"email address exists in the system", Toast.LENGTH_LONG).show();
                            }
                            // if query is missing required fields param
                            else if (result.contains("noFields")) {
                                //setTextOfErrorTextView(getResources().getString(R.string.search_empty));
                                Toast.makeText(Util.getContext(),"missing required fields", Toast.LENGTH_LONG).show();
                            }
                            // if user wasn't inserted
                            else if (result.contains("userNotInserted")) {
                                //setTextOfErrorTextView(getResources().getString(R.string.search_empty));
                                Toast.makeText(Util.getContext(),"An error occurred. " +
                                        "User not inserted in the system", Toast.LENGTH_LONG).show();
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