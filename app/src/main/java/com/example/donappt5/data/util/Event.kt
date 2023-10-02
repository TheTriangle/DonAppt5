package com.example.donappt5.data.util

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Запретить изменение свойства извне

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}