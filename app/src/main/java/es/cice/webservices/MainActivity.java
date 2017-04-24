package es.cice.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private static final String SOAPACTION = "http://ws.cice.es/WSAndroid/WebService1";
    private static final String METHOD = "getLetraNIF";
    private static final String NAMESPACE = "http://ws.cice.es/";
    private static final String URL = "http://10.0.2.2:8084/WSAndroid/WebService1?wsdl";


    private EditText editText;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer value = Integer.parseInt(editText.getText().toString());
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(value);
            }
        });
    }


    private class MyAsyncTask extends AsyncTask<Integer,Void,String>  {


        @Override
        protected void onPostExecute(String s) {
            MainActivity.this.textView.setText(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String result = null;
            SoapObject request = new SoapObject(NAMESPACE, METHOD);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            //Creacion del transporte
            HttpTransportSE transport = new HttpTransportSE(URL);
            PropertyInfo dni = new PropertyInfo();
            dni.setName("NIF");
            dni.setValue(integers[0]);
            dni.setType(Integer.class);
            request.addProperty(dni);
            try {
                transport.call(SOAPACTION,envelope);
                SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                result = resultado.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
