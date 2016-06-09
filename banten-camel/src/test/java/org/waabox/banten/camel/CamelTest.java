package org.waabox.banten.camel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.waabox.banten.camel.MessageRequest;
import org.waabox.banten.camel.MessageResponse;
import org.waabox.banten.camel.ServiceBus;
import org.waabox.banten.testsupport.BantenTest;

/**
 * @author waabox (waabox[at]gmail[dot]com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@BantenTest(applicationClass = SampleCamelApplication.class)
public class CamelTest {

  @Autowired private ServiceBus serviceBus;

  @Test public void testEventBus() {
    MessageResponse response;
    response = serviceBus.request(
        new MessageRequest("direct:hello", "just a word"));

    String theResponse = (String) response.response();

    assertThat(response.isFailed(), is(false));
    assertThat(theResponse, is("just a word"));
  }

}
