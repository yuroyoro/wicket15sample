package jp.javelindev.wicket.component

import org.apache.wicket.Component
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.AttributeModifier
import org.apache.wicket.model.AbstractReadOnlyModel
import org.apache.wicket.behavior.AttributeAppender
import com.sun.syndication.feed.synd.SyndEnclosure


import jp.javelindev.wicket._

class RssImage(id:String) extends AjaxLink[java.lang.Void](id)
  with ReactableConponent
  with ImageSrcComponent
  with SelectableComponent
  with ImageRefreshReactor
  with RemoveReactor {

  import scala.util.control.Exception._

  def getImageSource() =  allCatch opt{
    val app = WicketApplication.get
    val rss = app.getRssSource
    val entry = rss.getRandomEntry
    entry.getEnclosures.get(0).asInstanceOf[SyndEnclosure].getUrl
  }

  override def onClick(target:AjaxRequestTarget ) = {
    selected = !selected;
    target.add(this);
  }
}

trait ImageSrcComponent extends WebMarkupContainer {

  private var source:Option[String] = None
  val NO_IMAGE_URL = Some(urlFor(getApplication().getSharedResources().get(classOf[RssImage], "noImage.jpg", getWebRequest().getLocale(), null, null, false), null).toString)

  add(new AttributeModifier("src", new AbstractReadOnlyModel[String]{
      override def getObject():String = imageSource()
  }))

  private def imageSource():String = source match {
    case Some(src) => src
    case None =>
      source = getImageSource orElse NO_IMAGE_URL
      source.get
  }

  def getImageSource():Option[String]
  def reload() = source = None
}

trait SelectableComponent extends Component {
  var selected = false

  add(new AttributeAppender("class", new AbstractReadOnlyModel[String]{
      override def getObject():String  = if(selected) "selected" else  "not-selected"
  }, " "))
}

