package com.example.stubprojectkotlin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.example.stubprojectkotlin.utils.PreferenceHelper
import com.google.gson.Gson

abstract class BaseFragment : Fragment() {
    var mBaseActivity: BaseActivity? = null
    private lateinit var  view: View
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mBaseActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(iD, container, false)
        ButterKnife.bind(this, view)
        this.view = view
        initUI(view)
        return view
    }

    abstract val iD: Int
    abstract fun initUI(view: View)

    override fun getView(): View {
        return view
    }

    val gSON: Gson?
        get() = mBaseActivity!!.gSON
    val preferenceHelper: PreferenceHelper?
        get() = mBaseActivity!!.preferenceHelper
}