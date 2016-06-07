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
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Button signIn ;
    EditText loginText;
    //EditText groupText;
    String login;
    String group;
    private String selection;
    SharedPreferences sharedPreferences;
    final static String  SAVE_LOGIN= "SAVE_LOGIN";
    final static String  SAVE_GROUP= "SAVE_GROUP";
    //public static String JsonURL;
    private static ArrayList<String> myBooks;
    private static final String FIRST = "firstname";
    Spinner spinner;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSignByPreference();
        signIn = (Button) findViewById(R.id.signIn);
        loginText = (EditText) findViewById(R.id.login);
        //groupText = (EditText) findViewById(R.id.group);
        signIn.setOnClickListener(this);
        if(!login.equals("pustoy")){
            loginText.setText(login);
            //selection.setText(group);
        }

         new RequestTask(this).execute("http://data.stpp.sumy.ua/rozklad/group.php");
        //Установка слушателя для выпадающего списка
        spinner = (Spinner) findViewById(R.id.cities_spinner);
        //listView = (ListView) findViewById(R.id.list);
        //name = (TextView) findViewById(R.id.name);
        System.out.println("this");
        myBooks = new ArrayList<String>();
        //принимаем параметр который мы послылали в manActivity
        //Bundle extras = getIntent().getExtras();
        //превращаем в тип стринг для парсинга;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String json =  sharedPreferences.getString("json","pustoy");
        System.out.println("this" + json);
        Log.e("error",json);
        spinner.setOnItemSelectedListener(this);
        //передаем в метод парсинга
        JSONURL(json);
        if(!group.equals("pustoy")){
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            int position = adapter.getPosition(group);
            spinner.setSelection(position);
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
                startActivity(intent);
            }
        }

    }
    public void saveSign(){
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(SAVE_LOGIN, loginText.getText().toString());
        ed.putString(SAVE_GROUP, selection);
        ed.commit();
        Log.d("save","text saved" + loginText.getText().toString());
    }
    public  void getSignByPreference(){
        sharedPreferences = getPreferences(MODE_PRIVATE);

        login = sharedPreferences.getString(LoginActivity.SAVE_LOGIN,"pustoy");
        group = sharedPreferences.getString(LoginActivity.SAVE_GROUP,"pustoy");
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
            System.out.println(result);
            Log.e("error",result);
            //создали читателя json объектов и отдали ему строку - result
            JSONArray urls = new JSONArray(result);
            // name.setText(json.getString("name").toString());
            //дальше находим вход в наш json им является ключевое слово data
            //проходим циклом по всем нашим параметрам
            for (int i = 0; i < urls.length(); i++) {
                //HashMap<String, Object> hm;
                //hm = new HashMap<String, Object>();
                //читаем что в себе хранит параметр firstname
                //hm.put(FIRST, urls.getJSONObject(i).getString("name").toString());
                //читаем что в себе хранит параметр lastname
                myBooks.add(urls.getJSONObject(i).getString("name").toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, myBooks);
                // Определяем разметку для использования при выборе элемента
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Применяем адаптер к элементу spinner
                spinner.setAdapter(adapter);
                //дальше добавляем полученные параметры в наш адаптер
                /*SimpleAdapter adapter = new SimpleAdapter(this, myBooks, R.layout.activity_login,
                        new String[] { FIRST}, new int[] {});
                //выводим в листвбю
                spinner.setAdapter(adapter);
               listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);*/
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
