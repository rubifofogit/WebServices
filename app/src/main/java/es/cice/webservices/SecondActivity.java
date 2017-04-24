package es.cice.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static javax.xml.transform.OutputKeys.METHOD;

public class SecondActivity extends AppCompatActivity {

    private static final String SOAPACTION = "http://ws.cice.es/WSAndroid/WebService1";
    private static final String METHOD = "getLetraNIF";
    private static final String NAMESPACE = "http://ws.cice.es/";
    private static final String URL = "http://10.0.2.2:8084/WSAndroid/WebService1?wsdl";

    private Button convertButton;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        convertButton = (Button) findViewById(R.id.convertButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int checkedRadioButtonId = SecondActivity.this.radioGroup.getCheckedRadioButtonId();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Integer,Void,String> {


        @Override
        protected void onPostExecute(String s) {

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
