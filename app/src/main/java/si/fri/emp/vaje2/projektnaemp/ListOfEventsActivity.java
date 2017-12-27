package si.fri.emp.vaje2.projektnaemp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

        /*eventList = new ArrayList<String>();
        eventList.add("Event1");
        eventList.add("Event2");
        eventList.add("Event3");
        eventList.add("Event4");

        eventAdapter = new ArrayAdapter<String>(ListOfEventsActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1,eventList);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        lvEvents.setAdapter(eventAdapter);*/

        getEvents();
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
                    String id = object.getString("eventID");
                    String name = object.getString("name");
                    String description = object.getString("description");
                    String price = object.getString("price");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("description", description);
                    map.put("price", price);
                    data.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.events_list_item,
                    new String[]{"name","description","price"},
                    new int[]{R.id.eventName,R.id.eventDescription,R.id.eventPrice});
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
