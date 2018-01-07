package si.fri.emp.vaje2.projektnaemp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText etUser, etPass;
    Button btn_SignIn;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfLoggedIn(getApplicationContext());

        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);

        btn_SignIn = (Button) findViewById(R.id.btnSignIn);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        /*btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });*/

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        boolean success = true;
        // Store values at the time of the login attempt.
        String username = etUser.getText().toString();
        String password = etPass.getText().toString();

        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPass.setError(getString(R.string.error_invalid_password));
            focusView = etPass;
            success = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            etUser.setError(getString(R.string.error_field_required));
            focusView = etUser;
            success = false;
        } else if (!isEmailValid(username)) {
            etUser.setError(getString(R.string.error_invalid_email));
            focusView = etUser;
            success = false;
        }

        if(success) {
            Login(username, password);
        }
        else {
            focusView.requestFocus();
        }
    }

    public void Login(String user, String pass) {
        String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/login/" + user + "/" + pass;
        JsonObjectRequest request = new JsonObjectRequest(url, null, jsonObjectListener, errorListener);
        requestQueue.add(request);
    }

    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                String city = response.getString("city");
                String cityID = response.getString("cityID");
                String email = response.getString("email");
                String name = response.getString("name");
                String personID = response.getString("personID");
                String surname = response.getString("surname");
                String valid = response.getString("valid");

                if(valid.equals("True")) {
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    //intent.putExtra("personID",personID);
                    //intent.putExtra("name",name);
                    SharedPreferences.Editor editor = getSharedPreferences("ACCOUNT_INFO", MODE_PRIVATE).edit();
                    editor.putString("Authentication_Id",personID);
                    editor.putString("Authentication_Name",name);
                    editor.putString("Authentication_Email",email);
                    editor.putString("City_id",cityID);
                    editor.putString("City_name",city);
                    editor.apply();

                    writeUserToFile(personID+"\n"+name+"\n"+email+"\n"+city+"\n"+cityID,getApplicationContext());

                    startActivity(intent);
                    finish();
                }
                else {
                    etUser.setError(getString(R.string.error_invalid_login));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void writeUserToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("user.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void checkIfLoggedIn(Context context) {
        try {
            InputStream inputStream = context.openFileInput("user.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String personID = bufferedReader.readLine();
                String name = bufferedReader.readLine();
                String email = bufferedReader.readLine();
                String city = bufferedReader.readLine();
                String cityID = bufferedReader.readLine();

                SharedPreferences.Editor editor = getSharedPreferences("ACCOUNT_INFO", MODE_PRIVATE).edit();
                editor.putString("Authentication_Id",personID);
                editor.putString("Authentication_Name",name);
                editor.putString("Authentication_Email",email);
                editor.putString("City_id",cityID);
                editor.putString("City_name",city);
                editor.apply();

                inputStream.close();

                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}