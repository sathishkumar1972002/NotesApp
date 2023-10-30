package com.example.notesapp_roomdatabase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp_roomdatabase.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(),ToAdapter.NoteClickListener {


    lateinit var bind:ActivityMainBinding
     lateinit var adapter: ToAdapter
     lateinit var list: List<Datas>

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind= ActivityMainBinding.inflate(layoutInflater)

        setContentView(bind.root)

       setData()

        bind.recyclerView.setHasFixedSize(true)
        bind.recyclerView.layoutManager = LinearLayoutManager(this)

        bind.add.setOnClickListener {
            var intent=Intent(this,NotesActivity::class.java)
            startActivity(intent)

        }

        bind.menu.setOnClickListener {
            val Menu1 = PopupMenu(this, bind.menu)
            Menu1.menuInflater.inflate(R.menu.toolbar,Menu1.menu)
            Menu1.setOnMenuItemClickListener { item ->
                when(item.itemId)
                {
                    R.id.deleteAll ->
                    {
                        if (list.isEmpty())
                        {
                            Toast.makeText(this,"Note is Empty",Toast.LENGTH_SHORT).show()
                            false
                        }
                        else {
                            showAlert()
                            true
                        }
                    }
                    else -> false
                }

            }
            Menu1.show()
        }


    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData()
    {
        val databass = NoteDatabase.getInstance(this)
        val dao = databass!!.Notedao()

       CoroutineScope(Dispatchers.IO).launch { //// Database operation run in coroutine not in main thread
           list = dao.getAllData()

           withContext(Dispatchers.Main)
           {
               adapter= ToAdapter(list,this@MainActivity)
               bind.recyclerView.adapter= adapter
               adapter.notifyDataSetChanged()
           }
       }


    }

    override fun itemClick(position: Int) {
        var intent=Intent(this,NotesActivity::class.java).also {
            it.putExtra("position",position)
            it.putExtra("ID",list[position].Id)
            it.putExtra("TITLE",list[position].title_content)
            it.putExtra("BODY",list[position].body_content)
            it.putExtra("DATE",list[position].date)
            startActivity(it)
        }
    }


        //// we initiate which menu file we want to use


    private fun showAlert() {

        val alert = AlertDialog.Builder(this)
            .setTitle("Delete All")
            .setMessage("Do you want to delete all notes?")
            .setPositiveButton("Yes")
            { p0, p1 ->
                deleteAllnote()
            }
            .setNegativeButton("No",null)
           .show()

    }

    private fun deleteAllnote() {
        val databass = NoteDatabase.getInstance(this)
        val notedao = databass?.Notedao()


        CoroutineScope(Dispatchers.IO).launch {
            notedao?.deleteAllData()
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@MainActivity,"All notes deleted", Toast.LENGTH_SHORT).show()
                adapter.clearItems()
                onResume()
            }
        }
    }


}