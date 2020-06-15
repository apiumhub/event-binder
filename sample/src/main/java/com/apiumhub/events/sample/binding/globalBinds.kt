package com.apiumhub.events.sample.binding

import android.app.Application
import com.apiumhub.events.core.GlobalBind
import com.apiumhub.events.sample.binding.binds.bindAnimeDetailsErrors
import com.apiumhub.events.sample.binding.binds.bindAnimeListErrors
import org.koin.android.ext.android.get


fun Application.globalBinds() {
    GlobalBind.bindAnimeListErrors(get(), get())
    GlobalBind.bindAnimeDetailsErrors(get(), get())
}