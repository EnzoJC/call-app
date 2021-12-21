package com.example.callapp;

import static com.example.callapp.CallStateListenerImpl.formatPhoneNumber;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import java.util.Timer;
import java.util.TimerTask;

public class IncomingCallActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTelefono;
    private TextView tvContestarLlamada;
    private TextView tvColgarLlamada;
    private TextView tvCallingTime;

    private String phoneNumber;
    private CallManager callManager;
    private CallType callType;
    private Timer timer;
    private int duracion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        setShowWhenLocked(true);
        setTurnScreenOn(true);
        ActivityStack.getInstance().addActivity(this);
        initData();
        initView();
    }

    public static void actionStart(Context context, String phoneNumber, CallType callType) { 
        Intent intent = new Intent(context, IncomingCallActivity.class); 
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // FLAG_ACTIVITY_NEW_TASK: Esto es para que la actividad se abra en una nueva tarea. 
        intent.putExtra(Intent.EXTRA_MIME_TYPES, callType); // EXTRA_MIME_TYPES: Es una constante que indica el tipo de dato que se va a pasar.
        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, phoneNumber); // EXTRA_PHONE_NUMBER: Es una constante que indica el número de teléfono que se va a pasar.
        context.startActivity(intent);
    }

    private void initData() {
        callManager = new CallManager(this);
        timer = new Timer();
        if (getIntent() != null) {
            phoneNumber = getIntent().getStringExtra(Intent.EXTRA_PHONE_NUMBER); // EXTRA_PHONE_NUMBER: Es una constante que indica el número de teléfono que se va a pasar.
            callType = (CallType) getIntent().getSerializableExtra(Intent.EXTRA_MIME_TYPES); // EXTRA_MIME_TYPES: Es una constante que indica el tipo de dato que se va a pasar.
        }
    }
    private void initView() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        tvTelefono = findViewById(R.id.tv_telefono);
        tvContestarLlamada = findViewById(R.id.tv_contestar_llamada);
        tvColgarLlamada = findViewById(R.id.tv_colgar_llamada);
        tvCallingTime = findViewById(R.id.tv_duracion);

        tvTelefono.setText(formatPhoneNumber(phoneNumber));
        tvContestarLlamada.setOnClickListener(this);
        tvColgarLlamada.setOnClickListener(this);

        // Llamada entrante
        if (callType == CallType.CALL_IN) {
            tvContestarLlamada.setVisibility(View.VISIBLE);
        }
        // Llamada saliente
        else if (callType == CallType.CALL_OUT) {
            tvContestarLlamada.setVisibility(View.GONE);
            callManager.activarBocinaLlamada();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_contestar_llamada) {
            callManager.contestarLlamada();
            tvContestarLlamada.setVisibility(View.GONE);
            tvCallingTime.setVisibility(View.VISIBLE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
//                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            duracion++;
                            tvCallingTime.setText("Duración：" + getDuracion());
                        }
                    });
                }
            }, 0, 1000);
        } else if (view.getId() == R.id.tv_colgar_llamada) {
            callManager.colgarLlamada();
            stopTimer();
        }
    }

    private String getDuracion() {
        int minuto = duracion / 60;
        int segundo = duracion % 60;
        return (minuto < 10 ? "0" + minuto : minuto) + ":" + (segundo < 10 ? "0" + segundo : segundo);
    }

    private void stopTimer() {
        if (timer != null)
            timer.cancel();
        duracion = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callManager.destroy();
    }
}
