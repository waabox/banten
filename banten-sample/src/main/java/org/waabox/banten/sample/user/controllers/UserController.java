package org.waabox.banten.sample.user.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.waabox.banten.sample.user.domain.User;
import org.waabox.banten.sample.user.domain.UserFactory;
import org.waabox.banten.sample.user.domain.UserRepository;

/** The user controller.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@RestController
@RequestMapping("/users/")
public class UserController {

  /** The user repository. */
  private final UserRepository repository;

  /** The user factory. */
  private final UserFactory userFactory;

  /** Creates a new instance of the controller.
   * @param theUSerRepository the user repository.
   * @param theUserFactory the user factory.
   */
  public UserController(final UserRepository theUSerRepository,
      final UserFactory theUserFactory) {
    repository = theUSerRepository;
    userFactory = theUserFactory;
  }

  @Transactional
  @RequestMapping(value = "/list.html", method = RequestMethod.GET)
  public ModelAndView view() {
    ModelAndView mav = new ModelAndView("view");
    mav.addObject("users", repository.list());
    return mav;
  }

  @Transactional
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public ModelAndView save(
      @RequestParam("name") final String name,
      @RequestParam("gmt") final String gmt) {
    User user = userFactory.create(name, gmt);
    repository.save(user);
    return new ModelAndView("redirect:list.html");
  }

  @RequestMapping(value = "/samplePicture", method = RequestMethod.GET)
  public ModelAndView samplePicture(
      @Value("#{request.getAttribute('quote')}") final String quote) {
    ModelAndView mav = new ModelAndView("samplePicture");
    return mav;
  }

}
