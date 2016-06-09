package org.waabox.banten.sample;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.waabox.banten.sample.SampleApplication;
import org.waabox.banten.testsupport.BantenTest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(SpringJUnit4ClassRunner.class)
@BantenTest(applicationClass = SampleApplication.class)
@WebIntegrationTest
public class SampleApplicationIntegrationTest {

  @Test public void testTime() throws Exception {

    // view the page.
    try (WebClient webClient = new WebClient()) {

      HtmlPage loginPage = webClient.getPage(
          "http://localhost:8080/login/web/form.html");
      HtmlForm loginForm = loginPage.getFormByName("login");
      loginForm.getInputByName("username").setValueAttribute("root@banten.org");
      loginForm.getInputByName("password").setValueAttribute("root");
      loginForm.getButtonByName("submit-btn").click();

      HtmlPage page = webClient.getPage(
        "http://localhost:8080/time/time/view.html");
      String xmlPage = page.asXml();

      assertTrue(xmlPage.contains("Chuck Norris CAN get bacon from a cow"));
      assertTrue(xmlPage.contains("Timezones"));
      // checks the weblet renders the chuck norris image.
      assertTrue(xmlPage.contains(
          "http://www.writecamp.org/writecamp//files/copy_images/Vd3MJo.jpg"));

      // post to the page a new GTM value.
      HtmlPage page2 = webClient.getPage(
        "http://localhost:8080/time/time/view.html");
      HtmlForm form = page2.getForms().get(0);
      form.getInputByName("gmt").setValueAttribute("GMT+3");

      HtmlPage result = form.getButtonByName("submit-btn").click();
      String xmlPage2 = result.asXml();

      // verify that within the new page contains the posted value.
      assertTrue(xmlPage2.contains("GMT+3"));
    }

  }

}
