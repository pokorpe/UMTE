package com.example.notes

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notes.data.db.entities.NoteEntity
import com.example.notes.databinding.ActivityAddNoteBinding
import com.example.notes.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*
import java.util.logging.SimpleFormatter

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note :NoteEntity
    private lateinit var old_note: NoteEntity
    var isUpdated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            //TODO
            old_note = intent.getSerializableExtra("current_note") as NoteEntity
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdated = true
        }catch (e:Exception){
            e.printStackTrace()
        }
        binding.imgCheck.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etNote.text.toString()

            if (title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                if (isUpdated){
                    note = NoteEntity(
                        old_note.id,title,note_desc,formatter.format(Date())
                    )
                }else{
                    note = NoteEntity(
                        null,title,note_desc,formatter.format(Date())
                    )
                }
                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }else{
                Toast.makeText(this@AddNote,"Prosím vložte text",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.imgBackArrow.setOnClickListener {
            //TODO
            onBackPressed()
        }
    }
}