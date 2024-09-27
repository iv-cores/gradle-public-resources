package test.utils

import org.mockito.Mockito

internal inline fun <reified T> mock(): T {
    return Mockito.mock(T::class.java) as T
}