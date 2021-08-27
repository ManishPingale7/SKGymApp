package com.example.skgym.fragments.datacollection

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.BitmapCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.skgym.R
import com.example.skgym.data.Member
import com.example.skgym.databinding.FragmentGeneralDataBinding
import com.example.skgym.di.component.DaggerFactoryComponent
import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.repository.MainRepository
import com.example.skgym.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import concrete
import me.shouheng.compress.Compress
import me.shouheng.compress.strategy.config.ScaleMode
import java.io.ByteArrayOutputStream
import java.io.File


class GeneralData : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentGeneralDataBinding? = null
    private val binding get() = _binding!!
    var imageUri: Uri? = null
    private var currentUser: FirebaseUser? = null
    var mAuth = FirebaseAuth.getInstance()
    var member = Member()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralDataBinding.inflate(inflater, container, false)
        init()
        binding.addProfileImage.setOnClickListener {
            pickImage()
        }

        binding.profileImageGeneral.setOnClickListener {
            pickImage()
        }

        binding.btnContinueGeneral1.setOnClickListener {
            val firstname = binding.firstNameGeneral.text.toString()
            val middleName = binding.middleNameGeneral.text.toString()
            val lastname = binding.lastNameGeneral.text.toString()

            if (firstname.isNotEmpty() && middleName.isNotEmpty() && lastname.isNotEmpty()) {
                if (imageUri != null) {
                    member.name = "$firstname $middleName $lastname"
                    member.imgUrl = imageUri.toString()
                    //Passing Data
                    val action = GeneralDataDirections.actionGeneralDataToGymData(member)
                    it.findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Select Profile Image", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Fill the Fields", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


    private fun init() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.my_statusbar_color)
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this, component.getFactory()).get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser

    }

    private fun pickImage() {
        val galleryIntent = Intent()
        galleryIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = reduceSizeImage(data.data!!)
        }
    }

    private fun reduceSizeImage(uri: Uri): Uri {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        Log.d(
            "TAG", "reduceSizeImage:1 ${BitmapCompat.getAllocationByteCount(bitmap)}\n" +
                    " ${BitmapCompat.getAllocationByteCount(bitmap).toDouble() / 8e+6}"
        )

        val size: Double = BitmapCompat.getAllocationByteCount(bitmap).toDouble()
        val other: Double = 8e+6
        if ((size / other) < 2) {
            Log.d("TAG", "reduceSizeImage: Less than 1MB")
            binding.profileImageGeneral.setImageURI(uri)
            return uri
        } else {
            Log.d("TAG", "reduceSizeImage: Greater than 1MB")

            val bm = Compress.with(requireContext(), bitmap)
                .concrete {
                    this.maxWidth = 100f
                    this.maxHeight = 100f
                    this.scaleMode = ScaleMode.SCALE_SMALLER
                }
                .asBitmap()
                .get()

            bm?.let {
                val size = BitmapCompat.getAllocationByteCount(bm)
                Log.d("TAG", "reduceSizeImage:2 $size ")
            }
            binding.profileImageGeneral.setImageBitmap(bm)

            val file = File(requireContext().cacheDir, "tempFile")
            file.delete()
            file.createNewFile()
            val fileOS = file.outputStream()
            val baOS = ByteArrayOutputStream()
            bm?.let {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baOS)
            }
            val ba = baOS.toByteArray()
            fileOS.write(ba)
            fileOS.flush()
            fileOS.close()
            val uri = file.toUri()
            file.delete()

            Log.d("TAG", "reduceSizeImage:$bm  ")
            return uri

        }
    }

}
