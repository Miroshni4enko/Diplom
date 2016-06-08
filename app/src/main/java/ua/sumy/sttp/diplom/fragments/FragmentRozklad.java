package ua.sumy.sttp.diplom.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.sumy.sttp.diplom.CodingToWindows1251;
import ua.sumy.sttp.diplom.MainActivity;
import ua.sumy.sttp.diplom.R;
import ua.sumy.sttp.diplom.RequestTaskWithParam;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRozklad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRozklad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRozklad extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIRST = "firstname";
    private static final String LAST = "lastname";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String jsonParam;
    ListView listView;

    List<HashMap<String, Object>>  myBooks = new ArrayList<HashMap<String, Object>>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentRozklad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRozklad.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRozklad newInstance(String param1, String param2) {
        FragmentRozklad fragment = new FragmentRozklad();
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
        return inflater.inflate(R.layout.fragment_rozklad, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        //TextView output = (TextView) getActivity().findViewById(R.id.out_rozklad);
        RequestTaskWithParam requestTask1 = new RequestTaskWithParam();
        listView = (ListView) getActivity().findViewById(R.id.list);
        try {
            //nameValuePairs.add(new BasicNameValuePair("date", "18.06.2014"));
            //пароль
            //nameValuePairs.add(new BasicNameValuePair("group", "Е-22"));
            //nameValuePairs.add(new BasicNameValuePair("subgroup", "1"));
            String group = CodingToWindows1251.coding(MainActivity.group);

            long day = 24 * 60*60;
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            Date tommorrow =  new Date(new Date().getTime() + day);
            Log.e("group",group +" "+ MainActivity.subgroup + " "+tommorrow);
            jsonParam = requestTask1.execute("http://data.stpp.sumy.ua/rozklad/json_replacement.php?date="+format.format(tommorrow).toString()+"&group="+group+ "&subgroup="+MainActivity.subgroup).get();
            JSONWithParam(jsonParam);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void JSONWithParam(String result){
        try {
            JSONArray urls = new JSONArray(result);
            // name.setText(json.getString("name").toString());
            //дальше находим вход в наш json им является ключевое слово data
            //проходим циклом по всем нашим параметрам
            for (int i = 0; i < urls.length(); i++) {
                HashMap<String, Object> hm;
                hm = new HashMap<String, Object>();
                //читаем что в себе хранит параметр firstname
                hm.put(FIRST, i+1 + ". "+ urls.getJSONObject(i).getString("less").toString()+ "\n");
                //читаем что в себе хранит параметр lastname
                //читаем что в себе хранит параметр lastname
                hm.put(LAST, "Викладач: "+urls.getJSONObject(i).getString("teach").toString()+ "\n");
                myBooks.add(hm);
                //дальше добавляем полученные параметры в наш адаптер
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), myBooks, R.layout.list,
                        new String[] { FIRST, LAST, }, new int[] { R.id.text1, R.id.text2 });
                //выводим в листвбю
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                ;
                // Применяем адаптер к элементу spinner
                //spinner.setAdapter(adapter);
                //дальше добавляем полученные параметры в наш адаптер
                /*SimpleAdapter adapter = new SimpleAdapter(this, myBooks, R.layout.activity_login,
                        new String[] { FIRST}, new int[] {});
                //выводим в листвбю
                spinner.setAdapter(adapter);
               listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
