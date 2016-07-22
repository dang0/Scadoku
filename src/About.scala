import scala.swing.Dialog

object About {

  val message = "I made this."
  val title = "Scadoku - About"
  val info = Dialog.Message.Info
  
  def show() {
    Dialog.showMessage(Main.contents(0), message, title, info, null)
  }
  
}