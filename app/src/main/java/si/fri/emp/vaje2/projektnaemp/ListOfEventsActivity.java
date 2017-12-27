package si.fri.emp.vaje2.projektnaemp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListOfEventsActivity extends AppCompatActivity {
    private List<String> eventList;
    private ArrayAdapter<String> eventAdapter;
    private ListView lvEvents;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);

        lvEvents = (ListView)findViewById(R.id.lvEvents);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        getEvents();

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout ll = (LinearLayout)view;
                TextView tvEventID = (TextView) ll.findViewById(R.id.eventID);
                String eventID = tvEventID.getText().toString();
                Intent intent = new Intent(ListOfEventsActivity.this, InformationEventActivity.class);
                intent.putExtra("eventID",eventID);
                startActivity(intent);
            }
        });
    }

    public void getEvents () {
        String url = "http://dogodkiserverapi.azurewebsites.net/Osebe.svc/Events";
        JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
        requestQueue.add(request);
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<HashMap<String, String>> data = new ArrayList<>();
            for (int i = 0; i<response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    String eventID = object.getString("eventID");
                    String name = object.getString("name");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("eventID", eventID);
                    map.put("name", name);
                    data.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.events_list_item,
                    new String[]{"name","eventID"},
                    new int[]{R.id.eventName,R.id.eventID});
            lvEvents.setAdapter(adapter);
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };
}
