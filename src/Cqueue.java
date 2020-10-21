import java.util.Iterator;

/**
 * This javadoc demonstrates the use of our custom queue
 * @author Sidharrth Nagappan and Ahmed Sanad
 */

class Node<E> {
    E value;
    Node<E> next;

    /**
     * Used as node class contractor
     * @param value holds value of the node
     */
    public Node(E value) {
        this.value = value;
    }


    /**
     * Used to return node value for Iterable
     * @return the value of the node
     */
    public E getData()
    {
        return value;
    }

    /**
     * Used to return next node  for Iterable
     * @return returns the next node
     */
    public Node<E> getNext()
    {
        return next;
    }
}

public class Cqueue<E> implements Iterable<E> {
    private Node<E> head, tail;
    private int size = 0;

    /**
     * Used to add new element at the end of the queue
     * @param e  Object is passed
     */
    public void add(E e) {
        Node<E> newNode = new Node<>(e); // create new node

        if (tail != null){// last node is linked with new node, tail is the new node
            tail.next = newNode;
            tail = newNode;
        }else{
            head = tail = newNode; // this is the only existing node
        }
        size++;
    }

    /**
     * Used to remove head (First) value from the queue
     * @return returns the object in the removed node if size is 0 nothing is returned
     */

    public E removeFirst() {
        if (size != 0) {
            E temp = head.value;
            head = head.next;
            size--;

            if (head == null) {
                tail = null;
            }
            return temp;

        }
        else {
            return null;
        }

    }

    /**
     * Used to return head (First) value from the queue
     * @return returns the object in the first node if size is 0 nothing is returned
     */
    public E getHead() {
        if (size == 0) {
            return null;
        }
        else {
            return head.value;
        }
    }


    /**
     * Used to return head (First) value from the queue used for iterable
     * @return returns the object in the first node if size is 0 nothing is returned
     */
    public Node<E> getHeadNode() {
        if (size == 0) {
            return null;
        } else {
            return head;
        }
    }

    /**
     * Used to return queue size
     * @return int value, size of the queue
     */
    public int size() {
        return size;
    }


    /**
     * Used to return node value according to index
     * @param index int value which holds the index of the node
     * @return returns the value in the node based on the index, if size is 0 nothing is returned
     */
    public E get(int index) {
        if (index < 0) //invalid index
            return null;
        Node gvalue = head;
        if (head != null) {
            for (int i = 0; i < index; i++) {// if null go to next node
                if (gvalue.next == null)
                    return null;
                gvalue = gvalue.next;// holds next node
            }
            return (E) gvalue.value; //returns node value
        }
        return (E) gvalue;
    }

    /**
     * Used to return Iterator instance
     * @return returns Iterator instance
     */

    public Iterator<E> iterator()
    {
        return new ListIterator<E>(this);
    }

}

class ListIterator<E> implements Iterator<E> {

    Node<E> current;

    /**
     * Used to pass the queue and get first node value for iterable
     * @param list object list is passed
     */
    public ListIterator(Cqueue<E> list)
    {
        current = list.getHeadNode();
    }

    /**
     *
     * @return returns false if next element does not exist
     */
    public boolean hasNext()
    {
        return current != null;
    }

    /**
     *
     * @return returns value of node
     */
    public E next()
    {
        E data = current.getData();
        current = current.getNext();
        return data;
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
  
  