package com.example.notes

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notes.data.db.entities.NoteEntity
import com.example.notes.databinding.ActivityAddNoteBinding
import com.example.notes.databinding.ActivityMainBinding
import com.nvt.color.ColorPickerDialog
import java.lang.Exception
import java.util.*
import java.util.logging.SimpleFormatter

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: NoteEntity
    private lateinit var old_note: NoteEntity
    var isUpdated = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var newtc: Int
        //
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            //TODO
            old_note = intent.getSerializableExtra("current_note") as NoteEntity
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            //!!!
            binding.etTitle.setTextColor(old_note.titleColor)
            isUpdated = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgColor.setOnClickListener {
            val colorPicker = ColorPickerDialog(
                this,
                Color.BLACK, // color init
                true, // true is show alpha
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                        // handle click button Cancel
                    }

                    //-65536 red
                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        // onBackPressed()
                        Toast.makeText(this@AddNote, colorPicker.toString(), Toast.LENGTH_SHORT).show()
                        //binding.etTitle.setTextColor(colorPicker)
                        //newtc = colorPicker
                        //binding.etTitle.setTextColor(colorPicker)
                        val title = binding.etTitle.text.toString()
                        val note_desc = binding.etNote.text.toString()
                        val tc = binding.etTitle.currentTextColor
                        if (title.isNotEmpty() || note_desc.isNotEmpty()) {
                            val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                            if (isUpdated) {
                                note = NoteEntity(
                                    old_note.id, title, note_desc, formatter.format(Date()), colorPicker
                                )
                            } else {
                                note = NoteEntity(
                                    null, title, note_desc, formatter.format(Date()), colorPicker
                                )
                            }
                            val intent = Intent()
                            intent.putExtra("note", note)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(this@AddNote, "Prosím vložte text", Toast.LENGTH_SHORT).show()
                        }

                    }
                })
            colorPicker.show()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etNote.text.toString()
            val tc = binding.etTitle.currentTextColor
            if (title.isNotEmpty() || note_desc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                if (isUpdated) {
                    note = NoteEntity(
                        old_note.id, title, note_desc, formatter.format(Date()), tc
                    )
                } else {
                    note = NoteEntity(
                        null, title, note_desc, formatter.format(Date()), tc
                    )
                }
                /*
                if (tC!= null) {
                    binding.etTitle.setTextColor(tC)
                }*/
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNote, "Prosím vložte text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.imgBackArrow.setOnClickListener {
            //TODO
            onBackPressed()
        }
    }
}