package com.example.appvoz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText tMsg;
    private TextToSpeech textFala;
    private Button btFalar, btOuvir;
    private ListView lstV;
    private Locale locale;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textFala = new TextToSpeech((MainActivity.this) ,MainActivity.this);
        tMsg = (EditText)findViewById(R.id.edt);
        botoes();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuaction, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int i = item.getItemId();
        {
            if (i == R.id.action_portugues){
                locale = new Locale ("pt", "BR");
                textFala.setLanguage(locale);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onInit (int status) {
        textFala.setLanguage(Locale.ENGLISH);
    }
    private void botoes(){
        btFalar = (Button) findViewById(R.id.btfalar);
        btFalar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String texto = tMsg.getText().toString();
                textFala.speak(texto, TextToSpeech.QUEUE_FLUSH, Bundle.EMPTY, "1");
            }
        });
        btOuvir = (Button) findViewById(R.id.btouvir);
        btOuvir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                lstV = (ListView)findViewById(R.id.listview);
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
                if (activities.size() != 0){
                    Intent intent = getRecognizerIntent();
                    startActivityForResult(intent, 0);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sem reconhecimento de voz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override

    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            ArrayList<String> palavras = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lstV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, palavras));
        }
    }
    protected  Intent getRecognizerIntent () {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale Aqui!");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        return intent;
    }

}
