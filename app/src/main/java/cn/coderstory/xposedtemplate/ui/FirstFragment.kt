package cn.coderstory.xposedtemplate.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import cn.coderstory.xposedtemplate.R
import cn.coderstory.xposedtemplate.State
import cn.coderstory.xposedtemplate.databinding.FragmentFirstBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        Thread.sleep(1000)
        var dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("标题")
            .setMessage(State.notice)
            .create()
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确认", DialogInterface.OnClickListener { dialog, which ->
            run {
                Snackbar.make(binding.root,"点击了确认",5)
            }
        })
        dialog.show()
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        //val layout = ConstraintLayout(requireContext())
        var view = this.requireView().rootView
        var materialButton = view.findViewById<MaterialButton>(R.id.btn1)
        var layout = view.findViewById<ConstraintLayout>(R.id.layout2)
        layout.removeAllViews()
        layout?.addView(materialButton)
        //layout?.addView(materialButton)
        super.onStart()
    }
}