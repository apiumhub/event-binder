package dev.martori.events.sample.binding

import android.app.Application
import dev.martori.events.core.GlobalBind
import dev.martori.events.sample.binding.binds.bindAnimeDetailsErrors
import dev.martori.events.sample.binding.binds.bindAnimeListErrors
import org.koin.android.ext.android.get


fun Application.globalBinds() {
    GlobalBind.bindAnimeListErrors(get(), get())
    GlobalBind.bindAnimeDetailsErrors(get(), get())
}