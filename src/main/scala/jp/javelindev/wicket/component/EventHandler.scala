package jp.javelindev.wicket.component

import org.apache.wicket.Component
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.AttributeAppender

import jp.javelindev.wicket.payload._

trait EventReactor extends Component {
  this: ReactableConponent =>
}

trait ReactableConponent extends Component {
  this: Component =>

  private val handlers = scala.collection.mutable.ArrayBuffer.empty[PartialFunction[Any, Unit]]
  final def handler( f:PartialFunction[Any,Unit]):Unit = {
    handlers += f
  }

  override final def onEvent(event:IEvent[_]):Unit = {
    val e = event.getPayload
    handlers.filter{ _ isDefinedAt e}.foreach{ f => f(e) }
  }

}

trait ImageRefreshReactor extends EventReactor {
  this: Component with ReactableConponent with ImageSrcComponent with SelectableComponent =>

  handler{
    case event:ImageRefresh[_] =>
      if (selected) {
        reload()
        selected = false
        event.addComponent(this)
      }
  }
}

trait RemoveReactor extends EventReactor {
  this:Component with WebMarkupContainer with ReactableConponent =>

  handler{
    case event:RemoveRequest => setVisibilityAllowed( !event.isRemove )
  }

}

