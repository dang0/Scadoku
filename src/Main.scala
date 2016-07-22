import scala.swing._
import javax.swing.border._
import java.awt.Color
import scala.xml.Text
import scala.swing.event.Key
import java.awt.event.KeyEvent
import javax.swing.KeyStroke
import scala.swing.event.UIElementResized
import scala.swing.event.ComponentResized
import scala.swing.event.UIElementResized

object Main extends MainFrame with Reactor with App{

  centerOnScreen
  resizable = true
  minimumSize = new Dimension(300,300)
  maximumSize = new Dimension(600,600)
  title = "Scadoku"
    
  menuBar = new MenuBar {
    contents += (
      new Menu("File") {
        mnemonic = Key.F
        contents += (
          new MenuItem(new Action("Reset") { mnemonic = KeyEvent.VK_R; accelerator = Some(KeyStroke.getKeyStroke("ctrl R")); def apply { Area.reset } }),
          new MenuItem(new Action("Save") { mnemonic = KeyEvent.VK_S; accelerator = Some(KeyStroke.getKeyStroke("ctrl S")); def apply { println("save") } }),
          new MenuItem(new Action("Load") { mnemonic = KeyEvent.VK_L; accelerator = Some(KeyStroke.getKeyStroke("ctrl L")); def apply { FileIO.loadFile } }),
          new Separator,
          new MenuItem(new Action("Close") { mnemonic = KeyEvent.VK_C; accelerator = Some(KeyStroke.getKeyStroke("ctrl C")); def apply { closeOperation } }))
      },
      new Menu("Help") {
        mnemonic = Key.H
        contents += (
          new MenuItem(new Action("About") { def apply { About.show } }))
      })
  }
  
  contents = Area
  
  //var pheight = Main.size.height
  //var pwidth = Main.size.width
  listenTo(Main)
  reactions += {
    case e: UIElementResized => val b = e.source.bounds; e.source.size = new Dimension(math.min(b.getHeight().toInt,600), b.getHeight().toInt)
    //case x: UIElementResized if(pheight > 600 || pwidth > 600) => size = new Dimension(600,600); centerOnScreen; pheight = size.height ; pwidth = size.width
    //case x: UIElementResized if(pwidth != size.width && pheight != size.height) => val min = math.min(size.width, size.height); size = new Dimension(min,min); pheight = size.height ; pwidth = size.width 
    //case x: UIElementResized if(pwidth != size.width) => size = new Dimension(size.width, size.width); pheight = size.height ; pwidth = size.width
    //case x: UIElementResized if(pheight != size.height) => size = new Dimension(size.height, size.height); pheight = size.height ; pwidth = size.width
  }
  
  pack
  visible = true
}