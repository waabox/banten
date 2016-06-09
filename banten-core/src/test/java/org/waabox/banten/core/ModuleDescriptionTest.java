package org.waabox.banten.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.waabox.banten.core.ModuleDescription;

public class ModuleDescriptionTest {

  @Test public void test() {
    ModuleDescription moduleDescription;
    moduleDescription = new ModuleDescription(
        "test-name", "org.waabox.banten", "test", "../banten-core");

    assertThat(moduleDescription.getClasspath(), is("org.waabox.banten"));
    assertThat(moduleDescription.getName(), is("test-name"));
    assertThat(moduleDescription.getNamespace(), is("test"));
    assertThat(moduleDescription.getRelativePath(), is("../banten-core"));

  }

}
