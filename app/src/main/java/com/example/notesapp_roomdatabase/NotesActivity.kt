package com.example.notesapp_roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp_roomdatabase.databinding.ActivityMainBinding
import com.example.notesapp_roomdatabase.databinding.ActivityNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class NotesActivity : AppCompatActivity() {

    private lateinit var bind : ActivityNotesBinding

    ///////////////////

    private lateinit var tit:String
    private lateinit var bdy:String
    private lateinit var date:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(bind.root)
        /////////////

        ////////
         bind.deleteButton.hide()
        /////////////
        if (intent.hasExtra("ID"))
        {
            bind.ipbody.setText(intent.getStringExtra("BODY").toString())
            bind.iptitle.setText(intent.getStringExtra("TITLE").toString())
            bind.deleteButton.show()
        }
        bind.deleteButton.setOnClickListener {
            showAlert()

        }

        bind.add.setOnClickListener{

            tit = bind.iptitle.text.toString()
            bdy = bind.ipbody.text.toString()

            if (validate()) {

                if(intent.hasExtra("ID"))
                {
                         update_note()

                }
                else {
                    saveNote()
                    // Toast.makeText(this,"Updated ", Toast.LENGTH_SHORT).show()
                    bind.iptitle.text = null
                    bind.ipbody.text = null

                    finish()
                }
            }

        }

        bind.back.setOnClickListener {
            finish()
        }

    }

    private fun delete_note() {
        val databass = NoteDatabase.getInstance(this)
        val notedao = databass?.Notedao()


        CoroutineScope(Dispatchers.IO).launch {
            val datas = intent.getIntExtra("ID",0)
            notedao?.deletebyId(datas)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@NotesActivity,"Deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun update_note() {
        val databass = NoteDatabase.getInstance(this)
        val notedao = databass?.Notedao()
         var date1 =intent.getStringExtra("DATE")
          var ID = intent.getIntExtra("ID",0)
        CoroutineScope(Dispatchers.IO).launch {
            val datas = Datas(ID,tit,bdy, date1!!)
            notedao?.update(datas)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@NotesActivity,"Updated ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNote() {

        val databass = NoteDatabase.getInstance(this)
        val notedao = databass?.Notedao()
        val date = SimpleDateFormat("dd MMM yy , hh:mm a",Locale.getDefault()).format(Date())
        val datas = Datas(0,tit,bdy,date)

        GlobalScope.launch {
            notedao?.insert(datas)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@NotesActivity,"Added", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun validate():Boolean
    {
        if (tit.trim().isNullOrEmpty() or bdy.trim().isNullOrEmpty())
        {
            Toast.makeText(this,"Not allowed to add empty notes ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showAlert() {

        val alert = AlertDialog.Builder(this)
            .setTitle("Delete All")
            .setMessage("Do you want to delete this note?")
            .setPositiveButton("Yes")
            { p0, p1 ->
                delete_note()
            }
            .setNegativeButton("No",null)
            .show()

    }


}