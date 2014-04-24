package com.example.mislugares;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class VistaLugar extends Activity {
	private long id;
	private Lugar lugar;
	private boolean borrar;
	private ImageView imageView;
	final static int RESULTADO_EDITAR = 1;
	final static int RESULTADO_GALERIA = 2;
	final static int RESULTADO_FOTO = 3;
	private Uri uriFoto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.vista_lugar);
		Bundle extras = getIntent().getExtras();

		id = extras.getLong("id", -1);
		lugar = Lugares.elemento((int) id);

		imageView = (ImageView) findViewById(R.id.foto);
		actualizarVistas();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.vista_lugar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.accion_compartir:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, lugar.getNombre() + " - "
					+ lugar.getUrl());
			startActivity(intent);

			return true;

		case R.id.accion_llegar:
			verMapa(null);
			return true;

		case R.id.accion_editar:
			Log.i("Editar", "Iniciando el intent");
			lanzarEditarLugar(null);

			return true;

		case R.id.accion_borrar:
			lanzarBorrarLugar(null);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void lanzarBorrarLugar(View view) {

		Intent intent = new Intent(this, BorrarLugar.class);
		intent.putExtra("borrar", borrar);
		startActivityForResult(intent, 1);
	}

	private void lanzarEditarLugar(View view) {
		Intent i = new Intent(this, EdicionLugar.class);
		i.putExtra("id", id);
		startActivityForResult(i, RESULTADO_EDITAR);
		Log.i("Editar", "Intent iniciado");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			borrar = data.getExtras().getBoolean("borrar");
			if (borrar) {
				Lugares.borrar((int) id);
				finish();
			}
		}

		if (requestCode == RESULTADO_EDITAR) {
			actualizarVistas();
			findViewById(R.id.scrollView1).invalidate();
		}

		else if (requestCode == RESULTADO_GALERIA
				&& resultCode == Activity.RESULT_OK) {
			lugar.setFoto(data.getDataString());
			ponerFoto(imageView, lugar.getFoto());
		}

		else if (requestCode == RESULTADO_FOTO
				&& resultCode == Activity.RESULT_OK && lugar != null
				&& uriFoto != null) {
			lugar.setFoto(uriFoto.toString());
			ponerFoto(imageView, lugar.getFoto());
		}
	}

	public void actualizarVistas() {

		TextView nombre = (TextView) findViewById(R.id.nombre);
		nombre.setText(lugar.getNombre());

		ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
		logo_tipo.setImageResource(lugar.getTipo().getRecurso());

		TextView tipo = (TextView) findViewById(R.id.tipo);
		tipo.setText(lugar.getTipo().getTexto());

		// Tratamos el caso de que la direccion este vacia
		if (lugar.getDireccion().isEmpty()) {
			findViewById(R.id.direccion).setVisibility(View.GONE);
		} else {
			findViewById(R.id.direccion).setVisibility(View.VISIBLE);
			TextView direccion = (TextView) findViewById(R.id.direccion);
			direccion.setText(lugar.getDireccion());
		}

		// Tratamos el caso de que el telefono este vacio
		if (lugar.getTelefono() == 0) {
			findViewById(R.id.telefono).setVisibility(View.GONE);
		}

		else {
			findViewById(R.id.telefono).setVisibility(View.VISIBLE);
			TextView telefono = (TextView) findViewById(R.id.telefono);
			telefono.setText(Integer.toString(lugar.getTelefono()));
		}

		// Tratamos el caso de que la url este vacia
		if (lugar.getUrl().isEmpty()) {
			findViewById(R.id.url).setVisibility(View.GONE);
		}

		else {
			findViewById(R.id.url).setVisibility(View.VISIBLE);
			TextView url = (TextView) findViewById(R.id.url);
			url.setText(lugar.getUrl());
		}

		// Tratamos el caso de que el comentario este vacio
		if (lugar.getComentario().isEmpty()) {
			findViewById(R.id.comentario).setVisibility(View.GONE);
		}

		else {
			findViewById(R.id.comentario).setVisibility(View.VISIBLE);
			TextView comentario = (TextView) findViewById(R.id.comentario);
			comentario.setText(lugar.getComentario());
		}

		TextView fecha = (TextView) findViewById(R.id.fecha);
		fecha.setText(DateFormat.getDateInstance().format(
				new Date(lugar.getFecha())));

		TextView hora = (TextView) findViewById(R.id.hora);
		hora.setText(DateFormat.getTimeInstance().format(
				new Date(lugar.getFecha())));

		RatingBar valoracion = (RatingBar) findViewById(R.id.valoracion);
		valoracion.setRating(lugar.getValoracion());
		valoracion.setOnRatingBarChangeListener(

		new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float valor,
					boolean fromUser) {
				lugar.setValoracion(valor);
			}
		});
		ponerFoto(imageView, lugar.getFoto());
	}

	public void verMapa(View view) {
		Uri uri;
		double lat = lugar.getPosicion().getLatitud();
		double lon = lugar.getPosicion().getLongitud();
		if (lat != 0 || lon != 0) {
			uri = Uri.parse("geo:" + lat + "," + lon);
		} else {
			uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
		}
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	public void llamadaTelefono(View view) {
		startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ lugar.getTelefono())));
	}

	public void pgWeb(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
	}

	public void galeria(View view) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, RESULTADO_GALERIA);
	}

	protected void ponerFoto(ImageView imageView, String uri) {
		if (uri != null) {
			imageView.setImageURI(Uri.parse(uri));
		} else {
			imageView.setImageBitmap(null);
		}
	}

	public void tomarFoto(View view) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		uriFoto = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory()
				+ File.separator
				+ "img_"
				+ (System.currentTimeMillis() / 1000) + ".jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
		startActivityForResult(intent, RESULTADO_FOTO);
	}

	public void eliminarFoto(View view) {
		lugar.setFoto(null);
		ponerFoto(imageView, null);
	}
	
	
}
