package com.example.mislugares;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	public BaseAdapter adaptador;
	public MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adaptador = new AdaptadorLugares(this);
		setListAdapter(adaptador);

		mp = MediaPlayer.create(this, R.raw.audio);

		/*
		 * boton_acercaDe = (Button) findViewById(R.id.botonAcercaDe);
		 * boton_salir = (Button) findViewById(R.id.botonSalir);
		 * 
		 * boton_acercaDe.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View view) {
		 * 
		 * lanzarAcercaDe(null);
		 * 
		 * }
		 * 
		 * });
		 * 
		 * boton_salir.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub finish(); } });
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	public void lanzarPreferencias(View view) {
		Intent i = new Intent(this, Preferencias.class);
		startActivity(i);
	}

	public void salir(View view) {
		finish();
	}

	// Solo necesario para crear menu que se activa pulsando el boton fisico de
	// menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.acercaDe:
			lanzarAcercaDe(null);
			break;

		case R.id.config:
			lanzarPreferencias(null);
			break;
		}
		return true;
	}

	public void mostrarPreferencias(View view) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String s = "notificaciones: " + pref.getBoolean("notificaciones", true)
				+ ", distancia minima: " + pref.getString("distancia", "?");
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	/*
	 * public void lanzarVistaLugar(View view) { final EditText entrada = new
	 * EditText(this); entrada.setText("0"); new
	 * AlertDialog.Builder(this).setTitle("Seleccion de lugar")
	 * .setMessage("indica su id:").setView(entrada) .setPositiveButton("Ok",
	 * new DialogInterface.OnClickListener() { public void
	 * onClick(DialogInterface dialog, int whichButton) { long id =
	 * Long.parseLong(entrada.getText().toString()); Intent i = new
	 * Intent(MainActivity.this, VistaLugar.class); i.putExtra("id", id);
	 * startActivity(i); } }).setNegativeButton("Cancelar", null).show(); }
	 */

	@Override
	protected void onListItemClick(ListView listView, View vista, int posicion,
			long id) {
		super.onListItemClick(listView, vista, posicion, id);
		Intent intent = new Intent(this, VistaLugar.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {

		mp.start();
		super.onResume();
		Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

		mp.pause();
		super.onPause();
	}

	@Override
	protected void onStop() {
		mp.pause();
		super.onStop();
		Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle estadoGuardado) {
		super.onSaveInstanceState(estadoGuardado);
		if (mp != null) {
			int pos = mp.getCurrentPosition();
			estadoGuardado.putInt("posicion", pos);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle estadoGuardado) {
		super.onRestoreInstanceState(estadoGuardado);
		if (estadoGuardado != null && mp != null) {
			int pos = estadoGuardado.getInt("posicion");
			mp.seekTo(pos);
		}
	}
}
