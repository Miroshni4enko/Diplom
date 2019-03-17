package ua.sumy.sttp.diplom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Button signIn;
    EditText loginText;
    //EditText groupText;
    static String login;
    static String group;
    static String subgroup;
    private String selection;
    static SharedPreferences sharedPreferences;
    final static String SAVE_LOGIN = "SAVE_LOGIN";
    final static String SAVE_GROUP = "SAVE_GROUP";
    final static String SAVE_SUBGROUP = "SAVE_SUBGROUP";
    //public static String JsonURL;
    private static ArrayList<String> listGroup;
    private static final String FIRST = "firstname";
    Spinner spinner;
    public ListView listView;
    String json;
    String jsonParam;
    RadioButton sub1;
    RadioButton sub2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSignByPreference();
        signIn = (Button) findViewById(R.id.signIn);

        loginText = (EditText)

                findViewById(R.id.login);

        View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                RadioButton rb = (RadioButton) v;
                switch (rb.getId()) {
                    case R.id.subgroup1:
                        subgroup = "1";
                        break;
                    case R.id.subgroup2:
                        subgroup = "2";
                        break;
                    default:
                        break;
                }
            }
        };
        //groupText = (EditText) findViewById(R.id.group);
        signIn.setOnClickListener(this);
        sub1 = (RadioButton)findViewById(R.id.subgroup1);
        sub1.setOnClickListener(radioListener);

        sub2 = (RadioButton)findViewById(R.id.subgroup2);
        sub2.setOnClickListener(radioListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        getSignByPreference();
        if(!login.equals("pustoy")){
            loginText.setText(login);
            //selection.setText(group);
        }else if(login.equals("pustoy")){loginText.setText("");};

        RequestTask requestTask = new RequestTask(this);
        //RequestTaskWithParam requestTask1 = new RequestTaskWithParam(this);
        try {
            json = requestTask.execute("http://data.stpp.sumy.ua/rozklad/group.php").get();
            //nameValuePairs.add(new BasicNameValuePair("date", "18.06.2014"));
            //пароль
            //nameValuePairs.add(new BasicNameValuePair("group", "Е-22"));
            //nameValuePairs.add(new BasicNameValuePair("subgroup", "1"));
            //  jsonParam = requestTask1.execute("http://data.stpp.sumy.ua/rozklad/json_replacement.php?date=08.06.2016&group=%D5-117&subgroup=1").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Установка слушателя для выпадающего списка
        spinner = (Spinner) findViewById(R.id.cities_spinner);
        //listView = (ListView) findViewById(R.id.list);
        //name = (TextView) findViewById(R.id.name);
        System.out.println("this");
        listGroup = new ArrayList<String>();
        //принимаем параметр который мы послылали в manActivity
        //Bundle extras = getIntent().getExtras();
        //превращаем в тип стринг для парсинга;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        //System.out.println("this" + json);
        //Log.e("error",json + "dddddd"+test);
        spinner.setOnItemSelectedListener(this);
        //передаем в метод парсинга
        //JSONWithParam(jsonParam);
        JSONURL(json);
        if(!group.equals("pustoy")){
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            int position = adapter.getPosition(group);
            spinner.setSelection(position);
            if(subgroup.equals("1")) {
                sub1.setChecked(true);
            }else if(subgroup.equals("2")){
                sub2.setChecked(true);
            }

        }

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==signIn.getId()) {
            if(loginText.getText()!=null) {
                saveSign();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(SAVE_LOGIN, loginText.getText().toString());
                intent.putExtra(SAVE_GROUP, selection);
                intent.putExtra(SAVE_SUBGROUP,subgroup);
                startActivity(intent);
            }
        }

    }
    public void saveSign(){
        sharedPreferences = getPreferences(MODE_APPEND);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(SAVE_LOGIN, loginText.getText().toString());
        ed.putString(SAVE_GROUP, selection);
        ed.putString(SAVE_SUBGROUP,subgroup);
        ed.commit();
        Log.d("save","text saved" + loginText.getText().toString());
    }
    public  void getSignByPreference(){
        sharedPreferences = getPreferences(MODE_PRIVATE);

        login = sharedPreferences.getString(LoginActivity.SAVE_LOGIN,"pustoy");
        group = sharedPreferences.getString(LoginActivity.SAVE_GROUP,"pustoy");
        subgroup =sharedPreferences.getString(SAVE_SUBGROUP,"pustoy");
        Log.d("load","text load" + login);
    }

    public void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(SAVE_LOGIN, login);
        //intent.putExtra(SAVE_GROUP, group);
        startActivity(intent);
    }
    public void JSONURL(String result) {
        try {
            JSONArray urls = new JSONArray(result);
            listGroup.add("виберіть групу");
            for (int i = 0; i < urls.length(); i++) {
                listGroup.add(urls.getJSONObject(i).getString("name").toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, listGroup);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        selection = item.toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
