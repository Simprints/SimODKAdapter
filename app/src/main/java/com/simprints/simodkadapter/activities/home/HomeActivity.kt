package com.simprints.simodkadapter.activities.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.simprints.simodkadapter.R

class HomeActivity : AppCompatActivity(), HomeContract.View {

    override lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        presenter = HomePresenter(this).apply { start() }
    }

}
