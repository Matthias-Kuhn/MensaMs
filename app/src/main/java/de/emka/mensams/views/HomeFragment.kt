package de.emka.mensams.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import de.emka.mensams.R
import de.emka.mensams.data.BalanceUtils


class HomeFragment : Fragment() {

    lateinit var balanceTextView: TextView;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val inputField = view.findViewById<EditText>(R.id.card_nr_input)
        balanceTextView = view.findViewById<TextView>(R.id.tv_balance_view)

        inputField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s != null && s.length == 7) {
                    balanceTextView.text = "..."
                    BalanceUtils.getBalanceAndExecute(s.toString(), ::showResult)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        return view
    }

    fun showResult(input : Int) {
        if (input < 0) {
            balanceTextView.text = "Fehler"
            return
        }
        balanceTextView.text = BalanceUtils.intToString(input)
    }


}