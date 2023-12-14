package com.example.ordinario

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ordinario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnLogin = binding.btnLogin


        btnLogin.setOnClickListener({
            val correoIngresado = binding.txtCorreo
            val passwordIngresada = binding.txtPassword

            //Creamos la conexion con la BD
            val dbUsuarios = DBHelperUsuarios(this)
            //Abrimos la base de datos solo para leer
            val db = dbUsuarios.readableDatabase
            //Declaramos un cursor para recorrer los registros en la tabla
            val cursor = db.rawQuery("SELECT * FROM usuarios", null)
            //evaluar if si el cursor de la base de datos se  puede mover

            if (correoIngresado.text.toString() == "administrador@becas.com" && passwordIngresada.text.toString() == "administrador") {
                val intent = Intent(this, pantallaAdministrador::class.java)
                startActivity(intent)
                binding.txtCorreo.text.clear()
                binding.txtPassword.text.clear()
                // Salir del bucle usando la etiqueta
            }else {
                // Etiqueta para el bucle do-while
                buclePrincipal@ do {
                    if (cursor.moveToFirst()) {
                        do {
                            val correo = cursor.getString(cursor.getColumnIndex("correo"))
                            val password = cursor.getString(cursor.getColumnIndex("password"))

                            if (correoIngresado.text.toString() == correo && passwordIngresada.text.toString() == password) {
                                val intent = Intent(this, pantallaUsuario::class.java)
                                startActivity(intent)
                                binding.txtCorreo.text.clear()
                                binding.txtPassword.text.clear()
                                // Salir del bucle usando la etiqueta
                                break@buclePrincipal
                            }
                        } while (cursor.moveToNext())
                    } else {
                        Toast.makeText(this, "Sin registros", Toast.LENGTH_SHORT).show()
                    }

                    // Mover el código fuera del bucle
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT)
                        .show()
                } while (false) // El bucle se ejecuta solo una vez debido a la etiqueta y al break

                db.close()
                cursor.close()
            }
        })
    }
}