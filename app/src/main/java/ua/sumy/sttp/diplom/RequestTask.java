package ua.sumy.sttp.diplom;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

 public class RequestTask extends AsyncTask<String, String, String> {
    String response;
    private ProgressDialog dialog;
    LoginActivity loginActivity;

    public RequestTask(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            //создаем запрос на сервер
            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            //он у нас будет посылать post запрос
            HttpGet httpGet = new HttpGet(params[0]);
            //будем передавать два параметра
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //передаем параметры из наших текстбоксов
            //лоигн
            //nameValuePairs.add(new BasicNameValuePair("login", login.getText().toString()));
            //пароль
            //nameValuePairs.add(new BasicNameValuePair("pass", pass.getText().toString()));
            //собераем их вместе и посылаем на сервер
            //postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //получаем ответ от сервера
            response = hc.execute(httpGet, res);
            //посылаем на вторую активность полученные параметры
            /*Intent intent = new Intent(MainActivity.this, SecondActivity.class)
            intent.putExtra(SecondActivity.JsonURL, response.toString());
            startActivity(intent)*/;
            Log.e("errrrrrrrooorororor",response);
            /*SharedPreferences sharedPreferences = loginActivity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putString("json", response);
            ed.commit();*/
        } catch (Exception e) {
            System.out.println("Exp=" + e);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {

        dialog.dismiss();
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(loginActivity);
        dialog.setMessage("Загружаюсь...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        super.onPreExecute();
    }

}