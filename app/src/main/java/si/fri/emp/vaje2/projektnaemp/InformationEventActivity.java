package si.fri.emp.vaje2.projektnaemp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView ivPicture, ivLike, ivGoing;
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

        tvUrl = (TextView)findViewById(R.id.tvUrl);

        ivPicture =  findViewById(R.id.ivPicture);
        ivLike =  findViewById(R.id.ivLike);
        ivGoing =  findViewById(R.id.ivGoing);

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivLike.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_like_hollow).getConstantState()) {
                    ivLike.setImageResource(R.drawable.ic_like_fill);
                    Snackbar.make(v, "Event liked", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    ivLike.setImageResource(R.drawable.ic_like_hollow);
                    Snackbar.make(v, "Event disliked", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        ivGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivGoing.getMaxHeight() == 2625){
                    ivGoing.setMaxHeight(2624);
                    ivGoing.setColorFilter(getResources().getColor(R.color.green));
                    Snackbar.make(v, "Event signup", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    ivGoing.setMaxHeight(2625);
                    ivGoing.setColorFilter(getResources().getColor(R.color.black));
                    Snackbar.make(v, "Event signoff", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

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
                    String url = response.getString("tickets");
                    String text = "<a href=\""+ url +"\">Kupi vstopnico</a>";

                    /*DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = (Date)formatter.parse(time);*/

                    setTitle("O dogodku");
                    tvEventName.setText(name);
                    tvDescription.setText(description);
                    tvDescription.append("\nOrganizator: " + organiser);
                    if(price.equals("0")) {
                        tvPrice.setText("Vstop brezplačen");
                    }
                    else {
                        tvPrice.setText("Cena: " + price + "€");
                    }
                    tvPlace.setText(response.getString("location"));
                    tvPlace.append(", " + city);
                    //tvTags.setText(response.getString("tags"));
                    if (Build.VERSION.SDK_INT >= 24) {
                        tvUrl.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        tvUrl.setText(Html.fromHtml(text));
                    }
                    tvUrl.setMovementMethod(LinkMovementMethod.getInstance());

                    tvTime.setText(time.split(" ")[0] + ", " + time.split(" ")[1]);

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
