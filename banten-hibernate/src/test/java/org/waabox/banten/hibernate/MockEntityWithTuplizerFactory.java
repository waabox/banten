package org.waabox.banten.hibernate;

import org.waabox.banten.core.Factory;

public class MockEntityWithTuplizerFactory
  implements Factory<MockEntityWithTuplizer> {

  @Override
  public MockEntityWithTuplizer create() {
    return new MockEntityWithTuplizer("foo");
  }

}
