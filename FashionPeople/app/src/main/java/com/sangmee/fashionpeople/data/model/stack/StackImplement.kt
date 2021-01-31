package com.sangmee.fashionpeople.data.model.stack

interface StackImplement<Type> {
    fun count(): Int
    fun pop(): Type
    fun peek(): Type
    fun push(item: Type)
    fun isEmpty(): Boolean
    fun contains(item: Type): Boolean
    fun remove(item: Type)
    fun clear()
}
