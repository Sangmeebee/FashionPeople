package com.sangmee.fashionpeople.data.model.stack

class Stack<E> : StackImplement<E> {

    val list = mutableListOf<E>()

    override fun count(): Int {
        return list.size
    }

    override fun pop(): E {
        return list.removeAt(list.size - 1)
    }

    override fun peek(): E {
        return list[list.size - 1]
    }

    override fun push(item: E) {
        list.add(item)
    }

    override fun isEmpty(): Boolean {
        return list.size == 0
    }

    override fun contains(item: E): Boolean {
        return list.contains(item)
    }

    override fun remove(item: E) {
        list.remove(item)
    }

    override fun clear() {
        list.clear()
    }
}
