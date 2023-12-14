package com.example.ordinario

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.ordinario.databinding.ActivityAgregarBecarioBinding

class agregarBecario : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarBecarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarBecarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnCancelar = binding.btnCancelarBecario
        val btnAgregar = binding.btnGuardarBecario

        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        if(intent.getStringExtra("mensaje").toString() == "edit"){

            binding.txtAgregarNombre.setText("${intent.getStringExtra("nombre").toString()}")
            binding.txtAgregarCuenta.setText("${intent.getStringExtra("nocuenta").toString()}")
            binding.txtImagen.setText("${intent.getStringExtra("imagen").toString()}")
            val radioGroup = findViewById<RadioGroup>(R.id.rdgTipoBeca)
            val textoBuscado = "${intent.getStringExtra("tipoDeBeca").toString()}" // El texto que estás buscando

            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                if (radioButton.text.toString() == textoBuscado) {
                    val idRadioButton = radioButton.id
                    findViewById<RadioButton>(idRadioButton).isChecked = true
                    break // Puedes detener el bucle si encuentras el RadioButton
                }
            }
            binding.txvTitulo2.setText("Actualización de Becario")
            binding.btnGuardarBecario.setText("Actualizar")
        }


        btnCancelar.setOnClickListener({
            var intent = Intent(this, pantallaUsuario::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        })

        btnAgregar.setOnClickListener({
            //creamos la conexion a la DB
            val dbBecarios = DBHelperBecarios(this)
            var db = dbBecarios.writableDatabase


            //Pasamos los valores de los editText a variables
            val nocuenta = binding.txtAgregarCuenta.text
            val txtNom = binding.txtAgregarNombre.text
            val radioGruop = binding.rdgTipoBeca.checkedRadioButtonId
            val tipoBeca = findViewById<RadioButton>(radioGruop).text.toString()
            val imagen = binding.txtImagen.text

            //para actualizar
            if(intent.getStringExtra("mensaje").toString() == "edit"){
                if (txtNom.isNotEmpty() && nocuenta.isNotEmpty() && tipoBeca.isNotEmpty() && imagen.isNotEmpty()) {
                    val valores = ContentValues()
                    valores.put("nombre", txtNom.toString())
                    valores.put("nocuenta", nocuenta.toString())
                    valores.put("tipoDeBeca", tipoBeca)
                    valores.put("imagen", imagen.toString())

                    val id = intent.getStringExtra("id").toString()
                    val parametros = arrayOf(id)
                    //Insetamos el Registro
                    val res = db.update("becarios", valores, "id = ?",parametros)
                    db.close()

                    if (res == -1) {
                        Toast.makeText(this, "No se actualizo el registro", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Se actualizo el registro", Toast.LENGTH_LONG).show()
                        binding.txtAgregarNombre.text.clear()
                        binding.txtAgregarCuenta.text.clear()
                        binding.txtImagen.text.clear()
                        binding.rdgTipoBeca.clearCheck()
                        var intent = Intent(this, pantallaUsuario::class.java)
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
                //Pasamos los valores de las cajas a una variable contentValues
                if (txtNom.isNotEmpty() && nocuenta.isNotEmpty() && tipoBeca.isNotEmpty() && imagen.isNotEmpty()) {
                    val newReg = ContentValues()
                    newReg.put("nombre", txtNom.toString())
                    newReg.put("nocuenta", nocuenta.toString())
                    newReg.put("tipoDeBeca", tipoBeca)
                    newReg.put("imagen", imagen.toString())

                    //Insetamos el Registro
                    val res = db.insert("becarios", null, newReg)
                    db.close()

                    if (res.toInt() == -1) {
                        Toast.makeText(this, "No se inserto el registro", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Se inserto el registro", Toast.LENGTH_LONG).show()
                        binding.txtAgregarNombre.text.clear()
                        binding.txtAgregarCuenta.text.clear()
                        binding.txtImagen.text.clear()
                        binding.rdgTipoBeca.clearCheck()
                        var intent = Intent(this, pantallaUsuario::class.java)
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
    }
}