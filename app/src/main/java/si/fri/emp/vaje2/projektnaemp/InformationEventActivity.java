package si.fri.emp.vaje2.projektnaemp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InformationEventActivity extends AppCompatActivity {

    TextView tvEventName,tvDescription,tvPrice,tvTime,tvPlace,tvTags, tvUrl;
    ImageView ivPicture;
    private ArrayAdapter<String> eventAdapter;
    private RequestQueue requestQueue;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_event);

        tvEventName = (TextView)findViewById(R.id.tvEventName);
        tvDescription = (TextView)findViewById(R.id.tvDescription);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvPlace = (TextView)findViewById(R.id.tvPlace);
        tvTags = (TextView)findViewById(R.id.tvTags);
        tvUrl = (TextView)findViewById(R.id.tvUrl);

        ivPicture =  findViewById(R.id.ivPicture);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("eventID")!= null)
        {
            id = bundle.getString("eventID");
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        getEventInfo();
    }

    public void getEventInfo () {
        String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Event/" + id;
        JsonObjectRequest request = new JsonObjectRequest(url, null, jsonObjectListener, errorListener);
        requestQueue.add(request);
    }
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
                try {
                    //String eventID = response.getString("eventID");
                    String name = response.getString("name");
                    String description = response.getString("description");
                    String price = response.getString("price");
                    String organiser = response.getString("organiser");
                    String city = response.getString("city");
                    String time = response.getString("timeStarts");
                    /*DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = (Date)formatter.parse(time);*/


                    tvEventName.setText(name);
                    tvDescription.setText(description);
                    tvDescription.append("\nOrganizator: " + organiser);
                    if(price.equals("0")) {
                        tvPrice.setText("Ni vstopnine");
                    }
                    else {
                        tvPrice.setText(price + "€");
                    }
                    tvPlace.setText(response.getString("location"));
                    tvPlace.append(", " + city);
                    tvTags.setVisibility(View.INVISIBLE);
                    //tvTags.setText(response.getString("tags"));
                    tvUrl.setText(response.getString("tickets"));
                    tvTime.setText(time);

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
}
