import java.awt.GraphicsEnvironment
import scala.swing._
import java.awt.Graphics2D
import java.awt.Dimension
import java.awt.Font
import javax.swing.border.LineBorder
import java.awt.Color
import scala.swing.event.MouseClicked
import java.awt.event.MouseEvent
import scala.swing.event.MouseEntered
import scala.swing.event.MouseExited
import java.awt.event.MouseWheelEvent
import scala.swing.event.MouseWheelMoved
import scala.swing.event.KeyEvent
import scala.swing.event.KeyReleased
import scala.concurrent.future
import scala.concurrent.ExecutionContext.Implicits._
import scala.util._

class Cell(block: Int, id: Int) extends BoxPanel(Orientation.NoOrientation) {
  val cid = id
  val cblock = block
  var locked, userlock, mouseOver = false
  var str = "0"
  var pwidth, pheight = 0
  var center = (0,0)
  var row, col = 0
  val bgBase = new Color(230,230,230)
  background = bgBase
  var pbackground = background
  opaque = true
  def value = str.toInt
  minimumSize = new Dimension(30,30)
  
  border = new LineBorder(Color.GRAY)

  def addToRow(i: Int) {
    Area.cellRow(i) :+= this
    row = i
  }
  def addToCol(i: Int) {
    Area.cellCol(i) :+= this
    col = i
  }
  
  def reset() {
    locked = false
    userlock = false
    str = "0"
    background = bgBase
    repaint
  }
  
  def set(v: Int) {
    locked = true
    str = v.toString()
  }
  
  def isValueValid(v: Int): Boolean = {
    if(v == 0) true
    else if(v < 0 || v > 9) false
    else if(Area.block(block - 1).cell.exists(x => x.value == v  && x.cid != cid)) false 
    else if(Area.cellRow(row).exists(_.value == v)) false 
    else if(Area.cellCol(col).exists(_.value == v)) false  
    else true
  }
  
  def isCellValid(): Boolean = {
    if(value == 0) true
    else if(value < 0 || value > 9) false
    else if(Area.block(block - 1).cell.exists(x => x.value == value && x.cid != cid)) false
    else if(Area.cellRow(row).exists(x => x.value == value && x.col != col) ) false
    else if(Area.cellCol(col).exists(x => x.value == value && x.row != row)) false
    else true
  }
  
  def incCell() {
    var a = value
    do {a += 1; if(a > 9) a = 0}
    while(!isValueValid(a)) 
    str = a.toString()
    repaint
  }
  def decCell() {
    var a = value
    do  {a -= 1; if(a < 0) a = 9}
    while(!isValueValid(a))
    str = a.toString()
    repaint
  }
  
  def color() {
    background = bgBase
    if(locked) background = Color.LIGHT_GRAY
    else if(userlock) background = Color.ORANGE
    else if(mouseOver) background = Color.YELLOW
    else if ((!background.equals(Color.YELLOW)) && !isCellValid) background = Color.RED
  }
  
  listenTo(mouse.clicks, mouse.moves, mouse.wheel, keys)
  reactions += {
    case e if(locked) => 
    case e: MouseClicked if (e.peer.getButton() == MouseEvent.BUTTON3 && !userlock) => 
      //Area.numPopup(this, e.point.x, e.point.y)
      future {
        for(i <- 0 until 1000000) println(i * i)
        throw new Exception
      } onComplete {
        case Success(s) => Swing.onEDT {
          str = 2.toString
          repaint
        }
        case Failure(f) => Swing.onEDT {
          str = 3.toString
          repaint
        }
        case _ =>
      }
      
    case e: MouseClicked if (e.peer.getButton() == MouseEvent.BUTTON1 && (value != 0 || userlock)) => userlock = !userlock; repaint
    case e: MouseWheelMoved if (!userlock) => if (e.rotation < 0) incCell else decCell
    case e: MouseEntered => mouseOver = true; requestFocus; repaint
    case e: MouseExited => mouseOver = false; repaint
    case e: KeyReleased if(e.peer.getKeyChar().isDigit)=> str = e.peer.getKeyChar().toString
  }
  //GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames().foreach(println)
    override def paintComponent(g: Graphics2D) {
      color
      super.paintComponent(g)
      if (pwidth != size.width || pheight != size.height) {
        pwidth = size.width
        pheight = size.height
        font = new Font("Tahoma", Font.BOLD, size.height - 10)
        val strBounds = font.getStringBounds(str, g.getFontRenderContext())
        center = (size.getWidth().toInt / 2 - strBounds.getWidth().toInt / 2,
          size.getHeight().toInt / 2 + strBounds.getHeight().toInt / 4)
      }
      if (value != 0) g.drawString(str, center._1, center._2)
    }
  }