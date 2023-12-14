package com.example.ordinario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ordinario.databinding.ActivityPantallaAdministradorBinding

class pantallaAdministrador : AppCompatActivity() {

    private var data =  ArrayList<Usuarios>()
    private lateinit var rvAdapter: usuariosAdapter




    private lateinit var binding: ActivityPantallaAdministradorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)

        //conexion base de datos
        val dbUsuarios = DBHelperUsuarios(this)
        var db = dbUsuarios.readableDatabase


        //Declaramos un cursor para recorrer los registros en la tabla
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)
        //evaluar if si el cursor de la base de datos se  puede mover
        if(cursor.moveToFirst())
        {
            do {
                //pasar los valores de la tabla a variables locales
                val idUsuarios = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                val itemPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                //agregarlos a la lista data
                data.add(Usuarios(
                    "${idUsuarios}",
                    "${itemNom}",
                    "${itemPassword}",
                    "${itemCorr}"
                )
                )
            }  while(cursor.moveToNext())
            //cerramos base de datos y cursor
            db.close()
            cursor.close()
            //pasar la informacion del Array por el adaptador
            rvAdapter = usuariosAdapter(this,data, object : usuariosAdapter.OptionsMenuClickListener{
                override fun onOptionsMenuClicked(position: Int) {
                    //funcion para llamar menu opciones
                    itemOptionsMenu(position)
                }
            })

            binding.rvUsuarios.adapter = rvAdapter
        }// fin del if movetofirst


        val btnAgregar = binding.btnAgregar
        btnAgregar.setOnClickListener({
            val intent = Intent(this, agregarUsuario::class.java)
            startActivity(intent)
        })
    }

    private fun itemOptionsMenu( position: Int) {
        val popupMenu = PopupMenu(this, binding.rvUsuarios[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)
        //Para cambiarnos de activity
        val intento2 = Intent(this,agregarUsuario::class.java)
        //Implementar el click en el item
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.borrar -> {
                        val dialog = AlertDialog.Builder(this@pantallaAdministrador)
                            .setTitle("Borrar resgistro")
                            .setMessage("¿Estas seguro que deseas eliminar el registro?")
                            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                                Toast.makeText(this@pantallaAdministrador, "Presionaste cancelar", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                                //Toast.makeText(this@MainActivity,"Presionaste OK",Toast.LENGTH_SHORT).show()

                                val dbUsuarios = DBHelperUsuarios(this@pantallaAdministrador)
                                val db = dbUsuarios.writableDatabase
                                val tmpUsuario = data[position]
                                val id = arrayOf(tmpUsuario.id)
                                val res = db.delete("usuarios", "id = ?",id)
                                db.close()

                                if(res > 0){
                                    data.remove(tmpUsuario)
                                    rvAdapter.notifyItemRemoved(position)
                                    Toast.makeText(this@pantallaAdministrador, "El elemento se elimino con éxito", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    Toast.makeText(this@pantallaAdministrador, "Error al eliminar el elemento", Toast.LENGTH_LONG).show()
                                }
                                dialog.dismiss()
                                recreate()
                            }
                            .setCancelable(false)
                            .create()

                        dialog.show()
                        return true
                    }
                    R.id.editar ->{
                        //Tomamos los datos del usuario, en la posición de la lista donde hicieron click
                        val nombre = data[position].nombre
                        val correo = data[position].correo
                        val password = data[position].password
                        val id = data[position].id
                        //En position tengo el indice del elemento en la lista
                        val idUsuario: Int = position
                        intento2.putExtra("id", "${id}")
                        intento2.putExtra("mensaje","edit")
                        intento2.putExtra("nombre","${nombre}")
                        intento2.putExtra("correo","${correo}")
                        intento2.putExtra("password","${password}")
                        //Pasamos por extras el idAlum para poder saber cual editar de la lista (ArrayList)
                        intento2.putExtra("idU",idUsuario)
                        startActivity(intento2)
                        return true
                    }
                    R.id.ver ->{
                        val intento3 = Intent(this@pantallaAdministrador, verinfo2::class.java)

                        val nombre = data[position].nombre
                        val correo = data[position].correo
                        val password = data[position].password

                        intento3.putExtra("nombre","${nombre}")
                        intento3.putExtra("correo","${correo}")
                        intento3.putExtra("password","${password}")
                        startActivity(intento3)
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
}