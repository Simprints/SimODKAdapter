package com.simprints.simodkadapter.di

import com.simprints.simodkadapter.activities.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val koinModules = module {

    viewModel { (action: String?) -> MainViewModel(action) }

}