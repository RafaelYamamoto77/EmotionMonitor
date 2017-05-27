package com.example.emotionmonitor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SolidaoMonitor extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private TextView textView3;
    private TextView Cont;
    private LineChart CHART;
    public int[] AR; //array containing data history
    public int AR_L; //array length
    public int progressP;
    private int i , j;

    private void initializeVariables() {
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        textView = (TextView) findViewById(R.id.textView1);
        textView3 = (TextView) findViewById(R.id.textView3);
        Cont = (TextView) findViewById(R.id.textView4);
        CHART = (LineChart) findViewById(R.id.chart);
        progressP = 0;
        i = 0;
        j = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solidao_monitor);
        Intent intent = getIntent();
        setTitle(R.string.Solidao);
        initializeVariables(); //inicia as variáveis.
        readData(); //inicia o array.
        if(AR_L > 1) CreateChart();
        textView.setText("Covered: " + seekBar.getProgress() + "/" + seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                progressP = progress;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Nivel de Solidao: " + progress);
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void readData(){
        try { //tenta ler do arquivo
            Read();
            //ShowFileContent();
        } catch (IOException e) {
            AR_L = 1; //por padrão o tamanho do array é 1
            AR = new int[AR_L]; //definição do array com tamanho 1
            try {
                Write(1); //tenta fazer a primeira gravação de arquivo
                AR_L++;
                Write(1); //grava 0 no primeiro registro
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        //textView3.setText(String.valueOf(AR_L));
    }

    public void showArray(View view) {
        if( j == AR_L ) j = 0;
        textView3.setText(String.valueOf(AR[j]));
        j++;
    }

    public void Write(int K) throws IOException {
        int tempAR[] = new int[AR_L];
        tempAR[0] = AR_L;
        FileOutputStream fOut = openFileOutput("Solidao_FILE",Context.MODE_PRIVATE);
        String str = String.valueOf(AR_L) + "+"; //numeros são separados por '+'
        for(i = 1; i < AR_L-1; i++){
            tempAR[i] = AR[i];
            str = str + String.valueOf(AR[i]) + "+";
        }
        str = str + String.valueOf(progressP) + "+";
        tempAR[AR_L-1] = progressP;
        AR = tempAR;
        fOut.write(str.getBytes());
        fOut.close();
    }

    public void Read() throws IOException {
        FileInputStream fin = openFileInput("Solidao_FILE");
        int c;
        String temp="";
        while( (c = fin.read()) != -1 && (char) c != '+'){ //encontra o primeiro valor inteiro
            temp = temp + Character.toString((char)c);
        }
        AR_L = Integer.valueOf(temp); //o primeiro valor inteiro é o tamanho do array
        AR = new int[AR_L]; //o array assume o tamanho AR_L
        AR[0] = AR_L; //o primeiro valor do array assume o valor AR_L
        int i = 1; //indice do array se inicia em um
        temp="";
        while( (c = fin.read()) != -1){
            if((char) c == '+'){ //o sinal + simboliza o final de um inteiro e o inicio de outro ou final dos numeros
                AR[i] = Integer.valueOf(temp); //o campo no array recebe o valor da string
                i++; //incrementa o indice
                temp=""; //reseta a string temporaria
                continue; //pula o passo do +, já que ele não sera aproveitado
            }
            temp = temp + Character.toString((char)c); //junta os caracteres que formam os numeros
        }
        //textView3.setText("leitura de:" + String.valueOf(i) + "numeros, feita com sucesso!");
        fin.close();
    }

    public void ShowDir(View view){
        File F = getFilesDir();
        textView3.setText(F.getAbsolutePath());
    }

    public void ShowFiles(View view){
        File F = getFilesDir();
        String S[] = fileList();
        int LIST_S = S.length;
        if( i == LIST_S ) i = 0;
        textView3.setText(S[i]);
        i++;
    }

    public void ShowFileContent() throws IOException {
        FileInputStream fin = openFileInput("Solidao_FILE");
        int c;
        String temp="";
        while( (c = fin.read()) != -1){ //encontra o primeiro valor inteiro
            temp = temp + Character.toString((char)c);
        }
        //Cont.setText(temp);
        fin.close();
    }

    public void SaveData(View view){
        try {
            AR_L++;
            Write(progressP);
        } catch (IOException e) {
            AR_L--;
            textView3.setText("Falha na escrita");
            e.printStackTrace();
        }
        try {
            Read(); //le o arquivo novamente para atualizar o array
            ShowFileContent();
        } catch (IOException e) {
            textView3.setText("Falha na leitura");
            e.printStackTrace();
        }
        CreateChart();
    }

    private void CreateChart(){ //cria o gráfico na view
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 1; i < AR_L; i++){
            entries.add(new Entry(i, AR[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Histórico de Solidão");
        dataSet.setColors(2);
        LineData lineData = new LineData(dataSet);

        dataSet.setDrawFilled(true);
        YAxis left = CHART.getAxisLeft();
        left.setDrawLabels(false);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true);
        CHART.setData(lineData);
        CHART.invalidate();

    }

}
