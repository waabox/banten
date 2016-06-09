package org.waabox.banten.login.domain;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.waabox.banten.login.domain.CreateRolesFromContextTask;
import org.waabox.banten.login.domain.UserRepository;
import org.waabox.banten.shiro.ShiroConfigurationApi;
import org.waabox.banten.testsupport.BantenTest;

@RunWith(SpringJUnit4ClassRunner.class)
@BantenTest(applicationClass = SampleLoginApplication.class)
public class CreateRolesFromContextTaskTest {

  @Autowired private UserRepository repository;
  @Autowired private ShiroConfigurationApi api;
  @Autowired private ApplicationContext source;

  @Test @Transactional
  public void test() {
    // this only runs when an application context with a web module starts.
    new CreateRolesFromContextTask(repository, api).onApplicationEvent(
        new ContextRefreshedEvent(source));
    assertThat(repository.getRoles().size(), is(2));
  }

}
