package es.cice.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String SOAP_ACTION = "http://ws.cice.es/WSAndroid/WebService1";
    private static final String METHOD_CONVERT_TO_EURO = "convertToEuro";
    private static final String METHOD_CONVERT_TO_OTHER = "convertToOther";
    private static final String NAMESPACE = "http://ws.cice.es/";
    private static final String URL = "http://10.0.2.2:8084/WSAndroid/WebService1?wsdl";

    private ListView listView;

    private Button convertButton;
    private RadioGroup radioGroup;

    private TextView resultTextView;
    private EditText quantityEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        listView = (ListView) findViewById(R.id.currencyListView);
        listView.setAdapter(ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_list_item_single_choice));
        convertButton = (Button) findViewById(R.id.convertButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        quantityEditText = (EditText) findViewById(R.id.quantityEditText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String method = null;
               MyAsyncTask myAsyncTask = new MyAsyncTask();
               int checkedRadioButtonId = SecondActivity.this.radioGroup.getCheckedRadioButtonId();
               Toast.makeText(SecondActivity.this,"" + listView.getItemAtPosition(listView.getCheckedItemPosition()) ,Toast.LENGTH_SHORT ).show();

               if(checkedRadioButtonId == R.id.toEuroRadioButton) {
                   method = METHOD_CONVERT_TO_EURO;
               }
               else {
                   method = METHOD_CONVERT_TO_OTHER;
               }
                myAsyncTask.execute(quantityEditText.getText().toString(),"EUR",method);
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Object,Void,Double> {


        @Override
        protected void onPostExecute(Double value) {
            super.onPostExecute(value);
            SecondActivity.this.resultTextView.setText(String.valueOf(value));
        }

        @Override
        protected Double doInBackground(Object... params) {
            Double result = null;

            SoapObject request = new SoapObject(NAMESPACE, (String) params[2]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            //Creacion del transporte
            HttpTransportSE transport = new HttpTransportSE(URL);
            PropertyInfo quantity = new PropertyInfo();
            quantity.setName("quantity");
            quantity.setValue(params[0]);
            quantity.setType(Double.class);

            PropertyInfo currency = new PropertyInfo();
            currency.setName("currency");
            currency.setValue(params[1]);
            currency.setType(String.class);
            request.addProperty(quantity);
            request.addProperty(currency);
            try {
                transport.call(SOAP_ACTION,envelope);
                SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                result = new Double(resultado.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

}
