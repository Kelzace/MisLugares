package com.example.mislugares;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BorrarLugar extends Activity {
	private Button cancelar, confirmar;
	private Context context;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrar_lugar);
		confirmar = (Button) findViewById(R.id.boton_confirmar_borrado);
		cancelar = (Button) findViewById(R.id.boton_cancelar_borrado);
		context = this;

		confirmar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, VistaLugar.class);
				intent.putExtra("borrar", true);
				setResult(RESULT_OK, intent);
				finish();

			}
		});

		cancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, VistaLugar.class);
				intent.putExtra("borrar", false);
				setResult(RESULT_OK, intent);
				finish();

			}
		});

	}

}
