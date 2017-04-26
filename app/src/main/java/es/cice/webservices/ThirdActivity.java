package es.cice.webservices;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ThirdActivity extends AppCompatActivity {


    private static final String SOAP_ACTION = "\"http://ws.cice.es/HospitalWS/DoctorWS\"";
    private static final String METHOD_INSERT= "insertarDoctor";
    private static final String NAMESPACE = "http://ws.hospital.cice.es/";
    private static final String URL = "http://10.0.2.2:8084/HospitalWS/DoctorWS?wsdl";

    private EditText apellidoEditText;
    private EditText especialidadEditText;
    private EditText salarioEditText;

    private Button insertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        apellidoEditText = (EditText) findViewById(R.id.apellidoEditText);
        especialidadEditText = (EditText) findViewById(R.id.especialidadEditText);
        salarioEditText = (EditText) findViewById(R.id.salarioEditText);
        insertButton = (Button) findViewById(R.id.insertButton);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(apellidoEditText.getText().toString(),
                                    especialidadEditText.getText().toString(),
                                    salarioEditText.getText().toString());
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<String,Void,Void> {


        @Override
        protected void onPostExecute(Void value) {
            super.onPostExecute(value);
            Toast.makeText(ThirdActivity.this,"Executed",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(String... params) {
            Void result = null;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_INSERT);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            //Creacion del transporte
            HttpTransportSE transport = new HttpTransportSE(URL);
            PropertyInfo apellido = new PropertyInfo();
            apellido.setName("apellido");
            apellido.setValue(params[0]);
            apellido.setType(String.class);

            PropertyInfo especialidad = new PropertyInfo();
            especialidad.setName("especialidad");
            especialidad.setValue(params[1]);
            especialidad.setType(String.class);



            PropertyInfo salario = new PropertyInfo();
            salario.setName("salario");
            salario.setValue(Integer.valueOf(params[2]));

            request.addProperty(apellido);
            request.addProperty(especialidad);
            request.addProperty(salario);
            try {
                transport.call(SOAP_ACTION,envelope);
                SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
