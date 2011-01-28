package jp.javelindev.wicket.page

import jp.javelindev.wicket.component._
import jp.javelindev.wicket.payload._
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.behavior.AttributeAppender
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.{WebMarkupContainer, WebPage}
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.{Model,AbstractReadOnlyModel}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.mapper.parameter.PageParameters

class CheckerBoardPage(parameters:PageParameters) extends WebPage(parameters) {

  val row = new RepeatingView("gridrow")
  add(row)

  (1 to 4).foreach{ i =>
    val container = new WebMarkupContainer(row.newChildId())
    row.add(container)
    val column = new RepeatingView("rssImage")
    (1 to 6).foreach{ j =>
      val image = new RssImage(column.newChildId())
      if(j == 1) image.add(new AttributeAppender("class", Model.of("first"), " "))
      column.add(image)
    }
    container.add(column)
  }

  val refreshLink = new AjaxLink[Void]("refreshLink") {
    override def onClick(target:AjaxRequestTarget )=
      send(getPage(), Broadcast.BREADTH, new ImageRefresh[AjaxLink[Void]](this, target))
  }
  add(refreshLink)

  var displayStatus = true
  val displayStatusModel = new AbstractReadOnlyModel[java.lang.Boolean] {
    override def getObject():java.lang.Boolean = displayStatus
  }

  val removeButton = new RemoveLink("removeButton", displayStatusModel){
    override def onClick():Unit = {
      send(getPage(), Broadcast.BREADTH, new RemoveRequest(this, displayStatus))
      displayStatus = !displayStatus
    }
  }
  add(removeButton)

  val buttonLabel = new RemoveLabel("buttonLabel")
  removeButton.add(buttonLabel)

}
