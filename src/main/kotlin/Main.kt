import java.util.*

fun main() {

    val l1 = ListNode(1, ListNode(6, ListNode(8)))
    val l2 = ListNode(3, ListNode(6, ListNode(7)))
    val l3 = ListNode(1, ListNode(3, ListNode(4)))
    println("Merging sorted linked lists: ")
    var result = mergeSortedLists(mutableListOf(l1,l2,l3))
    while(result != null) {
        print("${result.value} ")
        result = result.next
    }
    println("")
    println("Find kth smallest number among all: ${findKthSmallestAmongSortedLists(
        mutableListOf(listOf(2,6,8),listOf(3,6,7),listOf(1,3,4)), 
        5)
    }")
    println("Find kth smallest number among all: ${findKthSmallestAmongSortedLists(
        mutableListOf(listOf(5,8,9),listOf(1,7)),
        3)
    }")
}
/*
    Given ‘M’ sorted arrays, find the K’th smallest number among all the arrays.
    Input: L1=[2, 6, 8], L2=[3, 6, 7], L3=[1, 3, 4], K=5
    Output: 4

    Here we can keep a minHeap like below
    Pop the elements from minHeap until we have popped our kth element
 */
fun findKthSmallestAmongSortedLists(list: MutableList<List<Int>>, k: Int): Int {
    // Triple will be ( value, index of list it was in, index within the list )
    //val minHeap = PriorityQueue<Triple<Int,Int,Int>>(compareBy { it.first })
    // Pair is 2D coordinate of the number
    val minHeap = PriorityQueue<Pair<Int,Int>>(compareBy { list[it.first][it.second] })
    var count = 0
    while(count < list.size) {
        minHeap.add(Pair(count, 0))
        count++
    }
    var popCount = 0
    var listIndex = -1
    var index = -1
    while(popCount < k) {
        if(index >= 0 && index < list[listIndex].size) {
            minHeap.add(Pair(listIndex, index))
        }
        // now we want to pop from minHeap, increment popCount
        val current = minHeap.poll()
        listIndex = current.first
        index = current.second + 1
        popCount++
    }
    return list[listIndex][index - 1]
}

/*
    Given an array of ‘K’ sorted LinkedLists, merge them into one sorted list.
    Input: L1=[2, 6, 8], L2=[3, 6, 7], L3=[1, 3, 4]
    Output: [1, 2, 3, 3, 4, 6, 6, 7, 8]

    Here we can use a minHeap, add index = 0 from each of the lists.
    Then pop 1 from the minHeap to add to the result, keep track of which list it came from
    Then add 1 from the list it came from to the minHeap.
      If the list is empty, pop from minHeap and keep going
    Repeat until done
    O(Nlogk) k is number of sorted lists. If we did brute force combine all lists then sort it is NlogN
 */
fun mergeSortedLists(list: MutableList<ListNode?>): ListNode? {
    // minHeap will store the ListNode and the index of the list it was in
    val minHeap = PriorityQueue<Pair<ListNode?,Int>>(compareBy {it.first?.value})

    // add all the first
    var count = 0
    while(count < list.size) {
        val firstNode = list[count]
        minHeap.add(Pair(firstNode,count))
        list[count] = firstNode?.next
        count++
    }
    // get root
    var (result, index) = minHeap.poll()
    var currentNode = result
    while(minHeap.isNotEmpty()) {
        if(list[index] != null) {
            // add to minHeap
            minHeap.add(Pair(list[index], index))
            list[index] = list[index]?.next
        }
        // get minNode
        val next = minHeap.poll()
        val nextNode = next.first
        // attach to result list
        currentNode?.next= nextNode
        // increment pointer
        currentNode = nextNode
        // keep track of index
        index = next.second
    }
    return result
}