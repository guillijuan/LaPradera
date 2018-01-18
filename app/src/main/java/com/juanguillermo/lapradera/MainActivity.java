package com.juanguillermo.lapradera;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.juanguillermo.lapradera.Objetos.FirebaseReferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button botonIniciar, botonRegistrar;
    EditText editTextUsuario, editTextContrasena;

    FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference turnosRef = database.getReference(FirebaseReferences.FINCA_REFERENCE);



        botonIniciar = (Button) findViewById(R.id.btn_inicio);
        botonRegistrar = (Button) findViewById(R.id.btn_registrar);
        editTextUsuario = (EditText) findViewById(R.id.et_usuario);
        editTextContrasena = (EditText) findViewById(R.id.et_pass);

        botonIniciar.setOnClickListener(this);

        botonRegistrar.setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.i("SESION", "Sesion iniciada con usuario " + user.getEmail());
                }else{
                    Log.i("SESION", "Sesion cerrada");
                }

            }
        };

    }

    private void Registrar(String usuario, String contrasena){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Log.i("SESION", "Usuario creado correctamente");
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Usuario creado correctamente", Toast.LENGTH_SHORT);
                    toast1.show();


                }else {

                    Log.e("SESION", task.getException().getMessage()+"");

                    Toast toast2 =
                            Toast.makeText(getApplicationContext(),
                                    "Error /n" +task.getException(), Toast.LENGTH_SHORT);
                    toast2.show();
                }
            }
        });
    }

    private void iniciarSesion (String usuario, String contrasena){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Log.i("SESION", "Sesion iniciada correctamente");

                    Intent ensayo = new Intent(getApplicationContext(), Activity_Menu.class);
                    startActivity(ensayo);

             /*       Intent inicio = new Intent(MainActivity.this, SegundoActivity.class);
                    startActivity(inicio);
*/

                }else {
                    Log.e("SESION", task.getException().getMessage()+"");

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Error /n" +task.getException(), Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_inicio:
                String usuario = editTextUsuario.getText().toString();
                String contrasena = editTextContrasena.getText().toString();
                iniciarSesion(usuario, contrasena);
                break;
            case R.id.btn_registrar:
                String usuarioReg = editTextUsuario.getText().toString();
                String contrasenaReg = editTextContrasena.getText().toString();
                Registrar(usuarioReg, contrasenaReg);
                break;
        }
    }

    protected void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    protected void onStop(){
        super.onStop();
        if (mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
