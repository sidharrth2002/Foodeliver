import java.util.Iterator;

class Node<E> {
    E value;
    Node<E> next;
  
    public Node(E value) {
      this.value = value;
    }

    public E getData()
    {
        return value;
    }

    public Node<E> getNext()
    {
        return next;
    }
}
  
  public class Cqueue<E> implements Iterable<E> {
    private Node<E> head, tail;
    private int size = 0; 
    
  
    //Add value at the end 
    public void add(E e) {
        Node<E> newNode = new Node<>(e);

        if (tail != null){
            tail.next = newNode; 
        tail = newNode; 
        }else{
            head = tail = newNode;
        }
        size++;
    }
  
  
    // Remove head value
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

     //Return head value
     public E getHead() {
        if (size == 0) {
          return null;
        }
        else {
          return head.value;
        }
      }

      //Return head value
      public Node<E> getHeadNode() {
        if (size == 0) {
            return null;
        } else {
            return head;
        }
      }

    // Return queue size
    public int size() {
        return size;
    }


    // get value using index
    public E get(int index) {
        if (index < 0)
            return null;
        Node gvalue = head;
        if (head != null) {
            for (int i = 0; i < index; i++) {
                if (gvalue.next == null)
                    return null;

                gvalue = gvalue.next;
            }
            return (E) gvalue.value;
        }
        return (E) gvalue;
    }

      // return Iterator instance
      public Iterator<E> iterator()
      {
          return new ListIterator<E>(this);
      }

  }

  class ListIterator<E> implements Iterator<E> {
    Node<E> current;

      public ListIterator(Cqueue<E> list)
      {
          current = list.getHeadNode();
      }

      // returns false if next element does not exist
      public boolean hasNext()
      {
          return current != null;
      }

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
  
  