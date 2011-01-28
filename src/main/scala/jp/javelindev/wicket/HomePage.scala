package jp.javelindev.wicket.page

import jp.javelindev.wicket.component._
import jp.javelindev.wicket.payload.ImageRefresh
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.behavior.AttributeAppender
import org.apache.wicket.event.Broadcast
import org.apache.wicket.markup.html.{WebMarkupContainer, WebPage}
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.{Model,AbstractReadOnlyModel}
import org.apache.wicket.request.mapper.parameter.PageParameters

class HomePage(parameters:PageParameters) extends WebPage {

  val name = parameters.get("name").toString("no name")
  val address = parameters.get("address").toString("no address")

  add(new Label("message", "name: " + name + " address: " + address))

  val targetBox = new TargetBox("targetBox")
  add(targetBox.setOutputMarkupPlaceholderTag(true))

  val displayStatusModel = new AbstractReadOnlyModel[java.lang.Boolean] {
    override def getObject():java.lang.Boolean = targetBox.isVisibilityAllowed()
  }

  val removeButton = new RemoveLink("removeButton", displayStatusModel)
  add(removeButton)

  val buttonLabel = new RemoveLabel("buttonLabel")
  removeButton.add(buttonLabel)

}
