package si.fri.emp.vaje2.projektnaemp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOfEventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<String> eventList;
    private ArrayAdapter<String> eventAdapter;
    private ListView lvEvents;

    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListOfEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListOfEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfEventsFragment newInstance(String param1, String param2) {
        ListOfEventsFragment fragment = new ListOfEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_list_of_events, container, false);

        lvEvents = (ListView) RootView.findViewById(R.id.lvEvents);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        getEvents();

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout ll = (LinearLayout)view;
                TextView tvEventID = (TextView) ll.findViewById(R.id.eventID);
                String eventID = tvEventID.getText().toString();
                Intent intent = new Intent(getContext(), InformationEventActivity.class);
                intent.putExtra("eventID",eventID);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) RootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        return RootView;
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
                    String city = object.getString("city");
                    String timeStarts = object.getString("timeStarts");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("eventID", eventID);
                    map.put("name", name);
                    map.put("city", city);
                    map.put("timeStarts", timeStarts);
                    data.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), data, R.layout.events_list_item,
                    new String[]{"name","eventID","city","timeStarts"},
                    new int[]{R.id.eventName,R.id.eventID,R.id.eventCity,R.id.eventTime});
            lvEvents.setAdapter(adapter);
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        getEvents();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
