package com.example.ordinario

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ordinario.databinding.ActivityAgregarUsuarioBinding

class agregarUsuario : AppCompatActivity() {
    private lateinit var binding:ActivityAgregarUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val btnAgregar = binding.btnAgregarUsuario
        val btnCancelar = binding.btnCancelar

        if(intent.getStringExtra("mensaje").toString() == "edit"){
            binding.txtNombre.setText("${intent.getStringExtra("nombre").toString()}")
            binding.txtAgregarCorreo.setText("${intent.getStringExtra("correo").toString()}")
            binding.txtAgregarPassword.setText("${intent.getStringExtra("password").toString()}")
            binding.txvUsuario.setText("Actualización de Usuario")
            binding.btnAgregarUsuario.setText("Actulizar")
        }


        btnAgregar.setOnClickListener({
            //creamos la conexion a la DB
            val dbusuarios = DBHelperUsuarios(this)
            var db = dbusuarios.writableDatabase


            //Pasamos los valores de los editText a variables
            val txtNom = binding.txtNombre.text
            val txtCorr = binding.txtAgregarCorreo.text
            val txtPassword = binding.txtAgregarPassword.text

            //para actualizar
            if(intent.getStringExtra("mensaje").toString() == "edit"){
                if (txtNom.isNotEmpty() && txtCorr.isNotEmpty() && txtPassword.isNotEmpty()) {
                    val valores = ContentValues()
                    valores.put("nombre", txtNom.toString())
                    valores.put("correo", txtCorr.toString())

                    if(intent.getStringExtra("password").toString() != txtPassword.toString()){
                        valores.put("password", txtPassword.toString())
                    }
                    val id = intent.getStringExtra("id").toString()
                    val parametros = arrayOf(id)
                    //Insetamos el Registro
                    val res = db.update("usuarios", valores, "id = ?",parametros)
                    db.close()

                    if (res == -1) {
                        Toast.makeText(this, "No se actualizo el registro", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Se actualizo el registro", Toast.LENGTH_LONG).show()
                        binding.txtNombre.text.clear()
                        binding.txtAgregarCorreo.text.clear()
                        binding.txtAgregarPassword.text.clear()
                        var intent = Intent(this, pantallaAdministrador::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "No puede quedar vacio ningún campo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else {
                //para agregar
                //Pasamos los valores de las cajas a una variable contentValues
                if (txtNom.isNotEmpty() && txtCorr.isNotEmpty() && txtPassword.isNotEmpty()) {
                    val newReg = ContentValues()
                    newReg.put("nombre", txtNom.toString())
                    newReg.put("correo", txtCorr.toString())
                    newReg.put("password", txtPassword.toString())

                    //Insetamos el Registro
                    val res = db.insert("usuarios", null, newReg)
                    db.close()

                    if (res.toInt() == -1) {
                        Toast.makeText(this, "No se inserto el registro", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Se inserto el registro", Toast.LENGTH_LONG).show()
                        binding.txtNombre.text.clear()
                        binding.txtAgregarCorreo.text.clear()
                        binding.txtAgregarPassword.text.clear()
                        var intent = Intent(this, pantallaAdministrador::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Es necesrio llenar todos los registros",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        btnCancelar.setOnClickListener({
            var intent = Intent(this, pantallaAdministrador::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        })
    }
}