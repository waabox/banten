package org.waabox.banten.hibernate;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.waabox.banten.core.Factory;
import org.waabox.banten.hibernate.PersistenceUnit;

public class PersistenceUnitTest {

  @Test
  public void test() {
    PersistenceUnit pu = new PersistenceUnit(Object.class);
    assertTrue(pu.getPersistenceClass() == Object.class);
    assertThat(pu.getFactory(), nullValue());
    assertThat(pu.hasCustomFactory(), is(false));
  }

  @Test
  public void test_withFactory() {
    PersistenceUnit pu = new PersistenceUnit(String.class, MockFactory.class);
    assertTrue(pu.getPersistenceClass() == String.class);
    assertTrue(pu.getFactory() == MockFactory.class);
    assertThat(pu.hasCustomFactory(), is(true));
  }

  private static class MockFactory implements Factory<String> {
    @Override
    public String create() {
      return "yes";
    }
  }

}
