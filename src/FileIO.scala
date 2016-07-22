import scala.io.Source
import java.io.FileNotFoundException
import scala.swing.FileChooser
import java.io.File
import javax.swing.filechooser.FileFilter

object FileIO {
    
  def loadFile() {
    val fc = new FileChooser(new File("."))
    fc.title = "Load a puzzle"
    fc.multiSelectionEnabled = false
    fc.fileFilter = new FileFilter() { 
      def accept(file: File): Boolean = {
        if(file.getName().startsWith(".")) false
        else if(file.isDirectory() || file.getName().endsWith(".scd")) true
        else false        
      }
      def getDescription(): String = "*.scd files"
    }
    if(FileChooser.Result.Approve == fc.showOpenDialog(null)) read(fc.selectedFile)
  }
  
  def read(filename: File) {
    Area.reset
    try {
      var x = 1
      var y = 0
      for (line <- Source.fromFile(filename).getLines().slice(1, 10); if(line.length() > 0)) {
          for (c <- line.toCharArray().slice(1, 10)) {
              c match {
                case '#' => 
                case '1' => Area.cellCol(x)(y).set(1)
                case '2' => Area.cellCol(x)(y).set(2)
                case '3' => Area.cellCol(x)(y).set(3)
                case '4' => Area.cellCol(x)(y).set(4)
                case '5' => Area.cellCol(x)(y).set(5)
                case '6' => Area.cellCol(x)(y).set(6)
                case '7' => Area.cellCol(x)(y).set(7)
                case '8' => Area.cellCol(x)(y).set(8)
                case '9' => Area.cellCol(x)(y).set(9)
                case e => throw new java.lang.IllegalArgumentException("Unknown argument: %s" format e)
              }
              x += 1
          }
          x = 1
          y += 1
    }
      Main.repaint
      } catch {
      case ex: FileNotFoundException => println("Could not find file")
      case ex: java.io.IOException => println("IO error opening file")
      case ex => println("Caught exception: %s" format ex)
    }
  }
  
  def write() {
    
  }
}