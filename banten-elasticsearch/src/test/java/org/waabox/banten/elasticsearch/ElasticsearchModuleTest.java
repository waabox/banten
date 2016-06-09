package org.waabox.banten.elasticsearch;

import static org.junit.Assert.*;

import org.elasticsearch.client.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.waabox.banten.elasticsearch.IndexManager;
import org.waabox.banten.testsupport.BantenTest;

@RunWith(SpringJUnit4ClassRunner.class)
@BantenTest(applicationClass = ElasticsearchSampleApplication.class)
public class ElasticsearchModuleTest {

  @Autowired private Client client;

  @Test public void test_indexCreation() {
    assertTrue(IndexManager.indexExists("test", client));
    assertTrue(IndexManager.indexExists("test2", client));
  }

}
