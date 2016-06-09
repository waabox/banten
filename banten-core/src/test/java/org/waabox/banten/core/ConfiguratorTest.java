package org.waabox.banten.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.waabox.banten.core.Configurator;

public class ConfiguratorTest {

  @Test public void test() {
    MockEnvironment mockEnv = new MockEnvironment();
    mockEnv.setProperty("waabox.name", "emiliano");
    mockEnv.setProperty("waabox.age", "32");
    mockEnv.setProperty("waabox.email", "waabox@gmail.com");
    mockEnv.setProperty("waabox.smoke", "true");

    Configurator cfg = new Configurator("waabox", mockEnv);
    assertThat(cfg.get().get("name"), is("emiliano"));;
    assertThat(cfg.get().get("age"), is("32"));;
    assertThat(cfg.get().get("email"), is("waabox@gmail.com"));;
    assertThat(cfg.get().get("smoke"), is("true"));;

    assertThat(cfg.get("waabox.name"), is("emiliano"));
    assertThat(cfg.get("waabox.age", Integer.class), is(32));
    assertThat(cfg.getInt("waabox.age"), is(32));
    assertThat(cfg.getBoolean("waabox.smoke"), is(true));

    assertThat(cfg.env(), is((Environment) mockEnv));


    cfg.put("waabox.newproperty", "newone");

    assertThat(cfg.get().get("waabox.newproperty"), is("newone"));

    cfg.remove("waabox.newproperty");

    assertThat(cfg.get().get("waabox.newproperty"), nullValue());

  }

}
