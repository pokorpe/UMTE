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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    //var pickedPhoto: Uri? = null
    //var pickedBitMap: Bitmap? = null

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

            binding.etTemp.setText(old_note.pictureUri)
            binding.pictureField.setImageBitmap(decode(Uri.parse(old_note.pictureUri)))
            // decode(Uri.parse(old_note.pictureUri))


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

        binding.imgPicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                val galleryInText = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryInText, 2)
            }
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDesc = binding.etNote.text.toString()
            val tc = binding.etTitle.currentTextColor
            val uri = binding.etTemp.text.toString()
            //val uri = note.pictureUri
            if (title.isNotEmpty() || noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                if (isUpdated) {
                    //val uri = old_note.pictureUri
                    note = NoteEntity(
                        old_note.id, title, noteDesc, old_note.date, tc, uri
                    )
                    println("1")
                } else {
                    //val uri = note.pictureUri
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
            /*
            var pickedPhoto = data.data
            var pickedBitMap: Bitmap? = null
            println("photo: " + pickedPhoto)

                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
                            pickedBitMap = ImageDecoder.decodeBitmap(source)
                            println("bitmap: "+pickedBitMap)
                            binding.pictureField.setImageBitmap(pickedBitMap)
                            println(pickedBitMap)
                        } else {
                            pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
                            binding.pictureField.setImageBitmap(pickedBitMap)
                        }
             */
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun decode(uri: Uri?): Bitmap {
        if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(this.contentResolver, uri!!)
            val pickedBitMap = ImageDecoder.decodeBitmap(source)
            binding.pictureField.setImageBitmap(pickedBitMap)
            binding.etTemp.setText(uri.toString())
            return pickedBitMap;
        } else {
            val pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            //binding.pictureField.setImageBitmap(pickedBitMap)
            return pickedBitMap
        }
    }
}