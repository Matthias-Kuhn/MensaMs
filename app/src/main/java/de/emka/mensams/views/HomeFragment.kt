package de.emka.mensams.views

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import de.emka.mensams.BalanceWidgetProvider
import de.emka.mensams.R
import de.emka.mensams.data.BalanceUtils
import de.emka.mensams.data.ResponseType
import de.emka.mensams.viewmodels.SharedViewModel


class HomeFragment : Fragment() {

    lateinit var balanceTextView: TextView
    lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val inputField = view.findViewById<EditText>(R.id.card_nr_input)
        balanceTextView = view.findViewById<TextView>(R.id.tv_balance_view)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        balanceTextView.text = viewModel.balanceString
        inputField.setText(viewModel.cardNr)

        if (viewModel.balanceString == "-" && viewModel.cardNr.length == 7) {
            BalanceUtils.getBalanceAndExecute(inputField.text.toString(), ::showResult)
        }

        inputField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                viewModel.setAndStoreCardNr(s.toString())
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

    fun showResult(input : Int, responseType: ResponseType) {

        when (responseType) {
            ResponseType.SUCCESSFUL_RESPONSE -> viewModel.setAndStoreBalance(input)
            ResponseType.EMPTY_RESPONSE_BODY -> viewModel.balanceString = "Kartennummer richtig?"
            ResponseType.FAILED_TO_CONNECT -> viewModel.balanceString = "Internet?"
        }
        balanceTextView.text = viewModel.balanceString

        val intent = Intent(context, BalanceWidgetProvider::class.java)
        intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(requireContext(), BalanceWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids)
        requireContext().sendBroadcast(intent)

    }


}