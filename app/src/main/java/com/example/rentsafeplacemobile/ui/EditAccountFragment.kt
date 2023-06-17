package com.example.rentsafeplacemobile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.rentsafeplacemobile.InfoViewModel
import com.example.rentsafeplacemobile.R
import com.example.rentsafeplacemobile.data.Tenant
import com.example.rentsafeplacemobile.databinding.FragmentEdtAccountBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class EditAccountFragment : Fragment() {
    private var _binding: FragmentEdtAccountBinding? = null
    private val binding get() = _binding!!
    private var tenant: Tenant? = null
    private val viewModel: InfoViewModel by viewModels()
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var imageUploadLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                uploadImageToFirebase(uri)
            }
        }

        imageUploadLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri: Uri? = data?.data
                uri?.let {
                    imageUri = uri
                    uploadImageToFirebase(uri)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEdtAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.getInfo()
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.info.collect { tenant ->
                    if (tenant != null) {
                        this@EditAccountFragment.tenant = tenant
                        binding.editTextName.setText(tenant.name)
                        binding.editTextEmail.setText(tenant.email)
                        binding.editTextPhone.setText(tenant.phone)
                        val photo = if (tenant.photo != null && tenant.photo != "") {
                            tenant.photo
                        } else {
                            "https://firebasestorage.googleapis.com/v0/b/rent-safe-place.appspot.com/o/images%2Fno-user-image-icon-27.jpg?alt=media&token=4de8c976-97da-4fa4-8799-96ae82c20803"
                        }
                        binding.imageViewAvatar.load(photo) {
                            transformations(
                                CircleCropTransformation()
                            )
                        }
                    }
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            with(binding) {
                val email = editTextEmail.text.toString()
                val name = editTextName.text.toString()
                val phone = editTextPhone.text.toString()
                if (tenant != null) {
                    viewModel.editInfo(Tenant(tenant!!.id, email, tenant!!.password, name, phone, tenant?.photo))
                    Toast.makeText(this@EditAccountFragment.requireContext(), resources.getString(R.string.info_was_changed) + " ", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        binding.buttonUploadImage.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageUploadLauncher.launch(intent)
    }

    private fun uploadImageToFirebase(uri: Uri) {
        val storageRef = Firebase.storage.reference
        val imagesRef = storageRef.child("images")
        val imageFileName = "tenant${tenant?.id}"
        val imageRef = imagesRef.child(imageFileName)

        imageRef.putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1
    }
}
