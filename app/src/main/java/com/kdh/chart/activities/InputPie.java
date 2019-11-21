package com.kdh.chart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.kdh.chart.R;

public class InputPie extends AppCompatActivity {

    private Button btnCreateChart;
    private EditText edtChartName;
    private EditText edtNumberOfField;
    private EditText edtFieldtName;
    private EditText edtValue;
    public static final String CHART_NAME = "name";
    public static final String NUMBER_OF_FIELD = "Number of field";
    public static final String FIELD_NAME = "Field name";
    public static final String VALUE = "Value";
    public static final String BUNDLE = "bundel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pie);
        setTitle("Input Data");

        btnCreateChart=findViewById(R.id.btnCreateChart);
        edtChartName=findViewById(R.id.edtChartName);
        edtNumberOfField=findViewById(R.id.edtNumberOfField);
        edtFieldtName=findViewById(R.id.edtFieldName);
        edtValue=findViewById(R.id.edtValue);

        btnCreateChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtChartName.getText()+""!="" && edtNumberOfField.getText()+""!="" &&edtFieldtName.getText()+""!="" &&edtValue.getText()+""!="")
                    sendData();
            }
        });
    }

    public void sendData() {
        Intent intent = new Intent(InputPie.this, ShowChartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CHART_NAME, edtChartName.getText().toString());
        bundle.putString(NUMBER_OF_FIELD, edtNumberOfField.getText().toString());
        bundle.putString(FIELD_NAME, edtFieldtName.getText().toString());
        bundle.putString(VALUE, edtValue.getText().toString());
        intent.putExtra(BUNDLE, bundle);
        startActivity(intent);

    }
}
