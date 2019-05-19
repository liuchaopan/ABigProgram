package com.qingmei2.rhine.base.view.fragment

import androidx.fragment.app.Fragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.kcontext

abstract class InjectionFragment : AutoDisposeFragment(), KodeinAware {

    protected val parentKodein by kodein()

    override val kodeinContext = kcontext<Fragment>(this)
}