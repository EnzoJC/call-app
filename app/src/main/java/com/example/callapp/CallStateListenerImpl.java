package com.example.callapp;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CallStateListenerImpl extends PhoneStateListener {
    private View phoneCallView;
    private TextView tvCallNumber;
    private Button btnOpenApp;

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    private boolean hasShown;
    private boolean isCallingIn;


    private String callNumber;
    private static final String TAG = CallStateListenerImpl.class.getName();

    @Override
    public void onCallStateChanged(int state, String phoneNumber) { // state: 0: IDLE, 1: OFFHOOK, 2: RINGING
        super.onCallStateChanged(state, phoneNumber); // Llama al método de la clase padre (PhoneStateListener)
        callNumber = phoneNumber;
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE: // IDLE: No hay llamada
                // El evento CALL_STATE_IDLE se produce cuando la llamada se ha terminado.
                dismiss();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: // llamada en curso
                // El evento CALL_STATE_OFFHOOK se produce cuando la llamada se ha iniciado.
                Log.d(TAG, "CallStateListenerImpl: CALL_STATE_OFFHOOK");
                isCallingIn = true;
                
                break;
            case TelephonyManager.CALL_STATE_RINGING: // llamada entrante
                // El evento CALL_STATE_RINGING se produce cuando una llamada entrante se está iniciando.
                Log.d(TAG, "CallStateListenerImpl: CALL_STATE_RINGING");
                break;
        }
    }

    private void show() { // Muestra la vista de la llamada
        if (!hasShown) { // Si no se ha mostrado la vista de la llamada
            windowManager.removeView(phoneCallView); // Elimina la vista de la llamada anterior para que no se superpongan
            hasShown = true; // Indica que se ha mostrado la vista de la llamada anterior
        }
    }

    private void dismiss() { // Oculta la vista de la llamada
        if (hasShown) { // Si se ha mostrado la vista de la llamada
            windowManager.removeView(phoneCallView); // Elimina la vista de la llamada anterior para que no se superpongan
            hasShown = false; // Indica que no se ha mostrado la vista de la llamada anterior
        }
    }

//    private void updateUI() { // Actualiza la vista de la llamada
//        tvCallNumber.setText(formatPhoneNumber(callNumber)); // Actualiza el número de la llamada
//        int callTypeDrawable = isCallingIn ? R.drawable.ic_call_in : R.drawable.ic_call_out; // Obtiene el recurso de la imagen de la llamada
//        tvCallNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(callTypeDrawable), null); // Actualiza el icono de la llamada (entrante o saliente) y el número de la llamada (entrante o saliente) en la vista de la llamada
//    }

    public static String formatPhoneNumber(String phoneNum) {
        // Este metodo permite agregar espacio en blanco al número de teléfono cada tres digitos
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNum.length(); i++) {
            if (i % 3 == 0 && i != 0) {
                sb.append(" ");
            }
            sb.append(phoneNum.charAt(i));
        }
        return sb.toString();
    }
}
