package shreckye.coursera.algorithms

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

// A union-find data structure implemented with ranks and path compression
class UnionFind(val size: Int) {
    companion object {
        const val NO_PARENT = -1
    }

    val parents = IntArray(size) { NO_PARENT }
    val ranks = IntArray(size) { 0 }
    inline var Int.parent
        get() = parents[this]
        set(parent) {
            parents[this] = parent
        }
    inline var Int.rank
        get() = ranks[this]
        set(rank) {
            ranks[this] = rank
        }

    fun unionRoots(root1: Int, root2: Int) {
        assertEquals(NO_PARENT, root1.parent)
        assertEquals(NO_PARENT, root2.parent)

        if (root1.rank > root2.rank)
            root2.parent = root1
        else if (root1.rank < root2.rank)
            root1.parent = root2
        else {
            root2.parent = root1
            root1.rank++
        }
    }

    fun unionRootsIfNecessary(root1: Int, root2: Int): Boolean =
        if (root1 != root2) {
            unionRoots(root1, root2)
            true
        } else
            false


    fun find(node: Int): Int {
        val parent = node.parent
        // Find and compress
        if (parent == NO_PARENT)
            return node
        else {
            val root = find(parent)
            node.parent = root
            return root
        }
    }

    fun findAndUnionIfNecessary(node1: Int, node2: Int): Boolean =
        unionRootsIfNecessary(find(node1), find(node2))

    fun findAndUnionDifferent(node1: Int, node2: Int) {
        val root1 = find(node1)
        val root2 = find(node2)
        assertNotEquals(root1, root2)
        unionRoots(root1, root2)
    }

    fun areInSameSet(node1: Int, node2: Int) =
        find(node1) == find(node2)

    fun areInDifferentSets(node1: Int, node2: Int) =
        find(node1) != find(node2)

    fun getSets() =
        (0 until size).groupBy { find(it) }
}