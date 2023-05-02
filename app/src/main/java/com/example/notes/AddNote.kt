package com.example.notes

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notes.data.db.entities.NoteEntity
import com.example.notes.databinding.ActivityAddNoteBinding
import com.nvt.color.ColorPickerDialog
import java.lang.Exception
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: NoteEntity
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
            //!!!
            binding.etTitle.setTextColor(old_note.titleColor)
            isUpdated = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // var tc = binding.etTitle.currentTextColor

        binding.imgColor.setOnClickListener {
            val colorPicker = ColorPickerDialog(
                this,
                Color.BLACK, // color init
                false, // true is show alpha
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        // tc = colorPicker
                        binding.etTitle.setTextColor(colorPicker)
                    }
                })
            colorPicker.show()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDesc = binding.etNote.text.toString()
            val tc = binding.etTitle.currentTextColor
            if (title.isNotEmpty() || noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                if (isUpdated) {
                    note = NoteEntity(
                        old_note.id, title, noteDesc, old_note.date, tc
                    )
                } else {
                    note = NoteEntity(
                        null, title, noteDesc, formatter.format(Date()), tc
                    )
                }
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