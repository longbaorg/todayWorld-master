package com.example.todayworld;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
	public TextView res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		res=(TextView)this.findViewById(R.id.nickname);
		Intent intent= getIntent();
		String resss= intent.getStringExtra("nickname");

		Toast.makeText(Main2Activity.this,resss,Toast.LENGTH_SHORT).show();
		res.setText(res.getText()+resss);
	}
}
