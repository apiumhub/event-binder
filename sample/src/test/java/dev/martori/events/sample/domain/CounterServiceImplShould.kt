package dev.martori.events.sample.domain

import dev.martori.events.core.Bindable
import dev.martori.events.test.BindAllTestsRule
import dev.martori.events.test.Parameter
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CounterServiceImplShould : Bindable {

    private val repo = mockk<CounterRepository>()
    private val sut = CounterServiceImpl(repo)

    @get:Rule
    val bindRule = BindAllTestsRule

    @Before
    fun setUp() {
        coEvery {
            repo.getNewCount()
        } returns 3
    }

    @Test
    fun `test example`() {
        sut.modifyCounter shouldDispatch {
            sut.totalCount assertOverParameter { assertEquals(3, it) }
        }

        sut.modifyCounter withParameter Unit shouldDispatch {
            sut.totalCount withAny Parameter
        }

        sut.modifyCounter shouldDispatch {
            sut.totalCount withParameter 3
        }

//        sut.modifyCounter shouldDispatch {
//            sut.totalCount never Dispatched
//        }

    }

}
