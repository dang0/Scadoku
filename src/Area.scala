import scala.swing.GridPanel
import java.awt.Dimension
import scala.swing.GridBagPanel
import scala.swing.FlowPanel

object Area extends GridPanel(3,3) {
  def numPopup(c: Cell, x: Int, y: Int) = (new NumPopup).show(c, x, y)
  val cellRow = Array.ofDim[Cell](10,0)
  val cellCol = Array.ofDim[Cell](10,0)
  private var i = 0
  private def blockID(): Int = { i += 1; i }
  val block = Array.fill(9)(new Block(blockID))
  preferredSize = new Dimension(300,300)
  //peer.add(Box.createVerticalStrut(1))
  (contents /: block) (_ += _)
  
  def reset() {
    block.foreach {_.reset}
  }
  
}

class Block(blockID: Int) extends GridPanel(3,3) {
  private var i = 0
  private def cellID(): Int = { i += 1; i }
  val cell = Array.fill(9)(new Cell(blockID,cellID))
  val rowcal = 3 * (math.floor((blockID - 1) / 3).toInt + 1)
  val colcal = 3 * ((blockID + 2) % 3 + 1) - 3
  cell.foreach { x => 
    //x.str = "1"
      if((1 to 3) contains x.cid) { x.addToRow(rowcal - 2) ; x.addToCol(((x.cid + 2) % 3 + 1) + colcal) }
      else if((4 to 6) contains x.cid) { x.addToRow(rowcal - 1) ; x.addToCol(((x.cid + 2) % 3 + 1) + colcal) }
      else if((7 to 9) contains x.cid) { x.addToRow(rowcal) ; x.addToCol(((x.cid + 2) % 3 + 1) + colcal) }
    }
  (contents /: cell) (_ += _)
  
  def reset() {
    cell.foreach {_.reset}
  }
}