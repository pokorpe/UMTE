package com.example.notes

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notes.data.db.entities.NoteEntity
import com.example.notes.databinding.ActivityAddNoteBinding
import com.nvt.color.ColorPickerDialog
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: NoteEntity
    private lateinit var oldNote: NoteEntity
    private var isUpdated = false
    private var pictureUri = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            //TODO
            oldNote = intent.getSerializableExtra("current_note") as NoteEntity
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)

            binding.etTitle.setTextColor(oldNote.titleColor)
            isUpdated = true

            pictureUri = oldNote.pictureUri
            binding.pictureField.setImageBitmap(decode(Uri.parse(oldNote.pictureUri)))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgColor.setOnClickListener {
            val colorPicker = ColorPickerDialog(
                this,
                Color.BLACK,
                true,
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        binding.etTitle.setTextColor(colorPicker)
                    }
                })
            colorPicker.show()
        }

        binding.imgPicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val selectPicture = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                val chooser = Intent.createChooser(selectPicture,"")
                startActivityForResult(chooser, 2)
            }
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDesc = binding.etNote.text.toString()
            val tc = binding.etTitle.currentTextColor
            val uri = pictureUri
            if (title.isNotEmpty() || noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                if (isUpdated) {
                    note = NoteEntity(
                        oldNote.id, title, noteDesc, oldNote.date, tc, uri
                    )
                    println("1")
                } else {
                    note = NoteEntity(
                        null, title, noteDesc, formatter.format(Date()), tc, uri
                    )
                    println("2")
                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNote, "Nejprve vložte text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.imgBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.pictureField.setOnClickListener {
            val openPicture = Intent(Intent.ACTION_VIEW, Uri.parse(pictureUri))
           // val chooser = Intent.createChooser(openPicture, pictureUri)
            startActivity(openPicture)
           // startActivity(chooser )
            //Toast.makeText(this@AddNote, pictureUri, Toast.LENGTH_LONG).show()
            //binding.etNote.setText(pictureUri)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val galleryInText = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryInText, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            decode(data.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun decode(uri: Uri?): Bitmap {
        if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(this.contentResolver, uri!!)
            val pickedBitMap = ImageDecoder.decodeBitmap(source)
            binding.pictureField.setImageBitmap(pickedBitMap)
            pictureUri = uri.toString()
            return pickedBitMap;
        } else {
            val pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            return pickedBitMap
        }
    }
}