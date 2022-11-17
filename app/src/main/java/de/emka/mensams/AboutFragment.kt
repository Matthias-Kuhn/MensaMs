package de.emka.mensams

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class AboutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.findViewById<ImageView>(R.id.btn_instagram).setOnClickListener {
            openInstagram()
        }
        view.findViewById<ImageView>(R.id.btn_github).setOnClickListener {
            openGithub()
        }
        view.findViewById<TextView>(R.id.btn_privacy_policy).setOnClickListener {
            openPrivacyPolicy()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


    }

    private fun openPrivacyPolicy() {
        // TODO: Add link and intent
        Toast.makeText(activity,"Open privacy policy",Toast.LENGTH_SHORT).show()
    }

    private fun openGithub() {
        // TODO: Add link and intent
        Toast.makeText(activity,"Open GitHub",Toast.LENGTH_SHORT).show()
    }

    private fun openInstagram() {
        val uri: Uri = Uri.parse("http://instagram.com/_u/_matthiaskuhn")

        val i = Intent(Intent.ACTION_VIEW, uri)

        i.setPackage("com.instagram.android")

        try {
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/_matthiaskuhn")
                )
            )
        }
    }


}