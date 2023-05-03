package com.example.notes

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Typeface
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
    private var locIsBold = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            //TODO
            oldNote = intent.getSerializableExtra("current_note") as NoteEntity
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdated = true
            binding.etTitle.setTextColor(oldNote.titleColor)
            locIsBold = oldNote.isBold
            if (locIsBold) {
                binding.etNote.setTypeface(null, Typeface.BOLD)
            }
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
                val chooser = Intent.createChooser(selectPicture, "")
                startActivityForResult(chooser, 2)
            }
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val noteDesc = binding.etNote.text.toString()
            val tc = binding.etTitle.currentTextColor
            //val uri = pictureUri
            if (title.isNotEmpty() || noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault())
                note = if (isUpdated) {
                    NoteEntity(
                        oldNote.id, title, noteDesc, oldNote.date, tc, pictureUri, locIsBold
                    )
                } else {
                    NoteEntity(
                        null, title, noteDesc, formatter.format(Date()), tc, pictureUri, locIsBold
                    )
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
        binding.imgBold.setOnClickListener {
            locIsBold = if (!locIsBold) {
                binding.etNote.setTypeface(null, Typeface.BOLD)
                true
            } else {
                binding.etNote.setTypeface(null, Typeface.NORMAL)
                false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val galleryInText = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryInText, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            decode(data.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun decode(uri: Uri?): Bitmap {
        val pickedBitMap: Bitmap
        if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(this.contentResolver, uri!!)
            pickedBitMap = ImageDecoder.decodeBitmap(source)
            binding.pictureField.setImageBitmap(pickedBitMap)
            pictureUri = uri.toString()
        } else {
            @Suppress("DEPRECATION")
            pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
        return pickedBitMap
    }
}