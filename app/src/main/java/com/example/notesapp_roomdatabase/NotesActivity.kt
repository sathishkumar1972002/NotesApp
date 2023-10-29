package com.example.notesapp_roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp_roomdatabase.databinding.ActivityMainBinding
import com.example.notesapp_roomdatabase.databinding.ActivityNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesActivity : AppCompatActivity() {

    private lateinit var bind : ActivityNotesBinding

    ///////////////////

    private lateinit var tit:String
    private lateinit var bdy:String

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
            delete_note()
           finish()
        }

        bind.add.setOnClickListener{

            tit = bind.iptitle.text.toString()
            bdy = bind.ipbody.text.toString()

            if (validate()) {

                if(intent.hasExtra("ID"))
                {
                         update_note()
                    finish()
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
            }
        }
    }

    private fun update_note() {
        val databass = NoteDatabase.getInstance(this)
        val notedao = databass?.Notedao()


        CoroutineScope(Dispatchers.IO).launch {
            val datas = Datas(intent.getIntExtra("ID",0),tit,bdy)
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
        val datas = Datas(0,tit,bdy)

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


}