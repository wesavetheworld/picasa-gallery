package web

import photos.LocalContent
import photos.Picasa
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

class SiteMapServlet : HttpServlet() {
  lateinit var render: Renderer
  lateinit var content: LocalContent

  override fun init(config: ServletConfig) {
    render = Renderer(config.servletContext)
    content = LocalContent(config.servletContext)
  }

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    resp.writer.use { out ->
      when (req.servletPath) {
        "/robots.txt" -> {
          resp.contentType = "text/plain"
          out.write("Sitemap: https://${req.getHeader("Host")}/sitemap.xml\n" +
              "User-Agent: *\n" +
              "Allow: /\n")
        }
        "/sitemap.xml" -> {
          resp.contentType = "text/xml"
          val picasa = Picasa(content)
          render("sitemap", null, mutableMapOf("gallery" to picasa.gallery, "host" to req.getHeader("Host")), resp)
        }
        else -> resp.sendError(SC_NOT_FOUND)
      }
    }
  }
}
