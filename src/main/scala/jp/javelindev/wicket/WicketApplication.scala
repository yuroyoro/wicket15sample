package jp.javelindev.wicket

import jp.javelindev.wicket.page.{CheckerBoardPage, HomePage}
import jp.javelindev.wicket.resource.{SimpleTextResource, SimpleTextResourceReference}
import org.apache.wicket.Application
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.mapper.ResourceMapper
import org.apache.wicket.request.resource.{ResourceReference, SharedResourceReference}

class WicketApplication extends WebApplication with Rss {

  var rssSource = new HaseriRss()

  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  def  getHomePage:Class[HomePage] = classOf[HomePage]

  override protected def init() = {
    super.init()
    getMarkupSettings().setDefaultMarkupEncoding("UTF-8")
    getRequestCycleSettings().setResponseRequestEncoding("UTF-8")

    mountPage("/home/${name}/address/${address}", getHomePage())
    mountPage("/checkerboard", classOf[CheckerBoardPage])

    getSharedResources().add("simpletext", SimpleTextResource.create())
    val reference = new SharedResourceReference("simpletext")
    mountSharedResource("/simpletext", reference)
  }

  def getRssSource():HaseriRss = rssSource

  override protected def onDestroy() = {
    rssSource.stopCrawlingThread()
    rssSource = null
    super.onDestroy()
  }
}
object WicketApplication {
  def get = Application.get().asInstanceOf[WicketApplication]

}
