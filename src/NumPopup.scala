

import scala.swing._
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.Graphics2D
import scala.collection.mutable.ArrayBuffer

class MenuItemListener(c: Cell) extends ActionListener {
  override def actionPerformed(e: ActionEvent) {
    //println("item " + e.getActionCommand())
    if(e.getActionCommand().equals("clear")) c.str = "0"
    else if(e.getActionCommand().equals("no valid numbers")) null
    else c.str = e.getActionCommand()
    c.repaint
  }
}

class NumPopup extends JPopupMenu {
  var numbers = Array[Int]()
  var empty = true
  def show(c: Cell, x: Int, y: Int) {
    if(c.value != 0) {add( new JMenuItem("clear"){ addActionListener(new MenuItemListener(c))} ); empty = false}
    numbers = Array.fill(10)(0)
    (1 to 9) foreach {x =>
      if(c.isValueValid(x)) {numbers(x) = x; empty = false}
      else numbers(x) = 0
    }
    numbers.foreach { x=> if(x != 0 && x != c.value) add( new JMenuItem(x.toString()){ addActionListener(new MenuItemListener(c))} ) }
    if(empty) add( new JMenuItem("no valid numbers"){ addActionListener(new MenuItemListener(c))} )
    //super.show(c.peer, x, y)
    super.show(Component.wrap(c.peer),x,y)
    
  }
}