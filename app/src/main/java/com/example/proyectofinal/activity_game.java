package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class activity_game extends AppCompatActivity implements SensorEventListener {
    ImageView pelota;
    ImageView obst1, obst2, obst3, obst4;
    ImageView cuadrado1, cuadrado2, cuadrado3, cuadrado4;
    ImageView muro1, muro2, muro3, muro4;
    TextView texto, ganaste;
    private SensorManager sm;
    private Sensor s;
    private float xPosition = 0;
    private float yPosition = 0;
    private float accelerationFactor = 1f;
    private int segundos = 0;
    private int minutos = 0;
    private Handler handler = new Handler();
    private Runnable actualizarCronometro;
    Button volver;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference puntajeRef;
    private boolean puntajeGuardado = false; // Variable de estado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pelota = findViewById(R.id.imgPelota);
        cuadrado1 = findViewById(R.id.imgCuadra1);
        cuadrado2 = findViewById(R.id.imgCuadra2);
        cuadrado3 = findViewById(R.id.imgCuadra3);
        cuadrado4 = findViewById(R.id.imgCuadra4);

        obst1 = findViewById(R.id.imgObstaculo1);
        obst2 = findViewById(R.id.imgObstaculo2);
        obst3 = findViewById(R.id.imgObstaculo3);
        obst4 = findViewById(R.id.imgObstaculo4);

        muro1 = findViewById(R.id.imgMuro1);
        muro2 = findViewById(R.id.imgMuro2);
        muro3 = findViewById(R.id.imgMuro3);
        muro4 = findViewById(R.id.imgMuro4);

        texto = findViewById(R.id.txtTexto);
        ganaste = findViewById(R.id.txtGanaste);
        volver = findViewById(R.id.btnVolver);

        volver.setVisibility(View.INVISIBLE);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        inicializarFireBase();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        actualizarCronometro = new Runnable() {
            @Override
            public void run() {
                segundos++;

                if (segundos == 60) {
                    segundos = 0;
                    minutos++;
                }

                // Actualiza la vista con el tiempo transcurrido
                // Por ejemplo, muestra el tiempo en un TextView
                TextView cronometroTextView = findViewById(R.id.txtTexto);
                cronometroTextView.setText(String.format("%02d:%02d", minutos, segundos));

                handler.postDelayed(this, 1000); // Ejecuta el Runnable cada segundo
            }
        };

        handler.postDelayed(actualizarCronometro, 1000); // Inicia el cronómetro
    }
    private void inicializarFireBase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase =FirebaseDatabase.getInstance();
        puntajeRef =firebaseDatabase.getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAcceleration = event.values[0];
            float yAcceleration = event.values[1];

            // Mueve la imagen en función de la aceleración en los ejes X e Y
            xPosition -= xAcceleration * accelerationFactor;
            yPosition += yAcceleration * accelerationFactor;

            // Actualiza la posición de la ImageView
            pelota.setTranslationX(xPosition);
            pelota.setTranslationY(yPosition);

            int[] location1 = new int[2];
            pelota.getLocationOnScreen(location1);

            int[] location2 = new int[2];
            cuadrado1.getLocationOnScreen(location2);

            int[] location3 = new int[2];
            cuadrado2.getLocationOnScreen(location3);

            int[] location4 = new int[2];
            cuadrado3.getLocationOnScreen(location4);

            int[] location5 = new int[2];
            cuadrado4.getLocationOnScreen(location5);

            int[] location6 = new int[2];
            obst1.getLocationOnScreen(location6);

            int[] location7 = new int[2];
            obst2.getLocationOnScreen(location7);

            int[] location8 = new int[2];
            obst3.getLocationOnScreen(location8);

            int[] location9 = new int[2];
            obst4.getLocationOnScreen(location9);

            int[] location10 = new int[2];
            muro1.getLocationOnScreen(location10);

            int[] location11 = new int[2];
            muro2.getLocationOnScreen(location11);

            int[] location12 = new int[2];
            muro3.getLocationOnScreen(location12);

            int[] location13 = new int[2];
            muro4.getLocationOnScreen(location13);

            int left1 = location1[0];
            int top1 = location1[1];
            int right1 = left1 + pelota.getWidth();
            int bottom1 = top1 + pelota.getHeight();

            int left2 = location2[0];
            int top2 = location2[1];
            int right2 = left2 + cuadrado1.getWidth();
            int bottom2 = top2 + cuadrado1.getHeight();

            int left3 = location3[0];
            int top3 = location3[1];
            int right3 = left3 + cuadrado2.getWidth();
            int bottom3 = top3 + cuadrado2.getHeight();

            int left4 = location4[0];
            int top4 = location4[1];
            int right4 = left4 + cuadrado3.getWidth();
            int bottom4 = top4 + cuadrado3.getHeight();

            int left5 = location5[0];
            int top5 = location5[1];
            int right5 = left5 + cuadrado4.getWidth();
            int bottom5 = top5 + cuadrado4.getHeight();

            int left6 = location6[0];
            int top6 = location6[1];
            int right6 = left6 + obst1.getWidth();
            int bottom6 = top6 + obst1.getHeight();

            int left7 = location7[0];
            int top7 = location7[1];
            int right7 = left7 + obst2.getWidth();
            int bottom7 = top7 + obst2.getHeight();

            int left8 = location8[0];
            int top8 = location8[1];
            int right8 = left8 + obst3.getWidth();
            int bottom8 = top8 + obst3.getHeight();

            int left9 = location9[0];
            int top9 = location9[1];
            int right9 = left9 + obst4.getWidth();
            int bottom9 = top9 + obst4.getHeight();

            int left10 = location10[0];
            int top10 = location10[1];
            int right10 = left10 + muro1.getWidth();
            int bottom10 = top10 + muro1.getHeight();

            int left11 = location11[0];
            int top11 = location11[1];
            int right11 = left11 + muro2.getWidth();
            int bottom11 = top11 + muro2.getHeight();

            int left12 = location12[0];
            int top12 = location12[1];
            int right12 = left12 + muro3.getWidth();
            int bottom12 = top12 + muro3.getHeight();

            int left13 = location13[0];
            int top13 = location13[1];
            int right13 = left13 + muro4.getWidth();
            int bottom13 = top13 + muro4.getHeight();

            if (left1 < right2 && right1 > left2 && top1 < bottom2 && bottom1 > top2) {
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Ganaste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right3 && right1 > left3 && top1 < bottom3 && bottom1 > top3){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Ganaste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right4 && right1 > left4 && top1 < bottom4 && bottom1 > top4){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Ganaste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right5 && right1 > left5 && top1 < bottom5 && bottom1 > top5){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Ganaste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right6 && right1 > left6 && top1 < bottom6 && bottom1 > top6){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right7 && right1 > left7 && top1 < bottom7 && bottom1 > top7){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right8 && right1 > left8 && top1 < bottom8 && bottom1 > top8){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right9 && right1 > left9 && top1 < bottom9 && bottom1 > top9){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right10 && right1 > left10 && top1 < bottom10 && bottom1 > top10){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right11 && right1 > left11 && top1 < bottom11 && bottom1 > top11){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right12 && right1 > left12 && top1 < bottom12 && bottom1 > top12){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
            if(left1 < right13 && right1 > left13 && top1 < bottom13 && bottom1 > top13){
                handler.removeCallbacks(actualizarCronometro); // Detiene el cronómetro
                ganaste.setText("Perdiste");
                volver.setVisibility(View.VISIBLE);
                pelota.setVisibility(View.INVISIBLE);
                puntajeRef = FirebaseDatabase.getInstance().getReference("puntaje");
                puntajeRef.setValue(segundos + minutos * 60);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}