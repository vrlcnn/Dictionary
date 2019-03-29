package com.work.dictionary;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameEt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init() {
        userNameEt = findViewById(R.id.kullanici_adi_et_id);
        loginBtn = findViewById(R.id.login_btn_id);
        loginBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == loginBtn){
            String userName = userNameEt.getText().toString().trim();
            userControl(userName);
        }
    }
    private void userControl(final String word) {
        if(word.isEmpty()){
            Toast.makeText(this, "Please fill it!", Toast.LENGTH_SHORT).show();
        }
        else{
            AndroidNetworking.post("http://bilimtadinda.com/cankasoft/aranacak_kelime/servis.php")
                    .addBodyParameter("word", word)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String  response) {
                            Log.i("LoginActivity",response);
                            cevapaBak(response);
                        }
                        @Override
                        public void onError(ANError error) {
                            Log.e("LoginActivity",error.getMessage());
                        }
                    });
        }
    }
    private void cevapaBak(String response) {
        Gson gson = new Gson();
        ResponseModel responseModel = gson.fromJson(response,ResponseModel.class);

        if(responseModel.getSonuc() == 1){

            Bundle bundle;
            bundle = responseModel.getMesaj();

            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "\"Failed\"", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
