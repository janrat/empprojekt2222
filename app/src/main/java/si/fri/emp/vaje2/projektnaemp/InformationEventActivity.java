package si.fri.emp.vaje2.projektnaemp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class InformationEventActivity extends AppCompatActivity {

    TextView tvEventName,tvDescription,tvPrice,tvTime,tvPlace,tvTags, tvUrl;
    ImageView ivPicture, ivLike, ivGoing;
    String personID;
    String liked, going;
    private ArrayAdapter<String> eventAdapter;
    private RequestQueue requestQueue;
    private String id;
    private String eventID;

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

        SharedPreferences prfs = getSharedPreferences("ACCOUNT_INFO", Context.MODE_PRIVATE);
        personID = prfs.getString("Authentication_Id", "");
        String name =  prfs.getString("Authentication_Name", "");

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("eventID")!= null)
        {
            id = bundle.getString("eventID");
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());



        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked.equals("0")) {
                    ivLike.setImageResource(R.drawable.ic_like_fill);
                    Snackbar.make(v, "Dogodek všečkan.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Liked/" + personID + "/" + id + "/1";
                    JsonObjectRequest request = new JsonObjectRequest(url, null, null, null);
                    requestQueue.add(request);
                    liked = "1";
                }
                else {
                    ivLike.setImageResource(R.drawable.ic_like_hollow);
                    Snackbar.make(v, "Dogodek odvšečkan.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Liked/" + personID + "/" + id + "/0";
                    JsonObjectRequest request = new JsonObjectRequest(url, null, null, null);
                    requestQueue.add(request);
                    liked = "0";
                }
            }
        });

        ivGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(going.equals("0")){
                    ivGoing.setColorFilter(getResources().getColor(R.color.green));
                    Snackbar.make(v, "Grem na dogodek.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Going/" + personID + "/" + id + "/1";
                    JsonObjectRequest request = new JsonObjectRequest(url, null, null, null);
                    requestQueue.add(request);
                    going = "1";
                    scheduleNotification(tvEventName.getText().toString(),tvTime.getText().toString(),Integer.parseInt(eventID));
                }
                else {
                    ivGoing.setColorFilter(getResources().getColor(R.color.black));
                    Snackbar.make(v, "Ne grem na dogodek.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Going/" + personID + "/" + id + "/0";
                    JsonObjectRequest request = new JsonObjectRequest(url, null, null, null);
                    requestQueue.add(request);
                    going = "0";
                }
            }
        });

        getEventInfo();
    }

    public void getEventInfo () {
        String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Event/" + personID + "/" + id;
        JsonObjectRequest request = new JsonObjectRequest(url, null, jsonObjectListener, errorListener);
        requestQueue.add(request);
    }
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
                try {
                    eventID = response.getString("eventID");
                    String name = response.getString("name");
                    String description = response.getString("description");
                    String price = response.getString("price");
                    String organiser = response.getString("organiser");
                    String city = response.getString("city");
                    String time = response.getString("timeStarts");
                    String url = response.getString("tickets");
                    liked = response.getString("liked");
                    going = response.getString("going");
                    if(liked.equals("1")) {
                        ivLike.setImageResource(R.drawable.ic_like_fill);
                    }
                    if(going.equals("1")) {
                        ivGoing.setColorFilter(getResources().getColor(R.color.green));
                    }
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

    private void scheduleNotification(String notificationName, String notificationTime, int notificationID) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        long diffInMs = 0;
        try {
            Date eventStartTime = format.parse(notificationTime);
            Date currentTime = Calendar.getInstance().getTime();
            diffInMs = eventStartTime.getTime() - currentTime.getTime() - 1000*60*60;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Context context = getApplicationContext();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle(notificationName)
            .setContentText("Imate dogodek ob "+ notificationTime)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_menu_share);

        Intent intent = new Intent(context, InformationEventActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + diffInMs;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        Log.d("notification", "sem v scheduleNotification");
    }

}
