package com.example.mislugares;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class EdicionLugar extends Activity {
	private long id;
	private Lugar lugar;
	private EditText nombre;
	private Spinner tipo;
	private EditText direccion;
	private EditText telefono;
	private EditText url;
	private EditText comentario;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Editar", "Antes de setContentView");
		setContentView(R.layout.edicion_lugar);

		Log.i("Editar", "Despues del setContentView");
		Bundle extras = getIntent().getExtras();

		Log.i("Editar", "Despues del getExtras");

		id = extras.getLong("id", -1);
		Log.i("Editar", "Despues del id");

		lugar = Lugares.elemento((int) id);
		Log.i("Editar", "Despues del lugar");

		Log.i("Editar", "Antes de iniciar variables");

		nombre = (EditText) findViewById(R.id.editNombre);
		nombre.setText(lugar.getNombre());

		Log.i("Editar", "Iniciada la variable nombre");

		tipo = (Spinner) findViewById(R.id.editTipo);
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, TipoLugar.getNombres());

		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tipo.setAdapter(adaptador);
		tipo.setSelection(lugar.getTipo().ordinal());

		Log.i("Editar", "Iniciado el Spinner");

		Log.i("Editar", "Fallo en direccion?");
		direccion = (EditText) findViewById(R.id.editDireccion);
		direccion.setText(lugar.getDireccion());
		

		Log.i("Editar", "Iniciada la direccion");

		// Tratamos el caso de que el telefono este vacio
		telefono = (EditText) findViewById(R.id.editTelefono);
		telefono.setText(Integer.toString(lugar.getTelefono()));
		

		Log.i("Editar", "Iniciado el telefono");

		// Tratamos el caso de que la url este vacia
		url = (EditText) findViewById(R.id.editUrl);
		url.setText(lugar.getUrl());
		

		Log.i("Editar", "Iniciada la url");

		comentario = (EditText) findViewById(R.id.editComentario);
		comentario.setText(lugar.getComentario());
		

		Log.i("Editar", "Iniciado el comentario");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edicion_lugar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.editar_cancelar:
			Log.i("BotonEditar","Boton cancelar pulsado");
			finish();
			return true;

		case R.id.editar_guardar:
			lugar.setNombre(nombre.getText().toString());
			lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
			lugar.setDireccion(direccion.getText().toString());
			lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
			lugar.setUrl(url.getText().toString());
			lugar.setComentario(comentario.getText().toString());
			Log.i("BotonEditar","Boton guardar pulsado");
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
