package dev.martori.kataeventarch.domain

import cat.martori.core.Bindable
import cat.martori.eventarchtest.BindTestRule
import cat.martori.eventarchtest.dispatchedWith
import cat.martori.eventarchtest.implies
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CounterServiceImplShould : Bindable {

    private val sut = CounterServiceImpl()

    @get:Rule
    val bindRule = BindTestRule

    @Test
    fun `test example`() {
        sut.modifyCounter implies {
            sut.totalCount dispatchedWith { assertEquals(1, it) }
        }

        sut.modifyCounter dispatchedWith (Unit) implies {
            sut.totalCount dispatchedWith { assertEquals(2, it) }
        }

//        sut.modifyCounter implies {
//            sut.totalCount never Dispatched
//        }

    }

}
